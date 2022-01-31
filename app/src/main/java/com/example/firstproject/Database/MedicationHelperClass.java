package com.example.firstproject.Database;

public class MedicationHelperClass {
    String medName,medDose;

    public MedicationHelperClass() {
        this.medName = medName;
        this.medDose = medDose;
    }

    public MedicationHelperClass(String medName, String medDose) {
        this.medName = medName;
        this.medDose = medDose;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedDose() {
        return medDose;
    }

    public void setMedDose(String medDose) {
        this.medDose = medDose;
    }
}
