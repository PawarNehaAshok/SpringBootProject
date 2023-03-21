package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherDto;
import com.studentattendancesystem.StudentAttendanceSystem.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/teacher")
@Slf4j
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @PostMapping
    public ResponseEntity<Object> createTeacher(@Valid @RequestBody TeacherDto teacherDto) throws Exception {
        log.info("Teacher creation started...");
        ResponseEntity<Object> response = teacherService.createTeacher(teacherDto);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTeachers(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "5") int pageSize) {
        return teacherService.getAllTeachers(offset, pageSize);
    }
}
