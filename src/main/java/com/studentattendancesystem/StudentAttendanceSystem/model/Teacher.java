package com.studentattendancesystem.StudentAttendanceSystem.model;

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
public class Teacher {
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
