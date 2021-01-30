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
}
