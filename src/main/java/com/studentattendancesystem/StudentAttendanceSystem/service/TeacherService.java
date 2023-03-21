package com.studentattendancesystem.StudentAttendanceSystem.service;

import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TeacherService {

    ResponseEntity<Object> createTeacher(TeacherDto teacherDto);
    ResponseEntity<Object> getAllTeachers(int offset, int pageSize);

}
