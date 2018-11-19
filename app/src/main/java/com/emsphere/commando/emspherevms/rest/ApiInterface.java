package com.emsphere.commando.emspherevms.rest;

import com.emsphere.commando.emspherevms.pojos.CommanResponsePojo;
import com.emsphere.commando.emspherevms.pojos.ExpectedVisitorPoJo;
import com.emsphere.commando.emspherevms.pojos.ExpectedVisitorSendData;
import com.emsphere.commando.emspherevms.pojos.LocationDataPojo;
import com.emsphere.commando.emspherevms.pojos.NewRegistrationPojo;
import com.emsphere.commando.emspherevms.pojos.NewRegistrationResponsePojo;
import com.emsphere.commando.emspherevms.pojos.SearchExistingVisitorPojo;
import com.emsphere.commando.emspherevms.pojos.SearchVisitorResponsePojo;
import com.emsphere.commando.emspherevms.pojos.SendVisitCodePojo;
import com.emsphere.commando.emspherevms.pojos.UpdateNewVisitor;
import com.emsphere.commando.emspherevms.pojos.UpdateVisitorEntryPojo;
import com.emsphere.commando.emspherevms.pojos.ValidateVisitOrInviteCodePojo;
import com.emsphere.commando.emspherevms.pojos.VisitorSendData;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by commando4 on 1/2/2018.
 */

public interface ApiInterface {
 //Enterprise_MobileApp
   // @FormUrlEncoded
   //@Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("GetExpectedVisitorsDetails.ashx")
    Call<ExpectedVisitorPoJo> getExpectedVisitor(@Body ExpectedVisitorSendData expectedVisitorSendData);

    @POST("SaveVisitorEntryDetails.ashx")
    Call<CommanResponsePojo> sendExpectedVisitor(@Body VisitorSendData visitorSendData);

    @POST("RegisteredVisitorDetails.ashx")
    Call<SearchVisitorResponsePojo> searchExistingVisitor(@Body SearchExistingVisitorPojo searchExistingVisitorPojo);

    @POST("RegisterVisitorDetails.ashx")
    Call<NewRegistrationResponsePojo> NewVisitor(@Body NewRegistrationPojo newRegistrationPojo);

    @GET("GetLocationsList.ashx")
    Call<LocationDataPojo> getLocationDetails();

    @POST("VerifyVisitOrInviteCode.ashx")
    Call<ValidateVisitOrInviteCodePojo> validateVisitCode(@Body SendVisitCodePojo sendVisitCodePojo);

    @POST("UpdateVisitorRegistrationDetails.ashx")
    Call<NewRegistrationResponsePojo> updateVisitorRegistration(@Body UpdateNewVisitor updateNewVisitor);

    @POST("UpdateVisitorEntryDetails.ashx")
    Call<CommanResponsePojo> updateVisitorEntryDetails(@Body UpdateVisitorEntryPojo updateVisitorEntryPojo);




}

