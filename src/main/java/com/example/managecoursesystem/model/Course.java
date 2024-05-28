package com.example.managecoursesystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String    courseName;
    private String   department;
    private  int credits;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Course(Long id, String courseName, String department, int credits) {
        this.courseId = id;
        this.courseName = courseName;
        this.department = department;
        this.credits = credits;
    }
    public Course() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
