package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.studentattendancesystem.StudentAttendanceSystem.dto.PageDTO;
import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentResponseDTO;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.StudentServiceImpl;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    StudentController studentController;

    @Mock
    StudentServiceImpl studentService;

    Student student1;
    Student student2;
    Student student3;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Before
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        student1 = new Student(1, "Neha","Pawar", "neha@gmail.com",false,null);
        student2 = new Student(2, "Kelvin", "Kerl", "kelvin@gmail.com", false,null);
        student3 = new Student(3, "John", "Mathew", "John.Mathew@xyz.com", false, null);
    }

    @Test
    public void createStudent() throws Exception {
        StudentDto studentDto = AutoMapper.MAPPER.mapToStudentDto(student1);
        when(studentService.createStudent(studentDto)).thenReturn(ResponseHandler.response(STUDENT_CREATED.getMessage(), HttpStatus.CREATED, studentDto));

        String content = objectWriter.writeValueAsString(studentDto);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        MvcResult result =  mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value(student1.getFirstName())).andReturn();
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @Test
    public void getAllStudents() throws Exception {
        PageDTO pageDTO = new PageDTO(0, 5, 3);
        List<StudentDto> studentDtoList = AutoMapper.MAPPER.mapToStudentDtoList(Arrays.asList(student1, student2, student3));
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO(pageDTO, studentDtoList);

        when(studentService.getAllStudents(Boolean.FALSE, 0, 5)).thenReturn(studentResponseDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/api/student")
                .param("isActive","false")
                .param("offset","0")
                .param("pageSize","5");

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentList.size()").value(studentResponseDTO.getStudentList().size()))
                .andExpect(jsonPath("$.metaInfo.page").value(studentResponseDTO.getMetaInfo().getPage()))
                .andExpect(jsonPath("$.studentList[:1].firstName").value(studentDtoList.get(0).getFirstName()));
    }

    @Test
    public void updateStudent() throws Exception {

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("Paul");
        studentDto.setLastName("parry");
        studentDto.setEmailId("paul.parry@gmail.com");

        when(studentService.updateStudent(studentDto, 1)).thenReturn(ResponseHandler.response(STUDENT_UPDATED.getMessage(), HttpStatus.OK, studentDto));

        String content = objectWriter.writeValueAsString(studentDto);

        mockMvc.perform(put("/api/student/{id}","1").content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setFirstName("Paul");
        studentDto.setLastName("parry");
        studentDto.setEmailId("paul.parry@gmail.com");

        when(studentService.deleteStudent(1)).thenReturn(ResponseHandler.response(STUDENT_DELETED.getMessage(), HttpStatus.OK, studentDto));

        mockMvc.perform(delete("/api/student/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudentById() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setFirstName("Paul");
        studentDto.setLastName("parry");
        studentDto.setEmailId("paul.parry@gmail.com");

        when(studentService.getStudentById(1)).thenReturn(ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, studentDto));

        mockMvc.perform(get("/api/student/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getStudentTeacherDetails() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(1);
        studentDto.setFirstName("Paul");
        studentDto.setLastName("parry");
        studentDto.setEmailId("paul.parry@gmail.com");

        when(studentService.findStudentTeacherById(studentDto.getId())).thenReturn(ResponseHandler.response(DATA_RETRIEVED.getMessage(), HttpStatus.OK, studentDto));

        mockMvc.perform(get("/api/student/student-teacher/{studentId}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
