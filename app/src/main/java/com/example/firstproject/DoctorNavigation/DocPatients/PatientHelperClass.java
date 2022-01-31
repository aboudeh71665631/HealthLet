package com.example.firstproject.DoctorNavigation.DocPatients;

public class PatientHelperClass {
    String role, fName, uName, email, password, date, gender, phoneNo;
    String address, bType, imageUri;


    public PatientHelperClass(String fName, String email, String date, String phoneNo,String gender) {
        this.fName = fName;
        this.email = email;
        this.date = date;
        this.phoneNo = phoneNo;
        this.gender = gender;
    }

    public PatientHelperClass() {
        this.fName = fName;
        this.email = email;
        this.date = date;
        this.phoneNo = phoneNo;
        this.address = address;
        this.bType = bType;
        this.imageUri = imageUri;
    }


    public void changeName(String text) {
        fName = text;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getRole() {
        return role;
    }

    public String getuName() {
        return uName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getbType() {
        return bType;
    }

    public String getfName() {
        return fName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }
}
