package com.studentattendancesystem.StudentAttendanceSystem.service;

import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {

    ResponseEntity<Object> createStudent(StudentDto studentDto);

    ResponseEntity<Object> updateStudent(StudentDto studentDto, int id);

    ResponseEntity<Object> deleteStudent(int id);

    StudentResponseDTO getAllStudents(boolean isActive, int offset, int pageSize);

    ResponseEntity<Object> getStudentById(int id);

    ResponseEntity<Object> findStudentTeacherById(int studentId);
}
