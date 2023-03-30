package com.studentattendancesystem.StudentAttendanceSystem.service;

import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentDto;
import com.studentattendancesystem.StudentAttendanceSystem.dto.StudentResponseDTO;
import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import com.studentattendancesystem.StudentAttendanceSystem.mapper.AutoMapper;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.repository.StudentRepository;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.StudentServiceImpl;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Student Service Test Cases")
public class StudentServiceTest {

    @InjectMocks
    StudentServiceImpl studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    Filter filter;

    @Mock
    Session session;
    Page<Student> studentPage;
    Student student1;
    Student student2;
    List<Student> studentList;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        student1 = new Student(1, "Neha", "Pawar", "neha123@gmail.com", false, null);
        student2 = new Student(2, "Kelvin", "Kerl", "kelvin@gmail.com", false, null);

        studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentPage = new PageImpl<>(studentList);
    }

    @Test
    public void should_save_one_student() {
        StudentDto studentToSave = AutoMapper.MAPPER.mapToStudentDto(student1);
        when(studentRepository.save(student1)).thenReturn(student1);
        ResponseEntity<Object> actual_response = studentService.createStudent(studentToSave);

        assertEquals(actual_response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(((HashMap) actual_response.getBody()).get("data"), studentToSave);
    }

    @Test
    public void should_update_one_student() {
        StudentDto studentToUpdate = AutoMapper.MAPPER.mapToStudentDto(student1);
        when(studentRepository.findById(student1.getId())).thenReturn(Optional.of(student1));
        when(studentRepository.save(student1)).thenReturn(student1);
        ResponseEntity<Object> actual_response = studentService.updateStudent(studentToUpdate, student1.getId());

        assertEquals(actual_response.getStatusCode(), HttpStatus.OK);
        assertEquals(((HashMap) actual_response.getBody()).get("data"), studentToUpdate);
    }

    @Test
    public void should_return_all_students() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("deletedStudentFilter")).thenReturn(filter);
        when(studentRepository.findAll(PageRequest.of(0, 5, Sort.by("id").ascending()))).thenReturn(studentPage);
        StudentResponseDTO response = studentService.getAllStudents(false, 0, 5);
        List<StudentDto> studentDtoList = AutoMapper.MAPPER.mapToStudentDtoList(studentList);
        assertEquals(response.getStudentList(), studentDtoList);
    }

    @Test
    public void should_delete_one_student() {
        int studentId = 1;
        Student existingStudent = new Student(studentId, "John", "Doe", "john.doe@example.com", false, null);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));

        ResponseEntity<Object> actual_response = studentService.deleteStudent(studentId);

        assertEquals(actual_response.getStatusCode(), HttpStatus.OK);
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    public void should_get_one_student() {
        Student student = new Student(1, "John", "Mathew", "John.Mathew@xyz.com", false, null);
        StudentDto studentDto = AutoMapper.MAPPER.mapToStudentDto(student);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        ResponseEntity<Object> response = studentService.getStudentById(student.getId());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(((HashMap) response.getBody()).get("data"), studentDto);
    }

    @Test
    public void should_return_student_teacher_details() {
        Student student = new Student(1, "John", "Mathew", "John.Mathew@xyz.com", false, null);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        Teacher teacher = new Teacher();
        teacher.setId(1);

        TeacherInterface teacherInterface = teacher;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterface);

        when(studentRepository.findTeachersByStudentId(student.getId())).thenReturn(teacherList);

        ResponseEntity<Object> actual_response = studentService.findStudentTeacherById(student.getId());

        assertEquals(actual_response.getStatusCode(), HttpStatus.OK);
        verify(studentRepository).findTeachersByStudentId(student.getId());
    }

    @Test
    public void should_check_null_create_student() {
        ResponseEntity<Object> actual_response = studentService.createStudent(null);
        assertEquals(actual_response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertNull(((HashMap) actual_response.getBody()).get("data"));
    }

    @Test
    public void should_check_null_update_student() {
        ResponseEntity<Object> actual_response = studentService.updateStudent(null, 1);
        assertEquals(actual_response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertNull(((HashMap) actual_response.getBody()).get("data"));
    }

    @Test
    public void should_check_email_format() {
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("Paul");
        studentDto.setLastName("Garry");
        studentDto.setEmailId("paul.garry@gmail.ghghghgghakjfsghgh09%%%88%");

        ResponseEntity<Object> actual_response = studentService.createStudent(studentDto);

        assertEquals(actual_response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(((HashMap) actual_response.getBody()).get("data"), null);
    }

    @Test
    public void should_check_input() {
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("");
        studentDto.setLastName("Garry");
        studentDto.setEmailId("paul.garry@gmail.ghghghgghakjfsghgh09%%%88%");

        ResponseEntity<Object> actual_response = studentService.createStudent(studentDto);

        assertEquals(actual_response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(((HashMap) actual_response.getBody()).get("data"), null);
    }

    @Test
    public void should_check_email_format_update() {
        int studentId = 1;
        Student existingStudent = new Student(studentId, "John", "Doe", "john.doe@example.com", false, null);
        Student updatedStudent = new Student(studentId, "Jane", "Smith", "jane.smith@example.jko883%", false, null);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));

        StudentDto updatedStudentDto = AutoMapper.MAPPER.mapToStudentDto(updatedStudent);
        ResponseEntity<Object> actual_response = studentService.updateStudent(updatedStudentDto, studentId);

        assertEquals(actual_response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(((HashMap) actual_response.getBody()).get("data"), null);
    }

    @Test
    public void should_check_input_update() {
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("");
        studentDto.setLastName("Garry");
        studentDto.setEmailId("paul.garry@gmail.ghghghgghakjfsghgh09%%%88%");

        ResponseEntity<Object> actual_response = studentService.createStudent(studentDto);

        assertEquals(actual_response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(((HashMap) actual_response.getBody()).get("data"), null);
    }

    @Test
    public void should_return_not_found_delete() {
        int studentId = 1;
        ResponseEntity<Object> actual_response = studentService.deleteStudent(studentId);
        assertEquals(actual_response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void should_not_found_one_student() {
        Student student = new Student(1, "John", "Mathew", "John.Mathew@xyz.com", false, null);
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        ResponseEntity<Object> response = studentService.getStudentById(2);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertNull(((HashMap) response.getBody()).get("data"));
    }

    @Test
    public void should_return_not_found_student_teacher_details() {
        when(studentRepository.findById(3)).thenReturn(Optional.empty());

        ResponseEntity<Object> actual_response = studentService.findStudentTeacherById(3);

        assertEquals(actual_response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertNull(((HashMap) actual_response.getBody()).get("data"));
    }
}
