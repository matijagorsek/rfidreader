package com.marrowlabs.rfid.commons.service;

import com.marrowlabs.rfid.home.absence_reasons.model.AbsenceModel;
import com.marrowlabs.rfid.home.absence_reasons.model.AddAbsenceReasonResponse;
import com.marrowlabs.rfid.commons.core.Constants;
import com.marrowlabs.rfid.commons.model.AuthAdminResponse;
import com.marrowlabs.rfid.commons.model.CheckInOutResponse;
import com.marrowlabs.rfid.commons.model.UserModel;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;


/**
 * Created by Matija on 30-Aug-17.
 */

public interface ApiService {


    @GET(Constants.API_GET_ABSENCE_REASONS)
    void getAbsenceReasons(Callback<ArrayList<AbsenceModel>> callback);

    @POST(Constants.API_AUTH_ADMIN)
    @FormUrlEncoded
    void authAdmin(@Field("rf_id") String rfId,
                   Callback<AuthAdminResponse> callback);

    @POST(Constants.API_CHECK_IN_OUT)
    @FormUrlEncoded
    void checkInOut(@Field("rf_id") String rfId,
                    Callback<CheckInOutResponse> callback);

    @PATCH(Constants.API_ADD_ABSENCE_REASON)
    @FormUrlEncoded
    void addAbsenceReason(@Field("record_id") String recordId,
                          @Field("absence_reason_id") String absenceId,
                          Callback<AddAbsenceReasonResponse> callback);

    @POST(Constants.API_CREATE_USER)
    @FormUrlEncoded
    void createUser(@Field("rf_id") String rfId,
                    @Field("first_name") String firstName,
                    @Field("last_name") String lastName,
                    Callback<UserModel> callback);

}
