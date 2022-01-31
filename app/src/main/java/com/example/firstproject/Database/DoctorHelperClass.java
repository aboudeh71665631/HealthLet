package com.example.firstproject.Database;

public class DoctorHelperClass {
    String fName,specialization,location;
    String imageUri;


    public DoctorHelperClass() {
        this.fName = fName;
        this.specialization = specialization;
        this.location = location;
        this.imageUri = imageUri;
    }

    public DoctorHelperClass(String fName, String specialization, String location) {
        this.fName = fName;
        this.specialization = specialization;
        this.location = location;
    }

    public String getfName() {
        return fName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getLocations() {
        return location;
    }
    public String getImageUri() {
        return imageUri;
    }
}
