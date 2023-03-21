package com.studentattendancesystem.StudentAttendanceSystem.dto;

import java.util.List;

public class TeacherResponseDTO {

    private PageDTO metaInfo;
    List<TeacherDto> teacherList;

    public PageDTO getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(PageDTO metaInfo) {
        this.metaInfo = metaInfo;
    }

    public List<TeacherDto> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherDto> teacherList) {
        this.teacherList = teacherList;
    }
}
