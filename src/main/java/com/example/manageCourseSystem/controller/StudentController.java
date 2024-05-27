package com.example.manageCourseSystem.controller;

import com.example.manageCourseSystem.exceptions.ResourceNotFoundException;
import com.example.manageCourseSystem.model.Course;
import com.example.manageCourseSystem.model.Student;
import com.example.manageCourseSystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    StudentService studentService;
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long studentId) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found for this id :: " + studentId));
        return ResponseEntity.ok().body(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long studentId, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(studentId,studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable(value = "id") Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}
