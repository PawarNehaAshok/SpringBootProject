package com.studentattendancesystem.StudentAttendanceSystem.model;

public enum ResponseMessages {
    STUDENT_UPDATED("Student data is successfully updated into the database."),
    STUDENT_DELETED("Student data is successfully deleted from the database."),
    STUDENT_CREATED("Student data is successfully inserted into the database."),
    STUDENT_DATA_EMPTY("Student data is empty, enter value correctly"),
    STUDENT_DATA_EXISTS("The Provided student data is already present in database."),
    STUDENT_ALREADY_ENROLL("Student is already enrolled with two teachers"),
    DATA_RETRIEVED("Successfully retrieved data!"),
    EMAIL_FORMAT("Email format is wrong"),
    TEACHER_CREATED("Teacher data is successfully inserted into the database."),
    TEACHER_NO_CONTENT("Teacher details are not provided"),
    TEACHER_CONTENT("Given teacher details are already present is database."),
    STUDENT_ENROLL("Student is successfully enrolled"),
    MARK_ATTENDANCE("Attendance is marked."),
    NO_DATA_FOUND("No Data Found"),
    STUDENT_DATA_NOT_FOUND("Student data not found"),
    TEACHER_DATA_NOT_FOUND("Teacher data not found"),
    STUDENT_TEACHER_NOT_ENROLL("Given Student and Teacher are not allowed to mark the attendance cause they are not enrolled"),
    STUDENT_NOT_DELETE("Student is not present in the database to delete"),
    STUDENT_NOT_UPDATE("Student data with given id is not present in database to update."),
    ATTENDANCE_NO_CONTENT("Attendance data is empty, enter attendance details correctly.");

    private final String message;

    private ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResponseMessages{" +
                "message='" + message + '\'' +
                '}';
    }
}
