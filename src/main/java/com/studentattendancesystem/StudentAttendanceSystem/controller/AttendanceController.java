package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.studentattendancesystem.StudentAttendanceSystem.dto.AttendanceDto;
import com.studentattendancesystem.StudentAttendanceSystem.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/student-attendance")
public class AttendanceController {


    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/{studentId}/{teacherId}")
    public ResponseEntity<Object> markAttendance(@RequestBody @Valid AttendanceDto attendanceDto, @PathVariable int studentId, @PathVariable int teacherId) {
        log.info("Marking the Attendance...");
        ResponseEntity<Object> response = attendanceService.markAttendance(attendanceDto, studentId, teacherId);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAttendanceDetailsById(@PathVariable int id) throws Exception {
        log.info("Getting Student's Attendance details by id " + id);
        ResponseEntity<Object> response = attendanceService.getAttendanceDetailsById(id);
        return response;
    }
}
