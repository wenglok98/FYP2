package com.example.fyp2.Class;

public class EnrollClass {
    String studentName;
    String timeStamp;
    String subjectCode;
    String studyMinutes;
    String subjectType;
    String subjectName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public EnrollClass(String studentName, String timeStamp, String subjectCode, String studyMinutes, String studentID) {
        this.studentName = studentName;
        this.timeStamp = timeStamp;
        this.subjectCode = subjectCode;
        this.studyMinutes = studyMinutes;
        this.studentID = studentID;
    }

    String studentID;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getStudyMinutes() {
        return studyMinutes;
    }

    public void setStudyMinutes(String studyMinutes) {
        this.studyMinutes = studyMinutes;
    }

    public EnrollClass(String studentName, String timeStamp, String subjectCode, String studyMinutes) {
        this.studentName = studentName;
        this.timeStamp = timeStamp;
        this.subjectCode = subjectCode;
        this.studyMinutes = studyMinutes;
    }

    public EnrollClass() {
    }
}
