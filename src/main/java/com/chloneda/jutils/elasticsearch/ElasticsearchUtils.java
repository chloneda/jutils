package com.chloneda.jutils.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.chloneda.jutils.commons.CheckUtils;
import com.chloneda.jutils.json.GsonUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by chloneda
 * Description:
 */
public class ElasticsearchUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchUtils.class);

    private static TransportClient client;
    private EsConfig esConfig;

    public ElasticsearchUtils(EsConfig esConfig) {
        this.esConfig = esConfig;
        initClient(esConfig);
    }

    public static TransportClient initClient(EsConfig esConfig) {
        // 连接集群的设置
        Settings settings = Settings.builder()
                .put("cluster.name", esConfig.getClusterName() != null ? esConfig.getClusterName() : EsConfig.DEFAULT_ES_CLUSTER_NAME)
                .put("client.transport.sniff", esConfig.isSniff())
                .build();
        try {
//            client = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(
//                            new TransportAddress(InetAddress.getByName(esConfig.getHost()),esConfig.getPort()));
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress.getByName(esConfig.getHost()), esConfig.getPort()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * 获取所有索引
     *
     * @return
     */
    public static Set<String> getAllIndices() {
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
    public static boolean isExitIndice(String indexName) {
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
    public static boolean isExistsIndiceType(String indexName, String indexType) {
        TypesExistsResponse response = getClient()
                .admin()
                .indices()
                .typesExists(new TypesExistsRequest(new String[]{indexName}, new String[]{indexType}))
                .actionGet();
        LOGGER.info("The response json:{} " + GsonUtils.getGson().toJson(response));
        return response.isExists();
    }

    public static MetaData getMetaData(){
        MetaData metaData = ((ClusterStateResponse)getClient()
                .admin()
                .cluster()
                .prepareState()
                .execute()
                .actionGet())
                .getState()
                .getMetaData();
        return metaData;
    }


    public static TransportClient getClient() {
        CheckUtils.requireNotNull(client, "The client is null !");
        return client;
    }

    public static void destory() {
        CheckUtils.requireNotNull(client, "The client is null !");
        client.close();
    }

    public static void main(String[] args) {
        EsConfig esConfig = new EsConfig();
        esConfig.setHost("172.21.7.203");
        esConfig.setPort(9300);
        esConfig.setSniff(false);
        esConfig.setClusterName("es");
        ElasticsearchUtils.initClient(esConfig);
        //TransportClient client = elasticSearchUtils.getClient();
        Set set = ElasticsearchUtils.getAllIndices();
        JSONObject jsonObject = initJson();

        //System.out.println(((Map) ((List) jsonObject.get("sort")).get(0)).get("sortField"));
        SearchRequestBuilder sBuilder = null;
        ElasticsearchUtils.build(jsonObject, "compex_test2");


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
