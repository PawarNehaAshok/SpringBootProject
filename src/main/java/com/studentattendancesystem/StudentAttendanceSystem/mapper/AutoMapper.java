package com.studentattendancesystem.StudentAttendanceSystem.mapper;

import com.studentattendancesystem.StudentAttendanceSystem.dto.*;
import com.studentattendancesystem.StudentAttendanceSystem.model.Attendance;
import com.studentattendancesystem.StudentAttendanceSystem.model.Student;
import com.studentattendancesystem.StudentAttendanceSystem.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface AutoMapper {

    AutoMapper MAPPER = Mappers.getMapper(AutoMapper.class);

    StudentDto mapToStudentDto(Student student);

    Student mapToStudent(StudentDto studentDto);

    List<StudentDto> mapToStudentDtoList(List<Student> studentList);

    List<Student> mapToStudentList(List<StudentDto> studentDtoList);

    TeacherDto mapToTeacherDto(Teacher teacher);

    Teacher mapToTeacher(TeacherDto teacherDto);

    List<TeacherDto> mapToTeacherDtoList(List<Teacher> teacherList);

    List<Teacher> mapToTeacherList(List<TeacherDto> teacherDto);

    AttendanceDto mapToAttendanceDto(Attendance attendance);

    Attendance mapToAttendance(AttendanceDto attendanceDto);

    List<AttendanceDto> mapToAttendanceDtoList(List<Attendance> attendanceList);

    List<Attendance> mapToAttendanceList(List<AttendanceDto> attendanceDtoList);

    @Mapping(source = "pageable.offset", target = "page")
    @Mapping(source = "pageable.pageSize", target = "size")
    @Mapping(source = "page.totalElements", target = "totalElements")
    PageDTO mapToPageDTO(Page<?> page, Pageable pageable);

    @Mapping(source = "pageDTO", target = "metaInfo")
    StudentResponseDTO mapToStudentResponseDTO(PageDTO pageDTO, List<StudentDto> studentList);

    @Mapping(source = "pageDTO", target = "metaInfo")
    TeacherResponseDTO mapToTeacherResponseDTO(PageDTO pageDTO, List<TeacherDto> teacherList);
}
