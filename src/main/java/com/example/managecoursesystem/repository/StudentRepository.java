package com.example.managecoursesystem.repository;

import com.example.managecoursesystem.model.Course;
import com.example.managecoursesystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByCoursesContaining(Course course);
}
