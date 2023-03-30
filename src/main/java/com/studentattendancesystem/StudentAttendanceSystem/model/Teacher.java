package com.studentattendancesystem.StudentAttendanceSystem.model;

import com.studentattendancesystem.StudentAttendanceSystem.dto.TeacherInterface;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
public class Teacher implements TeacherInterface {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String firstName;
    @Column
    private String department;
    @Column
    private String gender;
    @Column
    private int age;

    @ManyToMany(mappedBy = "teachers")
    private Set<Student> students = new HashSet<>();
}
