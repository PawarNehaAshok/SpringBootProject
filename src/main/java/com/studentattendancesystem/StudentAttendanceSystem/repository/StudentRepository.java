package com.studentattendancesystem.StudentAttendanceSystem.repository;

import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByEmailId(String emailId);

    @Query(value = "SELECT t.id as id, t. first_name as firstName, t.department as department, t.gender as gender FROM teacher t JOIN student_teacher st ON t.id = st.teacher_id WHERE st.student_id = ?1", nativeQuery = true)
    List<TeacherInterface> findTeachersByStudentId(int studentId);

    Student findStudentById(int id);
}
