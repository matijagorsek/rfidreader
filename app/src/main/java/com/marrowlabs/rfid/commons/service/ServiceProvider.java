package com.marrowlabs.rfid.commons.service;

import com.marrowlabs.RfidApp;
import com.marrowlabs.rfid.commons.core.PreferenceHelper;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Matija on 11-Sep-17.
 */

public class ServiceProvider {

    private static RestAdapter restAdapter;
    private static ApiService service;
    private static String BASE_URL = "http://erv.marrowlabs.com/api/";

    public static ApiService getService() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(60, TimeUnit.SECONDS);
        if (PreferenceHelper.getUserToken(RfidApp.getContext()) != null) {
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("X-Authorization", "f70d9ad6-9baa-4419-a371-b1c4fcb6a113");
                    request.addHeader("Authorization", "Bearer" + " " + PreferenceHelper.getUserToken(RfidApp.getContext()));
                }
            };
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setRequestInterceptor(requestInterceptor)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(okHttpClient))
                    .build();
            service = restAdapter.create(ApiService.class);
        }else{
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("X-Authorization", "f70d9ad6-9baa-4419-a371-b1c4fcb6a113");
                }
            };
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setRequestInterceptor(requestInterceptor)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(okHttpClient))
                    .build();
            service = restAdapter.create(ApiService.class);
        }


        return service;
    }
}
