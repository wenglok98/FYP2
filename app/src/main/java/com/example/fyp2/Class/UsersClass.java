package com.example.fyp2.Class;

public class UsersClass {

    String username, useremail,userid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public UsersClass(String username, String useremail, String userid) {
        this.username = username;
        this.useremail = useremail;
        this.userid = userid;
    }
    String userPhone, userAddress, userStuid, userCgpa, userGpa;

    public String getUserPhone() {
        return userPhone;
    }

    public UsersClass() {
    }

    public UsersClass(String username, String useremail, String userid, String userPhone, String userAddress, String userStuid, String userCgpa, String userGpa) {
        this.username = username;
        this.useremail = useremail;
        this.userid = userid;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userStuid = userStuid;
        this.userCgpa = userCgpa;
        this.userGpa = userGpa;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserStuid() {
        return userStuid;
    }

    public void setUserStuid(String userStuid) {
        this.userStuid = userStuid;
    }

    public String getUserCgpa() {
        return userCgpa;
    }

    public void setUserCgpa(String userCgpa) {
        this.userCgpa = userCgpa;
    }

    public String getUserGpa() {
        return userGpa;
    }

    public void setUserGpa(String userGpa) {
        this.userGpa = userGpa;
    }
}
