package org.example.courseregistration.repository;

import org.example.courseregistration.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    Student findByEmail(String email);
}
