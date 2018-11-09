package com.marrowlabs.rfid.commons.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matija on 11-Sep-17.
 */

public class AuthAdminResponse implements Parcelable {

    private int id;
    private String email;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("is_active")
    private int isActive;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("role")
    private RoleModel roleModel;
    @SerializedName("card")
    private CardModel cardModel;

    public AuthAdminResponse(int id, String email, String firstName, String lastName, int isActive, String accessToken, RoleModel roleModel, CardModel cardModel) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.accessToken = accessToken;
        this.roleModel = roleModel;
        this.cardModel = cardModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public RoleModel getRoleModel() {
        return roleModel;
    }

    public void setRoleModel(RoleModel roleModel) {
        this.roleModel = roleModel;
    }

    public CardModel getCardModel() {
        return cardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.cardModel = cardModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.email);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeInt(this.isActive);
        dest.writeString(this.accessToken);
        dest.writeParcelable(this.roleModel, flags);
        dest.writeParcelable(this.cardModel, flags);
    }

    protected AuthAdminResponse(Parcel in) {
        this.id = in.readInt();
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.isActive = in.readInt();
        this.accessToken = in.readString();
        this.roleModel = in.readParcelable(RoleModel.class.getClassLoader());
        this.cardModel = in.readParcelable(CardModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<AuthAdminResponse> CREATOR = new Parcelable.Creator<AuthAdminResponse>() {
        @Override
        public AuthAdminResponse createFromParcel(Parcel source) {
            return new AuthAdminResponse(source);
        }

        @Override
        public AuthAdminResponse[] newArray(int size) {
            return new AuthAdminResponse[size];
        }
    };
}
