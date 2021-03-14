package com.example.fyp2.Class;

public class EnrollClass {
    String studentName, timeStamp, subjectCode,studyMinutes;

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
