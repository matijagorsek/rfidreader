package com.marrowlabs;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Matija on 12-Sep-17.
 */

public class RfidApp extends Application {

    private static Context mContext;
    private static RfidApp instance;


    public static RfidApp getInstance() {
        if (instance == null) {
            instance = new RfidApp();
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


    public static void resetApp(Context context) {
        context = null;
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
