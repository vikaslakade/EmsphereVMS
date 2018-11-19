package com.emsphere.commando.emspherevms.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 8/18/2018.
 */

public class Expc_Vstr_Details {
    @SerializedName("visitor_name")
    String visitor_name;
    @SerializedName("mobile_no")
    String mobile_no;
    @SerializedName("purpose")
    String purpose;
    @SerializedName("visit_intime")
    String visit_intime;
    @SerializedName("visitor_image")
    String visitor_image;

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getVisit_intime() {
        return visit_intime;
    }

    public void setVisit_intime(String visit_intime) {
        this.visit_intime = visit_intime;
    }

    public String getVisitor_image() {
        return visitor_image;
    }

    public void setVisitor_image(String visitor_image) {
        this.visitor_image = visitor_image;
    }
}
