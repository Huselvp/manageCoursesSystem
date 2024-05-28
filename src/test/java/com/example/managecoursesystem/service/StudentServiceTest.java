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
        mockStudents = new ArrayList<>();
        mockStudents.add(new Student(1L, "Sanae", "Sss", 25, new Date(), "sanae.sss@example.com"));
        mockStudents.add(new Student(2L, "Jinan", "Jjj", 30, new Date(), "Jinane.Jjj@example.com"));
        when(studentRepository.findAll()).thenReturn(mockStudents);
        List<Student> students = studentService.getAllStudents();

        assertEquals(2, students.size());
        assertEquals("Sanae", students.get(0).getFirstName());
        assertEquals("Jinan", students.get(1).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById() {
        Student mockStudent = new Student(1L, "Houda", "Msb", 25, new Date(), "Houda.Msb@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));

        Optional<Student> student = studentService.getStudentById(1L);

        assertTrue(student.isPresent());
        assertEquals("Houda", student.get().getFirstName());

        verify(studentRepository, times(1)).findById(1L);
    }


    @Test
    void testCreateStudent() {
        Student newStudent = new Student(null, "Houda", "Msb", 28, new Date(), "Houda.Msb@example.com");
        Student savedStudent = new Student(1L, "Houda", "Msb", 28, new Date(), "Houda.Msb@example.com");
        when(studentRepository.save(newStudent)).thenReturn(savedStudent);
        Student createdStudent = studentService.createStudent(newStudent);
        assertNotNull(createdStudent.getStudentId());
        assertEquals("Houda", createdStudent.getFirstName());

        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void testUpdateStudent() {
        Student existingStudent = new Student(1L, "Houda", "Msb", 25, new Date(), "Houda.msb@example.com");
        Student updatedDetails = new Student(null, "Jinan", "Jjj", 30, new Date(), "Jinan.Jjj@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(updatedDetails);

        Student updatedStudent = studentService.updateStudent(1L, updatedDetails);

        assertEquals("Jinan", updatedStudent.getFirstName());

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void testUpdateStudent_NotFound() {
        Student updatedDetails = new Student(null, "Jinan", "Jjj", 30, new Date(), "Jinan.Jjj@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(1L, updatedDetails));

        verify(studentRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testDeleteStudent() {
        Student existingStudent = new Student(1L, "Houda", "Msb", 25, new Date(), "john.doe@example.com");

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