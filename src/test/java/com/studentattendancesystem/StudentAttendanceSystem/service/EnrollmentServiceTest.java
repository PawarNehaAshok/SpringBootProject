package com.studentattendancesystem.StudentAttendanceSystem.service;

import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import com.studentattendancesystem.StudentAttendanceSystem.repository.StudentRepository;
import com.studentattendancesystem.StudentAttendanceSystem.repository.TeacherRepository;
import com.studentattendancesystem.StudentAttendanceSystem.service.impl.EnrollmentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Enrollment Service Test Cases")
public class EnrollmentServiceTest {

    @InjectMocks
    EnrollmentServiceImpl enrollmentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    TeacherRepository teacherRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(enrollmentService, "teacherMaxValue", 2);
    }

    @Test
    public void should_enroll_student() {
        int studentId = 1;
        int teacherId = 2;

        Student student = new Student();
        student.setId(studentId);
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(teacher);
        student.setTeachers(teacherSet);

        TeacherInterface teacherInterface = teacher;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterface);

        when(studentRepository.findTeachersByStudentId(studentId)).thenReturn(teacherList);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when((studentRepository.save(student))).thenReturn(student);

        ResponseEntity<Object> response = enrollmentService.enrollStudent(studentId, teacherId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, ((HashMap) response.getBody()).get("data"));
        verify(studentRepository).save(student);
    }

    @Test
    public void should_check_null_input() {
        int studentId = 6;
        int teacherId = 2;

        Student student = new Student();
        student.setId(studentId);
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        ResponseEntity<Object> response = enrollmentService.enrollStudent(studentId, teacherId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, ((HashMap) response.getBody()).get("data"));
    }

    @Test
    public void should_check_student_exists_or_not()
    {
        int studentId = 7;
        int teacherId = 2;

        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(teacher);

        TeacherInterface teacherInterface = teacher;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterface);

        when(studentRepository.findTeachersByStudentId(studentId)).thenReturn(teacherList);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = enrollmentService.enrollStudent(studentId, teacherId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, ((HashMap) response.getBody()).get("data"));
    }

    @Test
    public void should_check_teacher_exists_or_not()
    {
        int studentId = 1;
        //this teacherId is for testing the whether the teacher exists with the given teacherId or not.
        int teacherId = 2;

        Student student = new Student();
        student.setId(studentId);
        Teacher teacher = new Teacher();
        teacher.setId(1);

        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(teacher);

        TeacherInterface teacherInterface = teacher;
        List<TeacherInterface> teacherList = Arrays.asList(teacherInterface);

        when(studentRepository.findTeachersByStudentId(studentId)).thenReturn(teacherList);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = enrollmentService.enrollStudent(studentId, teacherId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, ((HashMap) response.getBody()).get("data"));
    }

    @Test
    public void should_enroll_student_with_only_two_teachers() {
        int studentId = 1;
        int teacherId = 2;

        Student student = new Student();
        student.setId(studentId);
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        Teacher teacher1 = new Teacher();
        teacher1.setId(3);
        Teacher teacher2 = new Teacher();
        teacher2.setId(4);

        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(teacher);
        teacherSet.add(teacher1);
        teacherSet.add(teacher2);
        student.setTeachers(teacherSet);

        List<TeacherInterface> teacherList = Arrays.asList(teacher, teacher1, teacher2);

        when(studentRepository.findTeachersByStudentId(studentId)).thenReturn(teacherList);

        ResponseEntity<Object> response = enrollmentService.enrollStudent(studentId, teacherId);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, ((HashMap) response.getBody()).get("data"));
    }
}
