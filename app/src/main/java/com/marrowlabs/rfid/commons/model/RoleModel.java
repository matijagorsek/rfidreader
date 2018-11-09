package com.marrowlabs.rfid.commons.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Matija on 11-Sep-17.
 */

public class RoleModel implements Parcelable {

    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RoleModel(int id, String title) {
        this.id = id;
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
    }

    protected RoleModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<RoleModel> CREATOR = new Parcelable.Creator<RoleModel>() {
        @Override
        public RoleModel createFromParcel(Parcel source) {
            return new RoleModel(source);
        }

        @Override
        public RoleModel[] newArray(int size) {
            return new RoleModel[size];
        }
    };
}
