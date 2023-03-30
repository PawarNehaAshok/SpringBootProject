package com.studentattendancesystem.StudentAttendanceSystem.service;

import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherDto;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.repository.TeacherRepository;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.TeacherServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Teacher Service Test Cases")
public class TeacherServiceTest {

    @InjectMocks
    TeacherServiceImpl teacherService;

    @Mock
    TeacherRepository teacherRepository;

    Page<Teacher> teacherPage;

    List<Teacher> teacherList;
    Teacher teacher;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        teacher = new Teacher(1, "John", "Mathematics", "Male", 34, null);

        teacherList = new ArrayList<>();
        teacherList.add(teacher);
        teacherPage = new PageImpl<>(teacherList);
    }

    @Test
    public void should_save_one_teacher() {
        TeacherDto teacherToSave = AutoMapper.MAPPER.mapToTeacherDto(teacher);
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        ResponseEntity<Object> actual_response = teacherService.createTeacher(teacherToSave);

        assertEquals(HttpStatus.CREATED, actual_response.getStatusCode());
        assertEquals(teacherToSave, ((HashMap) actual_response.getBody()).get("data"));
    }

    @Test
    public void should_check_null() {
        TeacherDto teacherToSave = null;
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        ResponseEntity<Object> actual_response = teacherService.createTeacher(teacherToSave);

        assertEquals(HttpStatus.NO_CONTENT, actual_response.getStatusCode());
        assertEquals(null, ((HashMap) actual_response.getBody()).get("data"));
    }

    @Test
    public void should_check_existing_teacher() {
        Teacher exsitingTeacher = new Teacher(1, "Sarah", "English", "Female", 42, null);
        when(teacherRepository.findByFirstName(exsitingTeacher.getFirstName())).thenReturn(exsitingTeacher);

        TeacherDto teacherDto = AutoMapper.MAPPER.mapToTeacherDto(exsitingTeacher);
        ResponseEntity<Object> actual_response = teacherService.createTeacher(teacherDto);

        assertEquals(HttpStatus.CONFLICT, actual_response.getStatusCode());
        assertEquals(null, ((HashMap) actual_response.getBody()).get("data"));
    }

    @Test
    public void should_return_all_teachers() {
        when(teacherRepository.findAll(PageRequest.of(0, 5))).thenReturn(teacherPage);
        ResponseEntity<Object> actual_response = teacherService.getAllTeachers(0, 5);
        assertEquals(HttpStatus.OK, actual_response.getStatusCode());
    }
}
