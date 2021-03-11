package com.example.fyp2.Class;

public class SubjectClassModel {
    String subjectImage,subjectCode,subjectName;

    public String getSubjectImage() {
        return subjectImage;
    }

    public void setSubjectImage(String subjectImage) {
        this.subjectImage = subjectImage;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public SubjectClassModel(String subjectImage, String subjectCode, String subjectName) {
        this.subjectImage = subjectImage;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
    }

    public SubjectClassModel() {
    }
}
