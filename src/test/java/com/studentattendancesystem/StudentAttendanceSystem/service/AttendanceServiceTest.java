package com.studentattendancesystem.StudentAttendanceSystem.service;

import com.studentattendancesystem.StudentAttendanceSystem.dto.AttendanceDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Attendance;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.repository.AttendanceRepository;
import com.studentattendancesystem.StudentAttendanceSystem.repository.StudentRepository;
import com.studentattendancesystem.StudentAttendanceSystem.repository.TeacherRepository;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.AttendanceServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Attendance Service Test Cases")
public class AttendanceServiceTest {
    @InjectMocks
    AttendanceServiceImpl attendanceService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    AttendanceRepository attendanceRepository;
    AttendanceDto attendanceDto;
    Student student;
    Teacher teacher;
    Attendance attendance;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        student = new Student();
        student.setId(1);

        teacher = new Teacher();
        teacher.setId(2);

        attendanceDto = new AttendanceDto();
        attendanceDto.setId(3);
        attendanceDto.setDate(new Date());
        attendanceDto.setStatus(true);

        attendance = AutoMapper.MAPPER.mapToAttendance(attendanceDto);
    }

    @Test
    public void markAttendanceSuccess() {
        TeacherInterface teacherInterface = teacher;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterface);

        when(studentRepository.findTeachersByStudentId(1)).thenReturn(teacherList);
        when(teacherRepository.findById(2)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(attendanceRepository.save(attendance)).thenReturn(attendance);

        ResponseEntity<Object> response = attendanceService.markAttendance(attendanceDto, student.getId(), teacher.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void markAttendanceNoContent()
    {
        ResponseEntity<Object> response = attendanceService.markAttendance(null, 1, 1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(((HashMap)response.getBody()).get("data"));
    }

    @Test
    public void markAttendanceStudentTeacherNotFound()
    {
        when(studentRepository.findTeachersByStudentId(1)).thenReturn(new ArrayList<>());
        ResponseEntity<Object> response = attendanceService.markAttendance(attendanceDto, student.getId(), teacher.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(((HashMap)response.getBody()).get("data"));
    }

    @Test
    public void markAttendanceStudentTeacherNotEnroll()
    {
        Teacher teacherNotContain = new Teacher();
        teacherNotContain.setId(3);
        TeacherInterface teacherInterfaceNotContain = teacherNotContain;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterfaceNotContain);

        when(studentRepository.findTeachersByStudentId(1)).thenReturn(teacherList);
        ResponseEntity<Object> response = attendanceService.markAttendance(attendanceDto, student.getId(), teacher.getId());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(((HashMap)response.getBody()).get("data"));
    }

    @Test
    public void markAttendanceStudentNotFound()
    {
        int studentId = 5;
        TeacherInterface teacherInterface = teacher;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterface);

        when(studentRepository.findTeachersByStudentId(1)).thenReturn(teacherList);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        ResponseEntity<Object> response = attendanceService.markAttendance(attendanceDto, student.getId(), teacher.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(((HashMap)response.getBody()).get("data"));
    }

    @Test
    public void markAttendanceTeacherNotFound()
    {
        int teacherId = 5;
        TeacherInterface teacherInterface = teacher;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterface);

        when(studentRepository.findTeachersByStudentId(1)).thenReturn(teacherList);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());
        ResponseEntity<Object> response = attendanceService.markAttendance(attendanceDto, student.getId(), teacher.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(((HashMap)response.getBody()).get("data"));
    }

    @Test
    public void should_return_student_attendance_details() {
        AttendanceDto attendanceDto = AutoMapper.MAPPER.mapToAttendanceDto(attendance);
        when(attendanceRepository.findAttendanceByStudentId(student.getId())).thenReturn(attendance);

        ResponseEntity<Object> response = attendanceService.getAttendanceDetailsById(student.getId());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(((HashMap) response.getBody()).get("data"), attendanceDto);
    }

    @Test
    public void should_check_attendance_data() {
        when(attendanceRepository.findAttendanceByStudentId(4)).thenReturn(attendance);

        ResponseEntity<Object> response = attendanceService.getAttendanceDetailsById(student.getId());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(((HashMap) response.getBody()).get("data"), null);
    }
}
