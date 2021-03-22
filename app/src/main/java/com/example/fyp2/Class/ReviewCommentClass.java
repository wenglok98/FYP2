package com.example.fyp2.Class;

public class ReviewCommentClass {
    String name;
    String uid;
    String enrollmentDate;
    String rating;
    String comment;
    String leaveCommentDate;

    public String getLeaveCommentDate() {
        return leaveCommentDate;
    }

    public void setLeaveCommentDate(String leaveCommentDate) {
        this.leaveCommentDate = leaveCommentDate;
    }


    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    String subjectCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReviewCommentClass() {
    }
}
