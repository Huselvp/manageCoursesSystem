package com.example.manageCourseSystem.service;

import com.example.manageCourseSystem.exceptions.ResourceNotFoundException;
import com.example.manageCourseSystem.model.Course;
import com.example.manageCourseSystem.model.Student;
import com.example.manageCourseSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId,Student studentDetails) {
       Student student= studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Course not found for this id :: " +studentId));

        student.setFirstName(studentDetails.getFirstName());
      student.setLastName(studentDetails.getLastName());
       student.setAge(studentDetails.getAge());
       student.setEmail(studentDetails.getEmail());
       student.setDateOfBirth(studentDetails.getDateOfBirth());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
      Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Course not found for this id :: " + studentId));
        studentRepository.delete(student);
    }

}
