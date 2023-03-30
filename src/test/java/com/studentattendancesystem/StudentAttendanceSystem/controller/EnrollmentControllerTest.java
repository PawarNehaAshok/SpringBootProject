package com.studentattendancesystem.StudentAttendanceSystem.controller;

import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.response.ResponseHandler;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.EnrollmentServiceImpl;
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

import static com.studentattendancesystem.StudentAttendanceSystem.model.ResponseMessages.STUDENT_ENROLL;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(EnrollmentController.class)
public class EnrollmentControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    EnrollmentController enrollmentController;

    @Mock
    EnrollmentServiceImpl enrollmentService;

    @Before
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
    }

    @Test
    public void enrollStudent() throws Exception {

        Student student1 = new Student(1, "Neha","Pawar", "neha@gmail.com",false,null);
        Teacher teacher = new Teacher(1, "John", "Mathematics", "Male", 34, null);

        when(enrollmentService.enrollStudent(student1.getId(), teacher.getId())).thenReturn(ResponseHandler.response(STUDENT_ENROLL.getMessage(), HttpStatus.OK, student1));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/enrollment/enroll-student/{studentId}/{teacherId}", "1","1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }
}
