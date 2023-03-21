package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.studentattendancesystem.StudentAttendanceSystem.service.EnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    EnrollmentService enrollmentService;

    @PostMapping("enroll-student/{studentId}/{teacherId}")
    public ResponseEntity<Object> enrollStudent(@PathVariable int studentId, @PathVariable int teacherId) throws Exception {
        log.info("student with id " + studentId + " and teacher with id " + teacherId + " is getting enroll.");
        ResponseEntity<Object> response = enrollmentService.enrollStudent(studentId, teacherId);
        return response;
    }
}
