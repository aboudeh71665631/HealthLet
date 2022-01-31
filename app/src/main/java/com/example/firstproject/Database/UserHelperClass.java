package com.example.firstproject.Database;

public class UserHelperClass {

    String role,fName,uName,email,password,date,gender,phoneNo;

    public UserHelperClass(){
        this.role = role;
        this.fName = fName;
        this.uName = uName;
        this.email = email;
        this.password = password;
        this.date = date;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }



    public UserHelperClass(String role, String fName, String uName, String email, String password, String date, String gender, String phoneNo) {
        this.role = role;
        this.fName = fName;
        this.uName = uName;
        this.email = email;
        this.password = password;
        this.date = date;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    public UserHelperClass(String phoneNo,String ImageUri){

    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
