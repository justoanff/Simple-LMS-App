package org.example.courseregistration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "student")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "First Name is Required")
    @Column(name = "firstname")
    private String firstName;

    @NotEmpty(message = "Last Name is Required")
    @Column(name = "lastname")
    private String lastName;

    @NotEmpty(message = "Department is Required")
    @Column(name = "department")
    private String department;

    @NotEmpty
    @Email(message = "Invalid Email")
    @Column(name = "email")
    private String email;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "courseid"))
    private List<Course> courses;

    public void addCourse(Course theCourse) {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        courses.add(theCourse);
    }

    public boolean hasCourse(Course course) {
        for (Course studentCourse : getCourses()) {
            if (Objects.equals(studentCourse.getCourseId(), course.getCourseId())) {
                return true;
            }
        }
        return false;
    }
}
