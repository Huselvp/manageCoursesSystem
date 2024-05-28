package com.example.managecoursesystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.example.managecoursesystem.exception.ResourceNotFoundException;
import com.example.managecoursesystem.model.Course;
import com.example.managecoursesystem.model.Student;
import com.example.managecoursesystem.repository.CourseRepository;
import com.example.managecoursesystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

 class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Student student1;
    private Student student2;
    private List<Student> students;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student1 = new Student(1L, "John", "Doe", 20, new Date(), "john.doe@example.com");
        student2 = new Student(2L, "Jane", "Smith", 21, new Date(), "jane.smith@example.com");
        course = new Course(1L, "Course 1", "Dept 1", 3);
        students = Arrays.asList(student1, student2);
    }

    @Test
    void testGetAllCourses() {
        Course course2 = new Course(2L, "Course 2", "Dept 2", 4);
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course, course2));

        List<Course> courses = courseService.getAllCourses();
        assertEquals(2, courses.size());
        assertEquals("Course 1", courses.get(0).getCourseName());
        assertEquals("Course 2", courses.get(1).getCourseName());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCourseById_courseFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Optional<Course> result = courseService.getCourseById(1L);
        assertTrue(result.isPresent());
        assertEquals("Course 1", result.get().getCourseName());

        verify(courseRepository, times(1)).findById(1L);
    }
    @Test
    void testGetCourseById_courseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Course> result = courseService.getCourseById(1L);
        assertFalse(result.isPresent());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course createdCourse = courseService.createCourse(course);
        assertEquals("Course 1", createdCourse.getCourseName());

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateCourse() {
        Course updatedDetails = new Course(1L, "Updated Course", "Updated Dept", 4);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedDetails);

        Course updatedCourse = courseService.updateCourse(1L, updatedDetails);
        assertEquals("Updated Course", updatedCourse.getCourseName());
        assertEquals("Updated Dept", updatedCourse.getDepartment());
        assertEquals(4, updatedCourse.getCredits());

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
     @Test
     void testUpdateCourse_NotFound() {
         Course updatedDetails = new Course(1L, "Updated Course", "Updated Dept", 4);
         when(courseRepository.findById(1L)).thenReturn(Optional.empty());
         Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
             courseService.updateCourse(1L, updatedDetails);
         });
         String expectedMessage = "Course not found for this id :: 1";
         String actualMessage = exception.getMessage();
         assertTrue(actualMessage.contains(expectedMessage));
         verify(courseRepository, times(1)).findById(1L);
         verify(courseRepository, never()).save(any(Course.class));
     }

    @Test
    void testDeleteCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        courseService.deleteCourse(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course);
    }
     @Test
     void testDeleteCourse_NotFound() {
         when(courseRepository.findById(1L)).thenReturn(Optional.empty());

         Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
             courseService.deleteCourse(1L);
         });
         String expectedMessage = "Course not found for this id :: 1";
         String actualMessage = exception.getMessage();

         assertTrue(actualMessage.contains(expectedMessage));
         verify(courseRepository, times(1)).findById(1L);
         verify(courseRepository, never()).delete(any(Course.class));
     }

    @Test
    void testGetStudentsByCourse() {
        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));
        when(studentRepository.findByCoursesContaining(course)).thenReturn(students);

        Map<Course, List<Student>> result = courseService.getStudentsByCourse();
        assertEquals(1, result.size());
        assertTrue(result.containsKey(course));
        assertEquals(2, result.get(course).size());

        verify(courseRepository, times(1)).findAll();
        verify(studentRepository, times(1)).findByCoursesContaining(course);
    }

    @Test
    void testEnrollStudentsInCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.enrollStudentsInCourse(students, 1L);

        for (Student student : students) {
            assertTrue(student.getCourses().contains(course));
        }

        verify(courseRepository, times(1)).findById(1L);
        verify(studentRepository, times(students.size())).save(any(Student.class));
    }
     @Test
     void testEnrollStudentsInCourse_NotFound() {
         when(courseRepository.findById(1L)).thenReturn(Optional.empty());

         Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
             courseService.enrollStudentsInCourse(students, 1L);
         });
         String expectedMessage = "Course not found with id 1";
         String actualMessage = exception.getMessage();
         assertTrue(actualMessage.contains(expectedMessage));
         verify(courseRepository, times(1)).findById(1L);
         verify(studentRepository, never()).save(any(Student.class));
     }
     @Test
     void testGetStudentsByAgeGroup_18_22() {
         List<Student> students = List.of(
                 new Student(1L, "Narjis", "Nnn", 20, new Date(), "Narjis.Nnn@example.com")
         );

         when(studentRepository.findAll()).thenReturn(students);

         Map<String, List<Student>> ageGroupMap = courseService.getStudentsByAgeGroup();
         assertEquals(1, ageGroupMap.get("18-22").size());
         assertEquals("Narjis", ageGroupMap.get("18-22").get(0).getFirstName());
         verify(studentRepository, times(1)).findAll();
     }

     @Test
     void testGetStudentsByAgeGroup_23_27() {
         List<Student> students = List.of(
                 new Student(2L, "Jinane", "Jjj", 25, new Date(), "jinane.Jjj@example.com")
         );

         when(studentRepository.findAll()).thenReturn(students);

         Map<String, List<Student>> ageGroupMap = courseService.getStudentsByAgeGroup();

         assertEquals(1, ageGroupMap.get("23-27").size());
         assertEquals("Jinane", ageGroupMap.get("23-27").get(0).getFirstName());

         verify(studentRepository, times(1)).findAll();
     }

     @Test
     void testGetStudentsByAgeGroup_28_32() {
         List<Student> students = List.of(
                 new Student(3L, "Sanae", "Sss", 30, new Date(), "sanae.Sss@example.com")
         );

         when(studentRepository.findAll()).thenReturn(students);

         Map<String, List<Student>> ageGroupMap = courseService.getStudentsByAgeGroup();

         assertEquals(1, ageGroupMap.get("28-32").size());
         assertEquals("Sanae", ageGroupMap.get("28-32").get(0).getFirstName());

         verify(studentRepository, times(1)).findAll();
     }

     @Test
     void testGetStudentsByAgeGroup_33_37() {
         List<Student> students = List.of(
                 new Student(4L, "hajar", "Aaa", 35, new Date(), "hajar.Aaa@example.com")
         );

         when(studentRepository.findAll()).thenReturn(students);

         Map<String, List<Student>> ageGroupMap = courseService.getStudentsByAgeGroup();

         assertEquals(1, ageGroupMap.get("33-37").size());
         assertEquals("hajar", ageGroupMap.get("33-37").get(0).getFirstName());

         verify(studentRepository, times(1)).findAll();
     }

     @Test
     void testGetStudentsByAgeGroup_38() {
         List<Student> students = List.of(
                 new Student(5L, "Houda", "Msb", 40, new Date(), "Houda.msb@example.com")
         );
         when(studentRepository.findAll()).thenReturn(students);

         Map<String, List<Student>> ageGroupMap = courseService.getStudentsByAgeGroup();

         assertEquals(1, ageGroupMap.get("38+").size());
         assertEquals("Houda", ageGroupMap.get("38+").get(0).getFirstName());

         verify(studentRepository, times(1)).findAll();
     }
}
