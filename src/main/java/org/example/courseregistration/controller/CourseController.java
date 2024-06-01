package org.example.courseregistration.controller;


import jakarta.validation.Valid;
import org.example.courseregistration.entity.Course;
import org.example.courseregistration.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping("courses")
    public String index(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @RequestMapping(value = "course/new")
    public String newCourse(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("course", new Course());
        model.addAttribute("title", "Add Course");
        return "courseForm";
    }

    @RequestMapping(value = "course/edit/{id}")
    public String editCourse(@PathVariable("id") Long courseId, Model model) {
        Course course = courseRepository.findById(courseId).orElse(null);
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("course", course);
        model.addAttribute("title", "Edit Course");
        return "courseForm";
    }

    @RequestMapping("course/{id}")
    public String showCourse(@PathVariable("id") Long courseId, Model model) {
        Course course = courseRepository.findById(courseId).orElse(null);
        model.addAttribute("course", course);
        model.addAttribute("title", "Show Course");
        return "courseShow";
    }

    @RequestMapping(value = "saveCourse", method = RequestMethod.POST)
    public String saveCourse(@Valid @ModelAttribute("course") Course course, BindingResult bindingResult, @RequestParam(required = false) Long courseId, Model model) {
        if (bindingResult.hasErrors()) {
            String title = (courseId == null) ? "Add Course" : "Edit Course";
            model.addAttribute("title", title);
            return "courseForm";
        }

        // Check if course with the same name already exists in the database
        Course existingCourse = courseRepository.findByName(course.getName());
        if (existingCourse != null && (courseId == null || !existingCourse.getCourseId().equals(courseId))) {
            bindingResult.rejectValue("name", "duplicate", "Course with the same name already exists");
            model.addAttribute("title", (courseId == null) ? "Add Course" : "Edit Course");
            return "courseForm";
        }

        courseRepository.save(course);
        return "redirect:/courses";
    }


    @RequestMapping(value = "course/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long courseId, Model model) {
        try {
            courseRepository.deleteById(courseId);
            return "redirect:/courses";
        } catch (DataIntegrityViolationException e) {
            return "error";
        }
    }

}
