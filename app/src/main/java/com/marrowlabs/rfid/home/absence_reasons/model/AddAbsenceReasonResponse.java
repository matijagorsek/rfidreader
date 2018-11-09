package com.marrowlabs.rfid.home.absence_reasons.model;

import com.google.gson.annotations.SerializedName;
import com.marrowlabs.rfid.commons.model.UserModel;

/**
 * Created by Matija on 12-Sep-17.
 */

public class AddAbsenceReasonResponse {

    private int id;
    private int status;
    private double latitude;
    private double longitude;
    private String place;
    @SerializedName("user")
    private UserModel userModel;
    @SerializedName("absence_reason")
    private AbsenceModel absenceModel;

    public AddAbsenceReasonResponse(int id, int status, double latitude, double longitude, String place, UserModel userModel, AbsenceModel absenceModel) {
        this.id = id;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place = place;
        this.userModel = userModel;
        this.absenceModel = absenceModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public AbsenceModel getAbsenceModel() {
        return absenceModel;
    }

    public void setAbsenceModel(AbsenceModel absenceModel) {
        this.absenceModel = absenceModel;
    }
}
