package com.es;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.elasticsearch.action.get.GetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;

    @PostMapping("/createDocument")
    public ResponseEntity<Object> createOrUpdateDocument() throws IOException {
        elasticSearchQuery.createDocument();
        return new ResponseEntity<>("created", HttpStatus.OK);
    }

    @PostMapping("/addBulkData")
    public ResponseEntity<Object> addBulkData() throws IOException {
        BulkResponse student = elasticSearchQuery.bulkInsert();
        return new ResponseEntity<>("added", HttpStatus.OK);
    }

    @GetMapping("/getDocument")
    public ResponseEntity<Object> getDocumentById(@RequestParam String studentId) throws IOException {
        Student student = elasticSearchQuery.readDocument(studentId);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    /*
    In above function if studentId is put in request param
    output--
    {
    "studentId": "1",
    "studentName": "rajesh",
    "studentGender": "Male",
    "physics": 96,
    "maths": 100,
    "english": 95
}
     */

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<Object> deleteByStudentId(@RequestParam String studentId) throws IOException {
        String response = elasticSearchQuery.deleteDocumentById(studentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllDocument")
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        List<Student> students = elasticSearchQuery.searchAllDocuments();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }


    @GetMapping("/searchQuery")
    public ResponseEntity<Object> searchQuery(@RequestParam String studentName) throws IOException {
        List<Hit<Student>> hits = elasticSearchQuery.searchQuery(studentName);
        for (Hit<Student> hit : hits) {
            Student student = hit.source();
            return new ResponseEntity<>("Found found  " + student.getStudentName() + ", score " + hit.score(), HttpStatus.OK);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /*
    output--
    Found found  onkar, score 1.3862942
    * */


}
