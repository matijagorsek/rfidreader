package com.marrowlabs.rfid.commons.model;

/**
 * Created by Matija on 30-Aug-17.
 */

public class RfidRequest {

    private double latitude;
    private double longitude;
    private String rfid;
    private String placeName;
    private String date;
    private String time;

    public RfidRequest(double latitude, double longitude, String rfid, String placeName, String date, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.rfid = rfid;
        this.placeName = placeName;
        this.date = date;
        this.time = time;
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

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
