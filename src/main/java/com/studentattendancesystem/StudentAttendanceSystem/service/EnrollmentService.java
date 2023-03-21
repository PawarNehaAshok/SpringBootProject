package com.studentattendancesystem.StudentAttendanceSystem.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EnrollmentService {

    ResponseEntity<Object> enrollStudent(int studentId, int teacherId);
}
