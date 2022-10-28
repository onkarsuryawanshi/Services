package com.es;

import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;


    @GetMapping("/getAllDocument")
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        List<Object> deviceList = elasticSearchQuery.getAllDocuments();
        return new ResponseEntity<>(deviceList, HttpStatus.OK);
    }


    @GetMapping("/getDocument")
    public ResponseEntity<Object> getDocumentById(@RequestParam String _id) throws IOException {
        GetResponse<Object> response = elasticSearchQuery.readDocument(_id);
        if (response != null) {
            System.out.println(response);
            return new ResponseEntity<>("found document with entered Id ",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("entered id document does'nt exit" , HttpStatus.OK);
        }
    }

    @GetMapping("/getAllBN")
    public SearchResponse<Object> getAllBn(){
        SearchResponse<Object> response = elasticSearchQuery.getDocumentWithRadioTypeAsBn();
        System.out.println(response);
        return response;
    }


    @DeleteMapping("/deleteDocument")
    public ResponseEntity<Object> deleteByStudentId(@RequestParam String _id) throws IOException {
        String response = elasticSearchQuery.deleteDocumentById(_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/searchQuery")
    public ResponseEntity<Object> searchQuery(@RequestParam String text) throws IOException {
        List<Hit<Object>> hits = elasticSearchQuery.searchQuery(text);
        for (Hit<Object> hit : hits) {
            Object device = hit.source();
            return new ResponseEntity<>("Found found  " + device + ", score " + hit.score(), HttpStatus.OK);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @GetMapping("/range")
    public ResponseEntity<Object> rangeQuery(@RequestParam String stringMillis){
        JsonData millis = JsonData.fromJson(stringMillis);
        try {
            SearchResponse<Object>  response = elasticSearchQuery.getDocumentByRangeQuery(millis);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/daterange")
    public SearchResponse<Object> getDocumentByDateRange(@RequestParam String date){
            return elasticSearchQuery.getDocumentByDate(date);
    }
}
