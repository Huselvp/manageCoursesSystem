package com.example.managecoursesystem.service;
import com.example.managecoursesystem.exception.ResourceNotFoundException;
import com.example.managecoursesystem.model.Course;
import com.example.managecoursesystem.model.Student;
import com.example.managecoursesystem.repository.CourseRepository;
import com.example.managecoursesystem.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    public Course updateCourse(Long courseId, Course courseDetails) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found for this id :: " + courseId));

        course.setCourseName(courseDetails.getCourseName());
        course.setDepartment(courseDetails.getDepartment());
        course.setCredits(courseDetails.getCredits());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found for this id :: " + courseId));
        courseRepository.delete(course);
    }

    public Map<Course, List<Student>> getStudentsByCourse() {
        List<Course> courses = courseRepository.findAll();
        Map<Course, List<Student>> courseStudentMap = new HashMap<>();

        for (Course course : courses) {
            List<Student> students = studentRepository.findByCoursesContaining(course);
            courseStudentMap.put(course, students);
        }

        return courseStudentMap;
    }

    public Map<String, List<Student>> getStudentsByAgeGroup() {
        List<Student> students = studentRepository.findAll();
        Map<String, List<Student>> ageGroupMap = new HashMap<>();

        ageGroupMap.put("18-22", new ArrayList<>());
        ageGroupMap.put("23-27", new ArrayList<>());
        ageGroupMap.put("28-32", new ArrayList<>());
        ageGroupMap.put("33-37", new ArrayList<>());
        ageGroupMap.put("38+", new ArrayList<>());

        for (Student student : students) {
            int age = student.getAge();
            if (age >= 18 && age <= 22) {
                ageGroupMap.get("18-22").add(student);
            } else if (age >= 23 && age <= 27) {
                ageGroupMap.get("23-27").add(student);
            } else if (age >= 28 && age <= 32) {
                ageGroupMap.get("28-32").add(student);
            } else if (age >= 33 && age <= 37) {
                ageGroupMap.get("33-37").add(student);
            } else {
                ageGroupMap.get("38+").add(student);
            }
        }

        return ageGroupMap;
    }

    public void enrollStudentsInCourse(List<Student> students, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));

        for (Student student : students) {
            student.getCourseList().add(course);
            studentRepository.save(student);
        }
    }
}





