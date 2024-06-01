package org.example.courseregistration.controller;


import jakarta.validation.Valid;
import org.example.courseregistration.entity.Course;
import org.example.courseregistration.entity.Student;
import org.example.courseregistration.repository.CourseRepository;
import org.example.courseregistration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping("/students")
    public String index(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students";
    }

    @RequestMapping("student/new")
    public String newStudent(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("student", new Student());
        return "studentForm";
    }

    @RequestMapping("student/edit/{id}")
    public String editStudent(@PathVariable("id") Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return "error"; // Handle student not found
        }
        student.getCourses().size(); // Eagerly fetch courses
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("student", student);
        model.addAttribute("courses", courses);
        return "studentForm";
    }


    @RequestMapping("student/{id}")
    public String showStudent(@PathVariable("id") Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        model.addAttribute("student", student);
        assert student != null;
        model.addAttribute("courses", student.getCourses());
        return "studentShow";
    }

    @RequestMapping(value = "saveStudent", method = RequestMethod.POST)
    public String saveStudent(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult, @RequestParam(required = false) Long id, Model model) {
        if (bindingResult.hasErrors()) {
            String title = (id == null) ? "Add Student" : "Edit Student";
            model.addAttribute("title", title);
            return "studentForm";
        }

        // Check if email already exists in the database
        Student existingStudent = studentRepository.findByEmail(student.getEmail());
        if (existingStudent != null && (id == null || !existingStudent.getId().equals(id))) {
            bindingResult.rejectValue("email", "duplicate", "Email is already in use");
            model.addAttribute("title", (id == null) ? "Add Student" : "Edit Student");
            return "studentForm";
        }

        // Fetch the existing student if updating
        Student existing = null;
        if (id != null) {
            existing = studentRepository.findById(id).orElse(null);
        }

        // Merge the courses from the existing student to the updated student
        if (existing != null) {
            student.setCourses(existing.getCourses());
        }

        studentRepository.save(student);
        return "redirect:/students";
    }



    @RequestMapping("student/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, Model model) {
        try {
            studentRepository.deleteById(id);
            return "redirect:/students";

        } catch (DataIntegrityViolationException e) {
            return "error";
        }
    }

    @RequestMapping("addStudentCourse/{id}")
    public String addStudentCourse(@PathVariable("id") Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        model.addAttribute("student", student);
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "addStudentCourse";
    }

    @RequestMapping("student/{id}/courses")
    public String studentsAddCourse(@RequestParam(value = "action", required = true) String action, @PathVariable Long id, @RequestParam Long courseId, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);
        if (student == null || course == null) {
            return "error";
        }
        course.addStudent(student);
        courseRepository.save(course);
        studentRepository.save(student);
        model.addAttribute("student", student);
        model.addAttribute("courses", courseRepository.findAll());
        return "redirect:/students";
    }


}
