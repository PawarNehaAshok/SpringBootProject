package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentResponseDTO;
import com.studentattendancesystem.StudentAttendanceSystem.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/student")
@Slf4j
public class StudentController {

    @Autowired
    StudentService studentService;


    @PostMapping
    public ResponseEntity<Object> createStudent(@RequestBody @Valid StudentDto studentDto) {
        log.info("Student creation started...");
        ResponseEntity<Object> response = studentService.createStudent(studentDto);
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody @Valid StudentDto studentDto, @PathVariable int id) throws Exception {
        log.info("Update Student by given id...");
        ResponseEntity<Object> response = studentService.updateStudent(studentDto, id);
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable int id) throws Exception {
        log.info("delete Student by given id...");
        ResponseEntity<Object> response = studentService.deleteStudent(id);
        return response;
    }

    @GetMapping
    public StudentResponseDTO getAllStudents(@RequestParam(value = "isActive", required = false, defaultValue = "false") boolean isActive, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "5") int pageSize) {
        return studentService.getAllStudents(isActive, offset, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable int id) throws Exception {
        log.info("Get Student by id...");
        ResponseEntity<Object> response = studentService.getStudentById(id);
        return response;
    }

    @GetMapping("/student-teacher/{studentId}")
    public ResponseEntity<Object> getStudentTeacherDetails(@PathVariable int studentId) throws Exception {
        log.info("Getting student by id " + studentId);
        ResponseEntity<Object> response = studentService.findStudentTeacherById(studentId);
        return response;
    }
}
