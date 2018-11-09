package com.marrowlabs.rfid.commons.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Matija on 12-Sep-17.
 */

public class PreferenceHelper {

    public static void setUserToken(Context context, String userToken) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREFERENCE_USER_TOKEN, userToken);
        editor.apply();
    }

    public static String getUserToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Constants.PREFERENCE_USER_TOKEN, null);
    }

    public static void deleteToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.PREFERENCE_USER_TOKEN);
        editor.apply();
    }

}
