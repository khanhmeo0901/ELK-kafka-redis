package com.example.demo.service;

import com.example.demo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {
    private final RestHighLevelClient elasticsearchClient;

    public ElasticsearchService(RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }
   public void queryDataFromELK(String index) throws IOException {
       SearchRequest searchRequest = new SearchRequest();
       searchRequest.indices(index);
       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       searchSourceBuilder.query(QueryBuilders.matchAllQuery());
       searchRequest.source(searchSourceBuilder);
       SearchResponse searchResponse = elasticsearchClient.search(searchRequest,RequestOptions.DEFAULT);
       if(searchResponse.getHits().getTotalHits().value > 0)
       {
           SearchHit[] searchHit = searchResponse.getHits().getHits();
           for (SearchHit hit : searchHit) {
             Map<String,Object>  map = hit.getSourceAsMap();
               System.out.println("map:" + Arrays.toString(map.entrySet().toArray()));
           }
       }

   }
   public void saveDataToELK(String index,User user)
   {
        Map<String, User> map = new HashMap<>();
        map.put(user.getId().toString(),user);
       List<Map.Entry<String,User>> dataList= map.entrySet().stream()
               .map(entry -> Map.entry(entry.getKey(),entry.getValue()))
               .collect(Collectors.toList());
       Gson gson = new GsonBuilder().setPrettyPrinting().create();
       String jsonData = gson.toJson(dataList);
       IndexRequest indexRequest = new IndexRequest(index).source(jsonData, XContentType.JSON);
   }


}
