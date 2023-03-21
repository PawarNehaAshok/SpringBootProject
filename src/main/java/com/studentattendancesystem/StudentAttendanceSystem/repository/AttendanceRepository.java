package com.studentattendancesystem.StudentAttendanceSystem.repository;

import com.studentattendancesystem.StudentAttendanceSystem.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(value = "select a.* from attendance a where a.student_id = ?1", nativeQuery = true)
    Attendance findAttendanceByStudentId(int studentId);
}
