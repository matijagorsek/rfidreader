package com.marrowlabs.rfid.base;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Matija on 30-Aug-17.
 */

public class BaseActivity extends AppCompatActivity {

    protected void addFragment(int layoutId, @NonNull BaseFragment fragment, String tag, boolean addToBackStack, String backstackName) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layoutId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(backstackName);
        }
        transaction.commit();
    }
}
