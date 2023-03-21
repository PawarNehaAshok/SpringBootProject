package com.studentattendancesystem.StudentAttendanceSystem.dto;

public class PageDTO {

    long page;
    int size;
    long totalElements;

    public PageDTO(long offset, int pageSize, long totalElements) {
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
