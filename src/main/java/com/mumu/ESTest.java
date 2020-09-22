package com.mumu;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class ESTest {
    private TransportClient transportClient;
    @Before
    public void before()throws Exception{
        Settings settings = Settings.builder().put("cluster.name","cluster-es").build();;
        transportClient = new PreBuiltTransportClient(settings);
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
    }

    @Test
    public void createIndex() throws  Exception{

        //4: 使用client对象完成索引库的创建
        CreateIndexResponse response =  transportClient.admin().indices().prepareCreate("blog2").get();
        System.out.println(response.index());
        System.out.println(transportClient);
        //5:关闭
        transportClient.close();
    }

    @Test
    public void createIndexFile() throws  Exception{
        //1：创建客户端
        //2：创建document
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject()
                .field("id",2)
                .field("title","elasticsearch是一个基于lucene的搜索服务")
                .field("content","ElasticSearch是一个基于Lucene的搜索服务器。" +
                        "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。" +
                        "Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，" +
                        "是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，" +
                        "可靠，快速，安装使用方便。")
                .endObject();
        //3：创建index
        transportClient.prepareIndex("blog2", "article", "2").setSource(contentBuilder).get();
        //4：关闭资源
        transportClient.close();
    }

    // 2：创建规则
    @Test
    public void createMapping() throws  Exception{
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate("blog3").get();
        if(response.isAcknowledged()) {
            // 1: 建立json规则
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject()
                    .startObject("content")
                    .startObject("properties")
                    .startObject("id").field("type", "long").field("store", true).endObject()
                    // 细粒度切分使用 ik_max_word  最小切分 ik_smart
                    .startObject("title").field("type", "text").field("store", true).field("analyzer", "ik_smart").endObject()
                    .startObject("content").field("type", "text").field("store", true).field("analyzer", "ik_smart").endObject()
                    .endObject()
                    .endObject()
                    .endObject();

//            Map<String,Object> map = new HashMap<String, Object>();
//            Map<String,Object> propertiesMap = new HashMap<String, Object>();
//            Map<String,Object> properties = new HashMap<String, Object>();
//
//
//            Map<String,Object> idMap = new HashMap<String, Object>();
//            idMap.put("type","long");
//            idMap.put("store",true);
//            properties.put("id",idMap);
//
//            Map<String,Object> titleMap = new HashMap<String, Object>();
//            titleMap.put("type","text");
//            titleMap.put("analyzer","ik_smart");
//            titleMap.put("store",true);
//            properties.put("title",titleMap);
//
//            Map<String,Object> contentMap = new HashMap<String, Object>();
//            contentMap.put("type","text");
//            contentMap.put("analyzer","ik_smart");
//            contentMap.put("store",true);
//            properties.put("content",contentMap);
//
//
//            propertiesMap.put("properties",properties);
//
//            map.put("content",propertiesMap);
//
//            // 2: 把pojo转换成json格式
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonString =  objectMapper.writeValueAsString(map);
//            System.out.println(jsonString);

            // 执行规则 报错，创建规则不会创建索引库和类型，就报错。
            PutMappingResponse putMappingResponse = transportClient.admin().indices().preparePutMapping("blog3").setType("content").setSource(contentBuilder).get();
            System.out.println(putMappingResponse.isAcknowledged());

            // 关闭
            transportClient.close();
        }

    }

}
