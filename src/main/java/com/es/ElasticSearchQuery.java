package com.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.*;

@Repository
public class ElasticSearchQuery {

    private final String indexName = "student";
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private StudentData studentData;

    public String createDocument() {
        Student student = new Student("1", "arun", "Male", 88, 87, 78);
        IndexResponse response = null;
        try {
            response = elasticsearchClient
                    .index(i -> i
                            .index(indexName)
                            .id(student.getStudentId())
                            .document(student));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(response.version());

    }
    public BulkResponse bulkInsert() throws IOException {
        List<Student> studentList = studentData.getListOfStudent();
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Student student : studentList) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(student.getStudentId())
                            .document(student)
                    )
            );
        }
        BulkResponse result = null;
        try {
            result = elasticsearchClient.bulk(br.build());
        } catch (Exception e) {
            System.out.println(e + "error while adding ");
        }

        // Log errors, if any
        if (result.errors()) {
            System.out.println("Bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
        return result;
    }

    public Student readDocument(String studentId) throws IOException {
        GetResponse<Student> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(studentId),
                Student.class
        );
        Student student = null;
        if (response.found()) {
            student = response.source();
            System.out.println("Student id " + student.toString());
        } else {
            System.out.println("Student not found");
        }
        return student;
    }

    public String deleteDocumentById(String studentID) throws IOException {

        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(studentID));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("Student with id " + deleteResponse.id() + " has been deleted.").toString();
        }
        System.out.println("Student not found");
        return new StringBuilder("Student with id " + deleteResponse.id() + " does not exist.").toString();

    }

    public List<Student> searchAllDocuments() throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse<Student> searchResponse = elasticsearchClient.search(searchRequest, Student.class);
        List<Hit<Student>> hits = searchResponse.hits().hits();
        List<Student> students = new ArrayList<>();
        for (Hit<Student> object : hits) {

            System.out.print((object.source()));
            students.add(object.source());

        }
        return students;
    }

    public List<Hit<Student>> searchQuery(String studentName) throws IOException {
        String searchText = studentName;
        SearchResponse<Student> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .match(t -> t
                                        .field("studentName")
                                        .query(searchText)
                                )
                        ),
                Student.class
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<Student>> hits = response.hits().hits();
        for (Hit<Student> hit : hits) {
            Student student = hit.source();
            System.out.println("Found found  " + student.getStudentName() + ", score " + hit.score());
        }
        return hits;

    }

}
