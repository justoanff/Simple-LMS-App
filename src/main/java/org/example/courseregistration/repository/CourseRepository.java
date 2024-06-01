package org.example.courseregistration.repository;

import org.example.courseregistration.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {


    Course findByName(String name);
}