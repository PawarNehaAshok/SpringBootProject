package com.studentattendancesystem.StudentAttendanceSystem.dto;

import java.util.List;

public class StudentResponseDTO {

    private PageDTO metaInfo;
    List<StudentDto> studentList;

    public PageDTO getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(PageDTO metaInfo) {
        this.metaInfo = metaInfo;
    }

    public List<StudentDto> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentDto> studentList) {
        this.studentList = studentList;
    }

    public StudentResponseDTO(PageDTO metaInfo, List<StudentDto> studentList) {
        this.metaInfo = metaInfo;
        this.studentList = studentList;
    }
}
