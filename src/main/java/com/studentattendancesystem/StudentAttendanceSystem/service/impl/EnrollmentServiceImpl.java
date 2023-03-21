package com.studentattendancesystem.StudentAttendanceSystem.service.impl;

import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.repository.StudentRepository;
import com.studentattendancesystem.StudentAttendanceSystem.repository.TeacherRepository;
import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.*;


@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;

    @Value("${application.teacher.maxValue}")
    private Integer teacherMaxValue;

    @Override
    public ResponseEntity<Object> enrollStudent(int studentId, int teacherId) {
        List<TeacherInterface> teacherList = studentRepository.findTeachersByStudentId(studentId);
        if (Objects.isNull(teacherList)) {
            return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
        }
        if (teacherList.size() < teacherMaxValue) {
            Optional<Teacher> teacher = teacherRepository.findById(teacherId);
            if (teacher.isPresent()) {
                Optional<Student> student = studentRepository.findById(studentId);
                if (student.isPresent()) {
                    student.get().getTeachers().add(teacher.get());
                    studentRepository.save(student.get());
                    return ResponseHandler.response(STUDENT_ENROLL.getMessage(), HttpStatus.OK, student.get());
                } else {
                    return ResponseHandler.response(STUDENT_DATA_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
                }
            } else {
                return ResponseHandler.response(TEACHER_DATA_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
            }
        } else {
            return ResponseHandler.response(STUDENT_ALREADY_ENROLL.getMessage(), HttpStatus.CONFLICT, null);
        }
    }
}
