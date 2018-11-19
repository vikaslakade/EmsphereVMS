package com.emsphere.commando.emspherevms;

/**
 * Created by commando1 on 4/20/2017.
 */

public class VisitorData {
    String image;
    String name;
    String mobileno;
    String purpose;
    String appointmenttime;

   /* public VisitorData(String image, String name, String mobileno, String purpose, String appointmenttime) {
        this.image = image;
        this.name = name;
        this.mobileno = mobileno;
        this.purpose = purpose;
        this.appointmenttime = appointmenttime;
    }*/



    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAppointmenttime() {
        return appointmenttime;
    }

    public void setAppointmenttime(String appointmenttime) {
        this.appointmenttime = appointmenttime;
    }


}
