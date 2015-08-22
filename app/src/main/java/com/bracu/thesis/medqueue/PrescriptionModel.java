package com.bracu.thesis.medqueue;

/**
 * Created by Tahmid on 22-Aug-15.
 */
public class PrescriptionModel {
    private String medName;
    private String dose;
    private String freq;

    @Override
    public String toString() {
        return "PrescriptionModel{" +
                "MedName='" + medName + '\'' +
                ", dose='" + dose + '\'' +
                ", freq='" + freq + '\'' +
                '}';
    }

    public PrescriptionModel() {
        super();
    }

    public PrescriptionModel(String medName, String freq, String dose) {
        this.medName = medName;
        this.freq = freq;
        this.dose = dose;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
