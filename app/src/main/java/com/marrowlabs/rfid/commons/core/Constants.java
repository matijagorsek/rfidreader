package com.marrowlabs.rfid.commons.core;

/**
 * Created by Matija on 06-Sep-17.
 */

public class Constants {

    /*Retrofit Calls*/
    public static final String API_AUTH_ADMIN = "/users/auth_admin/";
    public static final String API_CREATE_USER = "/users";
    public static final String API_CHECK_IN_OUT = "/records/checkinout";
    public static final String API_ADD_ABSENCE_REASON = "/records/add_absence_reason";
    public static final String API_GET_ABSENCE_REASONS = "/records/absence_reasons";


    /*Model Parcelable*/
    public static final String CHECK_IN_OUT_ID = "check_in_out_id";
    public static final String ARRAY_LIST_ABSENCE_MODEL = "array_list_absence_model";
    public static final String PREFERENCE_USER_TOKEN = "user_token";


}
