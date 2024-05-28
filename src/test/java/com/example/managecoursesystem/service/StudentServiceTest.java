package com.example.managecoursesystem.service;

import com.example.managecoursesystem.exception.ResourceNotFoundException;
import com.example.managecoursesystem.model.Student;
import com.example.managecoursesystem.repository.StudentRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        List<Student> mockStudents = new ArrayList<>();
        mockStudents.add(new Student(1L, "John", "Doe", 25, new Date(), "john.doe@example.com"));
        mockStudents.add(new Student(2L, "Jane", "Smith", 30, new Date(), "jane.smith@example.com"));

        when(studentRepository.findAll()).thenReturn(mockStudents);

        List<Student> students = studentService.getAllStudents();

        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getFirstName());
        assertEquals("Jane", students.get(1).getFirstName());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById() {
        Student mockStudent = new Student(1L, "John", "Doe", 25, new Date(), "john.doe@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));

        Optional<Student> student = studentService.getStudentById(1L);

        assertTrue(student.isPresent());
        assertEquals("John", student.get().getFirstName());

        verify(studentRepository, times(1)).findById(1L);
    }


    @Test
    void testCreateStudent() {
        Student newStudent = new Student(null, "Alice", "Johnson", 28, new Date(), "alice.johnson@example.com");
        Student savedStudent = new Student(1L, "Alice", "Johnson", 28, new Date(), "alice.johnson@example.com");

        when(studentRepository.save(newStudent)).thenReturn(savedStudent);

        Student createdStudent = studentService.createStudent(newStudent);

        assertNotNull(createdStudent.getStudentId());
        assertEquals("Alice", createdStudent.getFirstName());

        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void testUpdateStudent() {
        Student existingStudent = new Student(1L, "John", "Doe", 25, new Date(), "john.doe@example.com");
        Student updatedDetails = new Student(null, "Jane", "Smith", 30, new Date(), "jane.smith@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(updatedDetails);

        Student updatedStudent = studentService.updateStudent(1L, updatedDetails);

        assertEquals("Jane", updatedStudent.getFirstName());

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void testUpdateStudent_NotFound() {
        Student updatedDetails = new Student(null, "Jane", "Smith", 30, new Date(), "jane.smith@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(1L, updatedDetails));

        verify(studentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testDeleteStudent() {
        Student existingStudent = new Student(1L, "John", "Doe", 25, new Date(), "john.doe@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(existingStudent);
    }

    @Test
    void testDeleteStudent_NotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));

        verify(studentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(studentRepository);
    }
}