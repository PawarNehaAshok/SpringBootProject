package com.studentattendancesystem.StudentAttendanceSystem.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class StudentDto {
    private int id;
    @NotEmpty(message = "first name cannot be empty")
    private String firstName;
    @NotEmpty(message = "last name cannot be empty")
    private String lastName;
    @NotEmpty(message = "emailId cannot be empty")
    private String emailId;
    private boolean active;

    public StudentDto() {
    }
}
