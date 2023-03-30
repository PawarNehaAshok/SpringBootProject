package com.studentattendancesystem.StudentAttendanceSystem.service.impl;

import com.studentattendancesystem.StudentAttendanceSystem.dto.AttendanceDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Attendance;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.repository.AttendanceRepository;
import com.studentattendancesystem.StudentAttendanceSystem.repository.StudentRepository;
import com.studentattendancesystem.StudentAttendanceSystem.repository.TeacherRepository;
import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.service.AttendanceService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.*;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<Object> markAttendance(AttendanceDto attendances, int studentId, int teacherId) {
        if (Objects.isNull(attendances)) {
            return ResponseHandler.response(ATTENDANCE_NO_CONTENT.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
        List<TeacherInterface> teacherList = studentRepository.findTeachersByStudentId(studentId);
        if (teacherList.isEmpty()) {
            return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
        } else if (!isTeacherInList(teacherList, teacherId)) {
            return ResponseHandler.response(STUDENT_TEACHER_NOT_ENROLL.getMessage(), HttpStatus.CONFLICT, null);
        }

        Attendance attendance = AutoMapper.MAPPER.mapToAttendance(attendances);
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return ResponseHandler.response(STUDENT_DATA_NOT_FOUND.getMessage() + " for id = " + studentId, HttpStatus.NOT_FOUND, null);
        }
        attendance.setStudent(student.get());
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if (teacher.isEmpty()) {
            return ResponseHandler.response(TEACHER_DATA_NOT_FOUND.getMessage() + " for id = " + teacherId, HttpStatus.NOT_FOUND, null);
        }
        attendance.setTeacher(teacher.get());
        Attendance teacher_attendance = attendanceRepository.save(attendance);

        attendances = AutoMapper.MAPPER.mapToAttendanceDto(teacher_attendance);

        return ResponseHandler.response(MARK_ATTENDANCE.getMessage(), HttpStatus.OK, attendances);
    }

    @Override
    public ResponseEntity<Object> getAttendanceDetailsById(int studentId) {
        Attendance attendance = attendanceRepository.findAttendanceByStudentId(studentId);
        if (ObjectUtils.isNotEmpty(attendance)) {
            AttendanceDto attendanceDto = AutoMapper.MAPPER.mapToAttendanceDto(attendance);
            return ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, attendanceDto);
        }
        return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    public boolean isTeacherInList(List<TeacherInterface> teacherList, int teacherId) {
        for (TeacherInterface teacher : teacherList) {
            if (teacher.getId() == teacherId) {
                return true;
            }
        }
        return false;
    }
}
