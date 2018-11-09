package com.marrowlabs.rfid.home.absence_reasons.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matija on 06-Sep-17.
 */

public class AbsenceModel implements Parcelable {

    private int id;
    @SerializedName("working_hours")
    private int workingHours;
    private String title;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public AbsenceModel(int id, int workingHours, String title, String createdAt, String updatedAt, String color) {
        this.id = id;
        this.workingHours = workingHours;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.color = color;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.workingHours);
        dest.writeString(this.title);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.color);
    }

    protected AbsenceModel(Parcel in) {
        this.id = in.readInt();
        this.workingHours = in.readInt();
        this.title = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.color = in.readString();
    }

    public static final Creator<AbsenceModel> CREATOR = new Creator<AbsenceModel>() {
        @Override
        public AbsenceModel createFromParcel(Parcel source) {
            return new AbsenceModel(source);
        }

        @Override
        public AbsenceModel[] newArray(int size) {
            return new AbsenceModel[size];
        }
    };
}
