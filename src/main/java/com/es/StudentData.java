package com.es;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentData {
    public List<Student> getListOfStudent(){
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("1","rajesh","Male",96,100,95));
        studentList.add(new Student("2","onkar","Male",90,92,100));
        studentList.add(new Student("3","Niketan","Male",89,78,100));
                studentList.add(new Student("4","Rahul","Male",69,78,18));
        studentList.add(new Student("5","Rohit","Male",64,77,88));
        studentList.add(new Student("6","anket","Male",89,29,100));
        studentList.add(new Student("7","prasad","Male",78,18,78));
        studentList.add(new Student("8","Ganesh","Male",67,70,8));
        studentList.add(new Student("7","Yash","Male",80,89,78));
        return studentList;
    }
}
