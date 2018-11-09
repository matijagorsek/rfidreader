package com.marrowlabs.rfid;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.crashlytics.android.Crashlytics;
import com.marrowlabs.rfid.base.BaseActivity;
import com.marrowlabs.rfid.home.HomeFragment;
import com.marrowlabs.rfid.rfidandroid.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        addFragment(R.id.fragment_container, HomeFragment.newInstance(), HomeFragment.TAG, true, null);
    }


}
