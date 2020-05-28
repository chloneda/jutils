package com.chloneda.jutils.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.chloneda.jutils.commons.CheckUtils;
import com.chloneda.jutils.commons.StringUtils;
import com.chloneda.jutils.json.GsonUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @Created by chloneda
 * @Description:
 */
public class ElasticsearchTransportClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchTransportClient.class);

    private static TransportClient client;
    private EsConfig esConfig;

    public ElasticsearchTransportClient(String host, int port) {
        initClient(host, port);
    }

    public ElasticsearchTransportClient(TransportClient client) {
        initClient(client);
    }

    public ElasticsearchTransportClient(EsConfig esConfig) {
        this.esConfig = esConfig;
        initClient(esConfig);
    }

    public static TransportClient initClient(TransportClient client) {
        setClient(client);
        return client;
    }

    public static TransportClient initClient(String host, int port) {
        try {
            // 忽略连接节点的集群名称验证
            Settings settings = Settings.builder().put("client.transport.ignore_cluster_name", true).build();

            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (Throwable e) {
            LOGGER.error("ES client: {" + host + ":" + port + "} 初始化失败！", e);
        }
        return client;
    }

    public static TransportClient initClient(String host, int port, String clusterName) {
        try {
            // 忽略连接节点的集群名称验证
            Settings settings = Settings.builder().put("cluster.name", clusterName).build();

            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (Throwable e) {
            LOGGER.error("ES client: {" + host + ":" + port + "} 初始化失败！", e);
        }
        return client;
    }

    public static TransportClient initClient(EsConfig esConfig) {
        // 连接集群的设置
        Settings settings = Settings.builder()
                .put("cluster.name", esConfig.getClusterName() != null ? esConfig.getClusterName() : EsConfig.DEFAULT_ES_CLUSTER_NAME)
                .put("http.type", "netty3")
                .put("client.transport.sniff", esConfig.isSniff())
                .put("transport.type", "netty3")
                .build();
        try {
//            client = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(
//                            new TransportAddress(InetAddress.getByName(esConfig.getHost()),esConfig.getPort()));

            // ES6.4 之前获取 client 方式
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress.getByName(esConfig.getHost()), esConfig.getPort()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * @param indexName 创建索引名称
     * @return
     */
    public static boolean createIndex(String indexName) {
        boolean result = false;
        try {
            getClient().admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            getClient().close();
        }
        return result;
    }

    /**
     * 创建指定索引的类型
     *
     * @param indexName
     * @param type
     * @return
     */
    public static boolean createType(String indexName, String type) {
        boolean result = false;
        try {
            if (isExitIndex(indexName)) {
                XContentBuilder builder = XContentFactory.jsonBuilder()
                        .startObject().startObject("properties").endObject()
                        .endObject();

                PutMappingRequest mapping = Requests
                        .putMappingRequest(indexName).type(type)
                        .source(builder);
                getClient().admin().indices().putMapping(mapping).actionGet();
                result = true;
            }
        } catch (Exception e) {
            getClient().close();
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取索引库下的所有type
     *
     * @param indexName
     * @return
     */
    public static Set<String> getIndexAllTypes(String indexName) {
        Set<String> types = new HashSet<>();
        try {
            if (isExitIndex(indexName)) {
                ClusterStateResponse response = getClient().admin().cluster()
                        .prepareState().setIndices(indexName).get();
                MetaData metaData = response.getState().metaData();
                if (metaData.iterator().hasNext()) {
                    IndexMetaData indexMetaData = metaData.iterator().next();
                    for (ObjectCursor<MappingMetaData> cursor : indexMetaData
                            .getMappings().values()) {
                        MappingMetaData mappingMD = cursor.value;
                        types.add(mappingMD.type());
                    }
                }
            }
        } catch (Exception e) {
            getClient().close();
            e.printStackTrace();
        }
        return types;
    }

    /**
     * @param indexName
     * @param typeName
     * @param setMap
     * @param type
     * @return
     */
    public static boolean createIndex(String indexName, String typeName, Map<String, String> setMap, Map<String, Object> type) {
        IndicesAdminClient indicesAdminClient = getClient().admin().indices();
        // setting
        Settings settings = Settings.builder().put(setMap).build();
        // mapping
        CreateIndexResponse response = indicesAdminClient.prepareCreate(indexName)
                .setSettings(settings)
                .addMapping(typeName, type)
                .get();
        return response.isAcknowledged();
    }

    /**
     * 创建索引和类型,并项其中插入数据
     * 如果索引和类型存在,则直接插入数据
     *
     * @param id    文档id
     * @param datas 插入数据
     */
    public static boolean createIndexWithData(String indexName, String type, String id, Map<String, Object> datas) {
        boolean result = false;
        try {
            IndexResponse IndexResponse = null;
            if (StringUtils.isBlank(id)) {
                IndexResponse = getClient().prepareIndex(indexName, type).setSource(datas).execute().actionGet();
            } else {
                IndexResponse = getClient().prepareIndex(indexName, type, id).setSource(datas).execute().actionGet();
            }
            if (IndexResponse.status() == RestStatus.CREATED) {
                LOGGER.info("Create index is OK!");
                result = true;
            }
        } catch (Exception e) {
            getClient().close();
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除索引
     *
     * @param indexName 删除索引名称
     * @return
     */
    public static boolean deleteIndex(String indexName) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
        IndicesExistsResponse inExistsResponse = getClient().admin().indices().exists(inExistsRequest).actionGet();

        // 先判断索引索引是否存在
        if (inExistsResponse.isExists()) {
            DeleteIndexResponse diResponse = getClient().admin().indices().prepareDelete(indexName)
                    .execute().actionGet();
            return diResponse.isAcknowledged();
        }
        return false;
    }

    /**
     * 根据类型清除所有索引
     *
     * @param indexName
     * @param type
     * @return
     */
    public static boolean deleteIndexWithType(String indexName, String type) {
        boolean result = false;
        try {
            if (isExitIndex(indexName)) {
                getClient().prepareDelete().setIndex(indexName).setType(type).execute().actionGet();
                result = true;
            }
        } catch (Exception e) {
            getClient().close();
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取类型下的所有字段和字段类型
     *
     * @param host      is的地址
     * @param port      端口号
     * @param indexName 索引库名称
     * @param typeName  类型名
     * @return 返回是字段名 和 对应的json格式的类型 例如 age : {"index":"http_log","type":"long"}
     */
    public static Map<String, String> getFieldsIndexTypes(String host, int port, String indexName, String typeName) {
        ArrayList<String> types = new ArrayList<String>();
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            HttpGet get = new HttpGet(
                    "http://" + host + ":" + port + "/" + indexName + "/" + typeName + "/_mapping");
            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(20000)
                    .setConnectTimeout(20000).setSocketTimeout(20000).build();

            get.setConfig(config);
            response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                return listFields(indexName, is, typeName);
            }
        } catch (Exception e) {
            //getClient().close();
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> listFields(String indexName, InputStream is, String TypeName)
            throws IOException {
        Map<String, String> hm = new HashMap<String, String>();
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = is.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        String str = out.toString();
        JSONObject obj = (JSONObject) JSONObject.parse(str);
        JSONObject obj1 = (JSONObject) obj.get(indexName);
        JSONObject obj2 = (JSONObject) obj1.get("mappings");
        JSONObject obj3 = (JSONObject) obj2.get(TypeName);
        JSONObject obj4 = (JSONObject) obj3.get("properties");
        Iterator<String> it = obj4.keySet().iterator();
        while (it.hasNext()) {
            Map<String, String> map = new HashMap<String, String>();
            String key = it.next();
            JSONObject fieldInfo = (JSONObject) obj4.get(key);
            String type = fieldInfo.getString("type");
            String analyzer = fieldInfo.getString("index_analyzer");
            String index = fieldInfo.getString("index");
            String store = fieldInfo.getString("store");
            map.put("store", store);
            if (!StringUtils.isBlank(type)) {
                if (type.equals("string")) {
                    map.put("type", "String");
                    if (index != null) {
                        if (index.equals("not_analyzed")) {
                            map.put("index", "not_analyzed");
                        } else if (index.equals("no")) {
                            map.put("index", "no");
                        }
                    } else {
                        map.put("type", "String");
                        map.put("index", "analyzed");
                        map.put("analyzer", analyzer);
                    }
                } else {
                    if (index == null) {
                        map.put("index", "indexed");
                        if (type.equals("date")) {
                            map.put("type", "date");
                            map.put("format", fieldInfo.getString("format"));
                        } else {
                            map.put("type", type);
                        }
                    } else if (index.equals("no")) {
                        map.put("index", "no");
                        if (type.equals("date")) {
                            map.put("type", "date");
                            map.put("format", fieldInfo.getString("format"));
                        } else {
                            map.put("type", type);
                        }
                    }
                }
            } else {
                map.put("type", "object");
                String content = fieldInfo.getString("properties");
                if (!StringUtils.isBlank(content)) {
                    map.put("content", content);
                }
            }
            JSON.toJSONString(map);
            hm.put(key, JSON.toJSONString(map));
        }
        return hm;
    }

    /**
     * match匹配type下的所有的字段进行查询
     *
     * @param esclient
     * @param elasticIndex
     * @param TypeName
     * @return
     */
    public static String matchAllQuery(TransportClient esclient, String elasticIndex, String TypeName) {

        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse sr = esclient.prepareSearch(elasticIndex)
                .setTypes(TypeName).setQuery(qb).execute().actionGet();
        SearchHits hits = sr.getHits();
        JSONArray jsonArray = hitsToJSONArray(hits);
        return jsonArray.toString();
    }

    /**
     * match查询
     * 对内容进行多个字段进行索引查询
     *
     * @param esclient     es客户端对象
     * @param elasticIndex 对应索引
     * @param TypeName     类型
     * @param query        需要进行查询的内容
     * @param fieldNames   检索的字段名称
     * @return
     */
    public static String matchQuery(TransportClient esclient, String elasticIndex, String TypeName
            , String query, String... fieldNames) {
        QueryBuilder qb = QueryBuilders.multiMatchQuery(query, fieldNames);
        SearchResponse sr = esclient.prepareSearch(elasticIndex)
                .setTypes(TypeName).setQuery(qb).get();
        SearchHits hits = sr.getHits();
        JSONArray jsonArray = hitsToJSONArray(hits);
        return jsonArray.toString();
    }

    private static JSONArray hitsToJSONArray(SearchHits hits) {
        JSONArray jsonArray = new JSONArray();
        if (hits.getTotalHits() > 0) {
            for (SearchHit hit : hits) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id", hit.getId());
                Map<String, Object> map = hit.getSourceAsMap();
                Set<String> flds = map.keySet();
                for (String field : flds) {
                    jsonObject.put(field, map.get(field));
                }
                jsonArray.add(jsonObject);
            }
        } else {
            LOGGER.info("Nothing!");
        }
        return jsonArray;
    }

    /**
     * 获取所有索引
     *
     * @return
     */
    public static Set<String> getAllIndexes() {
        ActionFuture<IndicesStatsResponse> isr = getClient()
                .admin()
                .indices()
                .stats(new IndicesStatsRequest().all());
        Set<String> indices = isr.actionGet().getIndices().keySet();
        return indices;
    }

    /**
     * 判断指定的索引名是否存在
     *
     * @param indexName 索引名
     * @return
     */
    public static boolean isExitIndex(String indexName) {
        IndicesExistsResponse response = getClient()
                .admin()
                .indices()
                .exists(new IndicesExistsRequest(new String[]{indexName}))
                .actionGet();
        return response.isExists();
    }

    /**
     * 判断指定的索引的类型是否存在
     *
     * @param indexName 索引名
     * @param indexType 索引类型
     * @return
     */
    public static boolean isExistsIndexType(String indexName, String indexType) {
        TypesExistsResponse response = getClient()
                .admin()
                .indices()
                .typesExists(new TypesExistsRequest(new String[]{indexName}, new String[]{indexType}))
                .actionGet();
        LOGGER.info("The response json:{} " + GsonUtils.getGson().toJson(response));
        return response.isExists();
    }

    public static MetaData getMetaData() {
        MetaData metaData = ((ClusterStateResponse) getClient()
                .admin()
                .cluster()
                .prepareState()
                .execute()
                .actionGet())
                .getState()
                .getMetaData();
        return metaData;
    }

    public static void setClient(TransportClient transportClient) {
        CheckUtils.requireNotNull(client, "The client is null !");
        client = transportClient;
    }

    public static TransportClient getClient() {
        CheckUtils.requireNotNull(client, "The client is null !");
        return client;
    }

    public static void close(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        try {
            getAllIndexes().size();
            return true;
        } catch (Exception var) {
            var.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        EsConfig esConfig = new EsConfig();
        esConfig.setHost("127.0.0.1");
        esConfig.setPort(9300);
        esConfig.setSniff(false);
        esConfig.setClusterName("elastic");
        ElasticsearchTransportClient.initClient(esConfig);
        //TransportClient client = elasticSearchUtils.getClient();
        Set set = ElasticsearchTransportClient.getAllIndexes();

        System.out.println(set);
//        JSONObject jsonObject = initJson();
//        System.out.println(((Map) ((List) jsonObject.get("sort")).get(0)).get("sortField"));
//        SearchRequestBuilder sBuilder = null;
//        ElasticsearchUtils.build(jsonObject, "compex_test2");


    }

    public static JSONObject initJson() {
        JSONObject jsonObj = new JSONObject();

        //聚合分组
        List groupList = new ArrayList<Map>();
        HashMap group = new HashMap();
        group.put("groupField", "aaa");
        group.put("groupFilter", "aaa");
        group.put("function", "min");
        group.put("sortType", "desc");
        group.put("size", "33");
        groupList.add(group);
        jsonObj.put("group", groupList);

        //条件
        List counterList = new ArrayList<Map>();
        HashMap counter = new HashMap();
        counter.put("countField", "username");
        counter.put("countAlias", "用户名");
        counter.put("isCount", "true");
        counterList.add(counter);
        jsonObj.put("counter", counterList);

        //排序
        List sortList = new ArrayList<Map>();
        HashMap sort = new HashMap();
        sort.put("sortField", "bbb");
        sort.put("sortType", "dese");
        sortList.add(sort);
        jsonObj.put("sort", sortList);

        //条件
        List<Map<String, String>> filtersList = new ArrayList<Map<String, String>>();
        HashMap filters = new HashMap();
        filters.put("filterField", "ccc");
        filters.put("filterFun", "and");
        filters.put("filterValue", "2019-04-23T22:14:32.400+08:00");
        filtersList.add(filters);

//        HashMap filters2 = new HashMap();
//        filters2.put("filterField", "ccc");
//        filters2.put("filterFun", "and");
//        filters2.put("filterValue", "2019-04-22T11:17:22.929+08:00");
//        filtersList.add(filters2);

        jsonObj.put("filters", filtersList);

        //Limit
        HashMap limit = new HashMap();
        limit.put("from", 0);
        limit.put("size", 10);
        jsonObj.put("limit", limit);

        //统计

        return jsonObj;
    }


    public static void build(JSONObject jsonObject, String... indices) {
        SearchRequestBuilder searchBuilder = getClient().prepareSearch(indices);

        //聚合分组
        //searchBuilder = groupby(searchBuilder, (List) jsonObject.get("group"));
        //条件
        searchBuilder = filters(searchBuilder, (List) jsonObject.get("filters"));
        //排序
        searchBuilder = sort(searchBuilder, (List) jsonObject.get("sort"));
        //limit
        searchBuilder = limit(searchBuilder, (Map) jsonObject.get("limit"));

        SearchResponse response = searchBuilder.setExplain(true).execute().actionGet();
        System.out.println(response.toString());

    }

    //聚合分组
    public static SearchRequestBuilder groupby(SearchRequestBuilder searchBuilder, List<Map<String, String>> groupBy) {

        AggregationBuilder aggregation = null;
        for (int i = 0; i < groupBy.size(); i++) {
            Map<String, String> groupByMap = groupBy.get(i);
            String filterField = groupByMap.get("groupField");
            String groupFilter = groupByMap.get("groupFilter");
            String function = groupByMap.get("function");
            String sortType = groupByMap.get("sortType");
            String size = groupByMap.get("size");
            if (function.equals("min")) {
                aggregation = AggregationBuilders.min("agg").field(groupFilter);
            } else if (function.equals("max")) {
                aggregation = AggregationBuilders.max("agg").field(groupFilter);
            } else if (function.equals("sum")) {
                aggregation = AggregationBuilders.sum("agg").field(groupFilter);
            } else if (function.equals("avg")) {
                aggregation = AggregationBuilders.avg("agg").field(groupFilter);
            } else if (function.equals("stats")) {
                aggregation = AggregationBuilders.stats("agg").field(groupFilter);
            }

//            SearchRequestBuilder sBuilder = client.prepareSearch("dns_log")
//                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
//                .setQuery(QueryBuilders.termQuery("DVC_A", "192.167.4.107"))                 // Query
//                //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(20))     // Filter
//                .setFrom(0).setSize(60).setExplain(true)
//                .get();

//            SearchResponse response = client.prepareSearch()
//                    //.setQuery(query)
//                    .setFrom(0).setSize(60).setExplain(true)
//                    .execute()
//                    .actionGet();
        }

        return searchBuilder.addAggregation(aggregation);
    }

    //条件
    public static SearchRequestBuilder filters(SearchRequestBuilder searchBuilder, List<Map<String, String>> filters) {
        QueryBuilder queryBuilder = null;
        for (int i = 0; i < filters.size(); i++) {
            Map<String, String> filterMap = filters.get(i);
            String filterField = filterMap.get("filterField");
            String filterFun = filterMap.get("filterFun");
            String filterValue = filterMap.get("filterValue");
            if (filterFun.equals("and")) {
                queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery(filterField, filterValue));
            } else if (filterFun.equals("or")) {
                queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.termQuery(filterField, filterValue));
            } else if (filterFun.equals("not")) {
                queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(filterField, filterValue));
            }
        }
        return searchBuilder.setQuery(queryBuilder);
    }

    //统计
    public void counter() {

    }

    //排序
    public static SearchRequestBuilder sort(SearchRequestBuilder searchBuilder, List<Map<String, String>> sort) {
        //SortBuilder sortBuilder=null;
        for (int i = 0; i < sort.size(); i++) {
            Map<String, String> filterMap = sort.get(i);
            String sortField = filterMap.get("sortField");
            String sortType = filterMap.get("sortType");
            if (sortType.equals("ase")) {
                searchBuilder.addSort(sortField, SortOrder.ASC);
            } else {
                searchBuilder.addSort(sortField, SortOrder.DESC);
            }
        }
        return searchBuilder;
    }

    //Limit
    public static SearchRequestBuilder limit(SearchRequestBuilder searchBuilder, Map<Integer, Integer> range) {
        QueryBuilder queryBuilder = null;
        Integer from = range.get("from");
        Integer size = range.get("size");
        searchBuilder.setFrom(from.intValue()).setSize(size.intValue());
        return searchBuilder;
    }

}
