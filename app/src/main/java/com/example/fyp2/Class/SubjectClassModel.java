package com.example.fyp2.Class;

public class SubjectClassModel {
    String subjectImage;
    String subjectCode;
    String subjectDescription;

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public SubjectClassModel(String subjectImage, String subjectCode, String subjectDescription, String subjectName, String subjectPeople, String subjectType) {
        this.subjectImage = subjectImage;
        this.subjectCode = subjectCode;
        this.subjectDescription = subjectDescription;
        this.subjectName = subjectName;
        this.subjectPeople = subjectPeople;
        this.subjectType = subjectType;
    }

    String subjectName;
    String subjectPeople;

    String subjectType;

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectPeople() {
        return subjectPeople;
    }

    public void setSubjectPeople(String subjectPeople) {
        this.subjectPeople = subjectPeople;
    }

    public SubjectClassModel(String subjectImage, String subjectCode, String subjectName, String subjectType, String subjectPeople) {
        this.subjectImage = subjectImage;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectType = subjectType;
        this.subjectPeople = subjectPeople;
    }


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
