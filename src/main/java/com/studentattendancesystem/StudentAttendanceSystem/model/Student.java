package com.studentattendancesystem.StudentAttendanceSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
@SQLDelete(sql = "UPDATE student SET active = true WHERE id=?")
@FilterDef(name = "deletedStudentFilter", parameters = @ParamDef(name = "isActive", type = Boolean.class))
@Filter(name = "deletedStudentFilter", condition = "active = :isActive")
public class Student {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique = true)
    private String emailId;
    @Column
    private boolean active = Boolean.FALSE;

    @ManyToMany
    @JoinTable(name = "student_teacher", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private Set<Teacher> teachers;
}
