package com.bracu.thesis.medqueue;

/**
 * Created by Tahmid on 21-Aug-15.
 */
public class AppointmentModel {
    private String time;
    private String date;
    private String service_name;
    private String patient;

    public AppointmentModel(){
        super();
    }

    @Override
    public String toString() {
        return "AppointmentModel{" +
                "time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", service_name='" + service_name + '\'' +
                ", patient='" + patient + '\'' +
                '}';
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public AppointmentModel(String date, String time, String service_name, String patient) {
        this.time = time;
        this.date = date;
        this.service_name = service_name;
        this.patient = patient;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
