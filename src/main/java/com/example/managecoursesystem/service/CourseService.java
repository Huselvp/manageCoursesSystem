package com.example.managecoursesystem.service;
import com.example.managecoursesystem.exception.ResourceNotFoundException;
import com.example.managecoursesystem.model.Course;
import com.example.managecoursesystem.model.Student;
import com.example.managecoursesystem.repository.CourseRepository;
import com.example.managecoursesystem.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> getAllCourses() {
        log.info("Fetching all courses");
        List<Course> courses = courseRepository.findAll();
        log.info("Retrieved {} courses", courses.size());
        return courses;
    }

    public Optional<Course> getCourseById(Long courseId) {
        log.info("Fetching course with ID: {}", courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            log.info("Found course: {}", course.get());
        } else {
            log.warn("Course not found with ID: {}", courseId);
        }
        return course;
    }

    public Course createCourse(Course course) {
        log.info("Creating new course: {}", course);
        Course createdCourse = courseRepository.save(course);
        log.info("Created course: {}", createdCourse);
        return createdCourse;
    }

    public Course updateCourse(Long courseId, Course courseDetails) {
        log.info("Updating course with ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found for this id :: {}", courseId);
                    return new ResourceNotFoundException("Course not found for this id :: " + courseId);
                });

        course.setCourseName(courseDetails.getCourseName());
        course.setDepartment(courseDetails.getDepartment());
        course.setCredits(courseDetails.getCredits());

        Course updatedCourse = courseRepository.save(course);
        log.info("Updated course: {}", updatedCourse);
        return updatedCourse;
    }

    public void deleteCourse(Long courseId) {
        log.info("Deleting course with ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found for this id :: {}", courseId);
                    return new ResourceNotFoundException("Course not found for this id :: " + courseId);
                });
        courseRepository.delete(course);
        log.info("Deleted course with ID: {}", courseId);
    }

    public Map<Course, List<Student>> getStudentsByCourse() {
        log.info("Fetching students by course");
        List<Course> courses = courseRepository.findAll();
        Map<Course, List<Student>> courseStudentMap = new HashMap<>();

        for (Course course : courses) {
            List<Student> students = studentRepository.findByCoursesContaining(course);
            courseStudentMap.put(course, students);
            log.info("Course: {}, Students enrolled: {}", course, students.size());
        }

        return courseStudentMap;
    }

    public Map<String, List<Student>> getStudentsByAgeGroup() {
        log.info("Grouping students by age group");
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

        log.info("Grouped students into age groups");
        return ageGroupMap;
    }

    public void enrollStudentsInCourse(List<Student> students, Long courseId) {
        log.info("Enrolling students in course with ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found with id {}", courseId);
                    return new ResourceNotFoundException("Course not found with id " + courseId);
                });

        for (Student student : students) {
            student.getCourses().add(course);
            studentRepository.save(student);
            log.info("Enrolled student: {} in course: {}", student, course);
        }
    }
}





