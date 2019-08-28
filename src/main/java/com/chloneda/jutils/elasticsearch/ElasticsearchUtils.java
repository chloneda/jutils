package com.chloneda.jutils.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.chloneda.jutils.commons.CheckUtils;
import com.chloneda.jutils.json.GsonUtils;
import org.elasticsearch.action.ActionFuture;
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
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

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
        createClient(esConfig);
    }

    public static TransportClient createClient(EsConfig esConfig) {
        // 连接集群的设置
        Settings settings = Settings.builder()
                .put("cluster.name", esConfig.getClusterName()!=null?esConfig.getClusterName():EsConfig.DEFAULT_ES_CLUSTER_NAME)
                .put("client.transport.sniff", esConfig.isSniff())
                .build();
        try {
//            client = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(
//                            new TransportAddress(InetAddress.getByName(esConfig.getHost()),esConfig.getPort()));
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress.getByName(esConfig.getHost()),esConfig.getPort()));
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
        ElasticsearchUtils.createClient(esConfig);
        //TransportClient client = elasticSearchUtils.getClient();
        Set set = ElasticsearchUtils.getAllIndices();
        System.out.println(initJson());
        //elasticSearchUtils.groupby();


    }

    public static JSONObject initJson(){
        JSONObject jsonObj=new JSONObject();

        //聚合分组
        List groupList=new ArrayList<Map>();
        HashMap group=new HashMap();
        group.put("groupField","username");
        group.put("groupFilter","用户名");
        group.put("function","min");
        group.put("sortType","desc");
        group.put("size","33");
        groupList.add(group);
        jsonObj.put("group",groupList);

        //条件
        List counterList=new ArrayList<Map>();
        HashMap counter=new HashMap();
        counter.put("countField","username");
        counter.put("countAlias","用户名");
        counter.put("isCount","true");
        counterList.add(counter);
        jsonObj.put("counter",counterList);

        //排序
        List sortList=new ArrayList<Map>();
        HashMap sort=new HashMap();
        sort.put("sortField","age");
        sort.put("sortType","ase");
        sortList.add(sort);
        jsonObj.put("sort",sortList);

        //条件
        List filtersList=new ArrayList<Map>();
        HashMap filters=new HashMap();
        filters.put("filterField","age");
        filters.put("filterFun","=");
        filters.put("filterValue","18");
        filtersList.add(filters);
        jsonObj.put("filters",filtersList);

        //Limit
        HashMap limit=new HashMap();
        limit.put("from",0);
        limit.put("to",10);
        jsonObj.put("limit",limit);

        //统计

        return jsonObj;
    }


    //聚合分组
    public void groupby(JSONObject jsonObject,String... indices){
//        GetResponse gresponse = client.prepareGet("dns_log", "dns_log", "IRj_PGwBFyOY7mH_-5cK").get();
//        System.out.println(gresponse.getFields());
        SearchRequestBuilder sBuilder = client.prepareSearch("dns_log")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
//                .setQuery(QueryBuilders.termQuery("DVC_A", "192.167.4.107"))                 // Query
//                //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(20))     // Filter
//                .setFrom(0).setSize(60).setExplain(true)
//                .get();
        SearchRequestBuilder searchBuilder=getClient().prepareSearch(indices);
        //searchBuilder.addAggregation()
        AggregationBuilder aggregation=null;
        aggregation = AggregationBuilders.min("agg").field("age");
        aggregation=AggregationBuilders.max("agg").field("name");
        aggregation = AggregationBuilders.sum("agg").field("readSize");
        aggregation = AggregationBuilders.avg("agg").field("age");
        aggregation = AggregationBuilders.stats("agg").field("age");

        SearchResponse sr = getClient().prepareSearch("twitter").addAggregation(aggregation).get();
        Min agg = sr.getAggregations().get("agg");

        System.out.println(sBuilder.toString());
    }

    //条件
    public void filters(){

    }

    //统计
    public void counter(){

    }

    //排序
    public void sort(){

    }

    //Limit
//    public SearchRequestBuilder limit(String... indices){
//        SearchRequestBuilder searchBuilder=getClient().prepareSearch(indices);
//        searchBuilder.addAggregation();
//    }


}
