package com.marrowlabs.rfid.commons.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matija on 11-Sep-17.
 */

public class CheckInOutResponse {

    private int id;
    private int status;
    private double latitude;
    private double longitude;
    private String place;
    @SerializedName("user")
    private UserModel userModel;
    @SerializedName("absence_reason")
    private String absenceReason;

    public CheckInOutResponse(int id, int status, double latitude, double longitude, String place, UserModel userModel, String absenceReason) {
        this.id = id;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place = place;
        this.userModel = userModel;
        this.absenceReason = absenceReason;
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

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
    }
}
