package com.example.managecoursesystem.controller;

import com.example.managecoursesystem.exception.ResourceNotFoundException;
import com.example.managecoursesystem.model.Course;
import com.example.managecoursesystem.model.Student;
import com.example.managecoursesystem.service.CourseService;
import com.example.managecoursesystem.service.StudentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    StudentService studentService;
    CourseService courseService;
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

    @PutMapping("update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long studentId, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(studentId,studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable(value = "id") Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/by-course")
    public ResponseEntity<Map<Course, List<Student>>> getStudentsByCourse() {
        Map<Course, List<Student>> studentsByCourse = courseService.getStudentsByCourse();
        return ResponseEntity.ok(studentsByCourse);
    }

    @GetMapping("/by-age-group")
    public ResponseEntity<Map<String, List<Student>>> getStudentsByAgeGroup() {
        Map<String, List<Student>> studentsByAgeGroup = courseService.getStudentsByAgeGroup();
        return ResponseEntity.ok(studentsByAgeGroup);
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollStudentsInCourse(@RequestParam("file") MultipartFile file, @RequestParam("courseId") Long courseId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Student> students = objectMapper.readValue(file.getInputStream(), new TypeReference<List<Student>>() {});
            courseService.enrollStudentsInCourse(students, courseId);
            return ResponseEntity.ok("Students enrolled successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing the file");
        }
    }
}
