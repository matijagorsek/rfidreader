package com.marrowlabs.rfid.commons.service;

import com.marrowlabs.rfid.home.absence_reasons.model.AbsenceModel;
import com.marrowlabs.rfid.home.absence_reasons.model.AddAbsenceReasonResponse;
import com.marrowlabs.rfid.commons.model.AuthAdminResponse;
import com.marrowlabs.rfid.commons.model.CheckInOutResponse;
import com.marrowlabs.rfid.commons.model.UserModel;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Matija on 11-Sep-17.
 */

public class RfidService {

    public static Bus bus = BusProvider.getInstance();
    public static ApiService service = ServiceProvider.getService();

    public static void authAdmin(String rfid) {
        service.authAdmin(rfid,
                new Callback<AuthAdminResponse>() {
                    @Override
                    public void success(AuthAdminResponse authAdminResponse, Response response) {
                        bus.post(authAdminResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        bus.post(error);
                    }
                });
    }

    public static void checkInOut(String rfid) {
        service.checkInOut(rfid,
                new Callback<CheckInOutResponse>() {
                    @Override
                    public void success(CheckInOutResponse checkInOutResponse, Response response) {
                        bus.post(checkInOutResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        bus.post(error);
                    }
                });
    }

    public static void getAbsenceReasons() {
        service.getAbsenceReasons(new Callback<ArrayList<AbsenceModel>>() {
            @Override
            public void success(ArrayList<AbsenceModel> absenceModels, Response response) {
                bus.post(absenceModels);
            }

            @Override
            public void failure(RetrofitError error) {
                bus.post(error);
            }
        });
    }

    public static void addAbsenceReason(String recordId, String absenceId) {
        service.addAbsenceReason(recordId, absenceId,
                new Callback<AddAbsenceReasonResponse>() {
                    @Override
                    public void success(AddAbsenceReasonResponse addAbsenceReasonResponse, Response response) {
                        bus.post(addAbsenceReasonResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        bus.post(error);
                    }
                });
    }

    public static void createUser(String rfId, String firstName, String lastName) {
        service.createUser(rfId, firstName, lastName,
                new Callback<UserModel>() {
                    @Override
                    public void success(UserModel userModel, Response response) {
                        bus.post(userModel);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        bus.post(error);
                    }
                });
    }
}
