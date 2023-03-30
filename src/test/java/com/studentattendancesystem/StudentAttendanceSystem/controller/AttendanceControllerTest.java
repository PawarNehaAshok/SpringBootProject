package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.studentattendancesystem.StudentAttendanceSystem.dto.AttendanceDto;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Attendance;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.AttendanceServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.DATA_RETRIEVED;
import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.MARK_ATTENDANCE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(AttendanceController.class)
@DisplayName("")
public class AttendanceControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    AttendanceController attendanceController;

    @Mock
    AttendanceServiceImpl attendanceService;

    @Before
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Test
    public void should_mark_attendance() throws Exception {
        Student student = new Student();
        student.setId(1);
        Teacher teacher = new Teacher();
        teacher.setId(1);

        Attendance attendance = new Attendance(1, student, teacher, new Date(2023-03-19),true);

        AttendanceDto attendanceDto = AutoMapper.MAPPER.mapToAttendanceDto(attendance);
        when(attendanceService.markAttendance(attendanceDto, student.getId(), teacher.getId())).thenReturn(ResponseHandler.response(MARK_ATTENDANCE.getMessage(), HttpStatus.OK, attendanceDto));

        String content = objectWriter.writeValueAsString(attendanceDto);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/student-attendance/{studentId}/{teacherId}","1","1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.student.id").value(student.getId()))
                .andExpect(jsonPath("$.data.teacher.id").value(teacher.getId()));
    }

    @Test
    public void should_return_student_attendance_details() throws Exception {
        Student student = new Student();
        student.setId(1);
        Teacher teacher = new Teacher();
        teacher.setId(1);

        Attendance attendance = new Attendance(1, student, teacher, new Date(2023-03-19),true);

        AttendanceDto attendanceDto = AutoMapper.MAPPER.mapToAttendanceDto(attendance);
        when(attendanceService.getAttendanceDetailsById(1)).thenReturn(ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, attendanceDto));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/api/student-attendance/{id}","1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.student.id").value(student.getId()))
                .andExpect(jsonPath("$.data.teacher.id").value(teacher.getId()));
    }
}
