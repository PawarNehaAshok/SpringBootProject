package com.studentattendancesystem.StudentAttendanceSystem.repository;

import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findByFirstName(String firstName);
}
