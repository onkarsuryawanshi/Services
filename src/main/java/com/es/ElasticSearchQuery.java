package com.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.*;

import static com.es.config.ElasticSearchConfig.elasticSearchIndexName;

@Repository
public class ElasticSearchQuery {

    private final String indexName = elasticSearchIndexName;
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<Object> getAllDocuments() throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse<Object> searchResponse = elasticsearchClient.search(searchRequest, Object.class);
        List<Hit<Object>> hits = searchResponse.hits().hits();
        List<Object> deviceList = new ArrayList<>();
        for (Hit<Object> object : hits) {

            System.out.print((object.source()));
            deviceList.add(object.source());

        }
        return deviceList;
    }


    public GetResponse<Object> readDocument(String Id) throws IOException {
        GetResponse<Object> response = elasticsearchClient.get(g->g
                .index(indexName).id(Id)
                ,Object.class
        );
//        System.out.println(response);
        return response;
    }


    /*
    if you provide the correct id of the device present in the elastic search then that document will be deleted if its exits
    * */
    public String deleteDocumentById(String ID) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(ID));
        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("device  with id " + deleteResponse.id() + " has been deleted.").toString();
        }
//        System.out.println("device not found");
        return new StringBuilder("device with id " + deleteResponse.id() + " does not exist.").toString();

    }



    public List<Hit<Object>> searchQuery(String text) throws IOException {
        String searchText = text;
        SearchResponse<Object> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .match(t -> t
                                        .field("bn-id.keyword")
                                        .query(searchText)
                                )
                        ),
                Object.class
        );
        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<Object>> hits = response.hits().hits();
        for (Hit<Object> hit : hits) {
            Object device = hit.source();
            System.out.println("Found found  " + device.toString()+ ", score " + hit.score());
        }
        return hits;

    }

    public SearchResponse<Object> getDocumentByRangeQuery(JsonData millis) throws IOException {
        SearchResponse<Object> response =
        elasticsearchClient.search(s->s
                .index(indexName)
                        .query(q->q
                                .range(r->r
                                        .field("writeTime")
                                        .gte(millis)
                                ))
        ,Object.class);

        System.out.println(response);
        return response;
    }

    public SearchResponse<Object> getDocumentWithRadioTypeAsBn() {
        try {
            SearchResponse<Object> response = elasticsearchClient.search(s->s
                    .index(indexName)
                            .query(q->q
                                    .match(m->m
                                            .field("radioType")
                                            .query("BN"))
                            )
             ,Object.class);

            System.out.println(response);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public SearchResponse<Object> getDocumentByDate(String date) {

        try {
            SearchResponse<Object> response = elasticsearchClient.search(s->s
                            .index(indexName)
                            .query(q->q
                                    .match(m->m
                                            .field("date")
                                            .query(date))
                            )
                    ,Object.class);

            System.out.println(response);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
