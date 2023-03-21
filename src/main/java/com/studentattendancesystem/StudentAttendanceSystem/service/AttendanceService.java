package com.studentattendancesystem.StudentAttendanceSystem.service;

import com.studentattendancesystem.StudentAttendanceSystem.dto.AttendanceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AttendanceService {

    ResponseEntity<Object> markAttendance(AttendanceDto attendances, int studentId, int teacherId);

    ResponseEntity<Object> getAttendanceDetailsById(int studentId);
}
