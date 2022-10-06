package com.es;

public class Student {
    private String studentId;
    private String studentName;
    private String studentGender;
    private Integer physics;
    private Integer maths;
    private Integer english;

    public Student() {}

    public Student(String studentId, String studentName, String studentGender, Integer physics, Integer maths, Integer english) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentGender = studentGender;
        this.physics = physics;
        this.maths = maths;
        this.english = english;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public Integer getPhysics() {
        return physics;
    }

    public void setPhysics(Integer physics) {
        this.physics = physics;
    }

    public Integer getMaths() {
        return maths;
    }

    public void setMaths(Integer maths) {
        this.maths = maths;
    }

    public Integer getEnglish() {
        return english;
    }

    public void setEnglish(Integer english) {
        this.english = english;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentGender='" + studentGender + '\'' +
                ", physics=" + physics +
                ", maths=" + maths +
                ", english=" + english +
                '}';
    }
}
