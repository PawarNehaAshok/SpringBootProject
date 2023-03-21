package com.studentattendancesystem.StudentAttendanceSystem.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AttendanceDto {
    private int id;

    @NotNull(message = "student id cannot be empty")
    private StudentDto student;

    @NotNull(message = "teacher id cannot be empty")
    private TeacherDto teacher;

    private Date date;
    @NotNull(message = "status cannot be empty")
    private boolean status;
}
