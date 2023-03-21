package com.studentattendancesystem.StudentAttendanceSystem.service.impl;

import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.dto.PageDTO;
import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentResponseDTO;
import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.repository.StudentRepository;
import com.studentattendancesystem.StudentAttendanceSystem.service.StudentService;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    EntityManager entityManager;

    @Override
    public ResponseEntity<Object> createStudent(StudentDto studentDto) {
        Student student = AutoMapper.MAPPER.mapToStudent(studentDto);
        if (Objects.isNull(student)) {
            return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NO_CONTENT, null);
        } else {
            if (ObjectUtils.isEmpty(student.getFirstName()) || ObjectUtils.isEmpty(student.getLastName()) || ObjectUtils.isEmpty(student.getEmailId())) {
                return ResponseHandler.response(STUDENT_DATA_EMPTY.getMessage(), HttpStatus.BAD_REQUEST, null);
            } else if (!checkEmailFormat(student.getEmailId())) {
                return ResponseHandler.response(EMAIL_FORMAT.getMessage(), HttpStatus.BAD_REQUEST, null);
            } else if (studentRepository.existsByEmailId(student.getEmailId())) {
                return ResponseHandler.response(STUDENT_DATA_EXISTS.getMessage(), HttpStatus.CONFLICT, null);
            }
        }
        studentRepository.save(student);
        return ResponseHandler.response(STUDENT_CREATED.getMessage(), HttpStatus.CREATED, studentDto);
    }

    @Override
    public StudentResponseDTO getAllStudents(boolean isActive, int offset, int pageSize) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedStudentFilter");
        filter.setParameter("isActive", isActive);

        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(offset, pageSize, Sort.by("id").ascending()));

        List<StudentDto> students = AutoMapper.MAPPER.mapToStudentDtoList(studentPage.getContent());
        PageDTO pageDTO = AutoMapper.MAPPER.mapToPageDTO(studentPage, studentPage.getPageable());
        StudentResponseDTO studentResponseDTO = AutoMapper.MAPPER.mapToStudentResponseDTO(pageDTO, students);

        session.disableFilter("deletedStudentFilter");
        return studentResponseDTO;
    }

    @Override
    public ResponseEntity<Object> updateStudent(StudentDto studentDto, int id) {
        if (Objects.isNull(studentDto)) {
            return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NO_CONTENT, null);
        }
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isEmpty()) {
            return ResponseHandler.response(STUDENT_NOT_UPDATE.getMessage(), HttpStatus.NOT_FOUND, null);
        }

        if (ObjectUtils.isNotEmpty(studentDto.getFirstName())) {
            existingStudent.get().setFirstName(studentDto.getFirstName());
        }
        if (ObjectUtils.isNotEmpty(studentDto.getLastName())) {
            existingStudent.get().setLastName(studentDto.getLastName());
        }
        if (ObjectUtils.isNotEmpty(studentDto.getEmailId())) {
            if (!checkEmailFormat(studentDto.getEmailId())) {
                return ResponseHandler.response(EMAIL_FORMAT.getMessage(), HttpStatus.BAD_REQUEST, null);
            }
            existingStudent.get().setEmailId(studentDto.getEmailId());
        }
        studentRepository.save(existingStudent.get());

        return ResponseHandler.response(STUDENT_UPDATED.getMessage(), HttpStatus.OK, studentDto);
    }

    @Override
    public ResponseEntity<Object> deleteStudent(int id) {
        Optional<Student> studentDto = studentRepository.findById(id);
        if (studentDto.isEmpty()) {
            return ResponseHandler.response(STUDENT_NOT_DELETE.getMessage(), HttpStatus.NOT_FOUND, null);
        }
        studentRepository.deleteById(id);
        return ResponseHandler.response(STUDENT_DELETED.getMessage(), HttpStatus.OK, studentDto);
    }

    @Override
    public ResponseEntity<Object> getStudentById(int id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            StudentDto studentDto = AutoMapper.MAPPER.mapToStudentDto(student.get());
            return ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, studentDto);
        }
        return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @Override
    public ResponseEntity<Object> findStudentTeacherById(int studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            List<TeacherInterface> teacherList = studentRepository.findTeachersByStudentId(studentId);
            if (!teacherList.isEmpty()) {
                return ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, teacherList);
            }
        }
        return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    public boolean checkEmailFormat(String emailId) {
        Predicate<String> emailFilter = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}").asPredicate();
        return emailFilter.test(emailId);
    }
}
