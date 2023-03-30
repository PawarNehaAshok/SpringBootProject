package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.studentattendancesystem.StudentAttendanceSystem.dto.PageDTO;
import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherResponseDTO;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.TeacherServiceImpl;
import org.junit.Before;
import org.junit.Test;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.DATA_RETRIEVED;
import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.TEACHER_CREATED;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    TeacherController teacherController;

    @Mock
    TeacherServiceImpl teacherService;

    Teacher teacher = new Teacher(1, "John", "Mathematics", "Male", 34, null);

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Before
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    public void createTeacher() throws Exception {
        TeacherDto teacherDto = AutoMapper.MAPPER.mapToTeacherDto(teacher);
        when(teacherService.createTeacher(teacherDto)).thenReturn(ResponseHandler.response(TEACHER_CREATED.getMessage(), HttpStatus.CREATED, teacherDto));

        String content = objectWriter.writeValueAsString(teacherDto);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value(teacher.getFirstName()));
    }

    @Test
    public void getAllTeachers() throws Exception {
        TeacherDto teacherDto = AutoMapper.MAPPER.mapToTeacherDto(teacher);
        List<TeacherDto> teacherDtoList = new ArrayList<>(Arrays.asList(teacherDto));

        PageDTO pageDTO = new PageDTO(0, 5, 1);
        TeacherResponseDTO teacherResponseDTO = new TeacherResponseDTO();
        teacherResponseDTO.setMetaInfo(pageDTO);
        teacherResponseDTO.setTeacherList(teacherDtoList);

        when(teacherService.getAllTeachers(0,5)).thenReturn(ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, teacherResponseDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.teacherList.size()").value(teacherResponseDTO.getTeacherList().size()));
    }
}
