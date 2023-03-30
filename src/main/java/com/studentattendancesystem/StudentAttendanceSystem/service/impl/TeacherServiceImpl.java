package com.studentattendancesystem.StudentAttendanceSystem.service.impl;

import com.studentattendancesystem.StudentAttendanceSystem.dto.*;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.repository.TeacherRepository;
import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.service.TeacherService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.*;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<Object> createTeacher(TeacherDto teacherDto) {
        if (Objects.isNull(teacherDto)) {
            return ResponseHandler.response(TEACHER_NO_CONTENT.getMessage(), HttpStatus.NO_CONTENT, null);
        }
        Teacher teacher = AutoMapper.MAPPER.mapToTeacher(teacherDto);
        Teacher existingTeacher = teacherRepository.findByFirstName(teacher.getFirstName());
        if (!Objects.isNull(existingTeacher) && existingTeacher.getFirstName().equals(teacher.getFirstName())) {
            return ResponseHandler.response(TEACHER_CONTENT.getMessage(), HttpStatus.CONFLICT, null);
        }
        teacherRepository.save(teacher);
        return ResponseHandler.response(TEACHER_CREATED.getMessage(), HttpStatus.CREATED, teacherDto);
    }

    @Override
    public ResponseEntity<Object> getAllTeachers(int offset, int pageSize) {
        Page<Teacher> teacherPage = teacherRepository.findAll(PageRequest.of(offset, pageSize));
        if (ObjectUtils.isNotEmpty(teacherPage)) {
            List<TeacherDto> teachers = AutoMapper.MAPPER.mapToTeacherDtoList(teacherPage.getContent());
            PageDTO pageDTO= new PageDTO();
            pageDTO.setSize(offset);
            pageDTO.setPage(pageSize);
            pageDTO.setTotalElements(teacherPage.getTotalElements());
            TeacherResponseDTO teacherResponseDTO = AutoMapper.MAPPER.mapToTeacherResponseDTO(pageDTO, teachers);
            return ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, teacherResponseDTO);
        }
        return ResponseHandler.response(NO_DATA_FOUND.getMessage(), HttpStatus.NOT_FOUND, null);
    }

}
