package com.studentattendancesystem.StudentAttendanceSystem.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class TeacherDto {

    private int id;
    @NotEmpty(message = "first name cannot be empty")
    private String firstName;
    @NotEmpty(message = "department cannot be empty")
    private String department;
    @NotEmpty(message = "gender cannot be empty")
    private String gender;
    @NotNull(message = "age cannot be empty")
    @Min(value=18, message="must be equal or greater than 18")
    @Max(value=60, message="must be equal or less than 60")
    private int age;
}
