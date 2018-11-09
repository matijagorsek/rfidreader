package com.marrowlabs.rfid.commons.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matija on 11-Sep-17.
 */

public class CardModel implements Parcelable {

    private int id;
    @SerializedName("rf_id")
    private String rfId;
    private String code;
    @SerializedName("is_present")
    private int isPresent;

    public CardModel(int id, String rfId, String code, int isPresent) {
        this.id = id;
        this.rfId = rfId;
        this.code = code;
        this.isPresent = isPresent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRfId() {
        return rfId;
    }

    public void setRfId(String rfId) {
        this.rfId = rfId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(int isPresent) {
        this.isPresent = isPresent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.rfId);
        dest.writeString(this.code);
        dest.writeInt(this.isPresent);
    }

    protected CardModel(Parcel in) {
        this.id = in.readInt();
        this.rfId = in.readString();
        this.code = in.readString();
        this.isPresent = in.readInt();
    }

    public static final Parcelable.Creator<CardModel> CREATOR = new Parcelable.Creator<CardModel>() {
        @Override
        public CardModel createFromParcel(Parcel source) {
            return new CardModel(source);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };
}
