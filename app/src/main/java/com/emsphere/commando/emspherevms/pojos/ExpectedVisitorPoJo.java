package com.emsphere.commando.emspherevms.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by commando4 on 8/18/2018.
 */

public class ExpectedVisitorPoJo {
    @SerializedName("response_status")
    String response_status;
    @SerializedName("response_message")
    String response_message;
    private ArrayList<Expc_Vstr_Details> expc_vstr_details;

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public ArrayList<Expc_Vstr_Details> getExpc_vstr_details() {
        return expc_vstr_details;
    }

    public void setExpc_vstr_details(ArrayList<Expc_Vstr_Details> expc_vstr_details) {
        this.expc_vstr_details = expc_vstr_details;
    }
}
