package com.example.managecoursesystem.service;

import com.example.managecoursesystem.exception.ResourceNotFoundException;
import com.example.managecoursesystem.model.Student;
import com.example.managecoursesystem.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

   StudentService(StudentRepository studentRepository){
    this.studentRepository = studentRepository;
}

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        logger.info("Fetching all students");
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long studentId) {
        logger.info("Fetching student with ID: {}", studentId);
        return studentRepository.findById(studentId);
    }

    public Student createStudent(Student student) {
        logger.info("Creating new student: {}", student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, Student studentDetails) {
        logger.info("Updating student with ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + studentId));

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setAge(studentDetails.getAge());
        student.setEmail(studentDetails.getEmail());
        student.setDateOfBirth(studentDetails.getDateOfBirth());

        logger.info("Updated student details: {}", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        logger.info("Deleting student with ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + studentId));
        studentRepository.delete(student);
        logger.info("Deleted student with ID: {}", studentId);
    }
}
