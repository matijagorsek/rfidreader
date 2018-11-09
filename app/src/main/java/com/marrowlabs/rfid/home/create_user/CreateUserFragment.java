package com.marrowlabs.rfid.home.create_user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marrowlabs.rfid.base.BaseFragment;
import com.marrowlabs.rfid.commons.model.UserModel;
import com.marrowlabs.rfid.commons.service.RfidService;
import com.marrowlabs.rfid.home.HomeFragment;
import com.marrowlabs.rfid.home.absence_reasons.adapter.AbsenceReasonsAdapter;
import com.marrowlabs.rfid.home.absence_reasons.fragment.AbsenceReasonsFragment;
import com.marrowlabs.rfid.home.absence_reasons.model.AbsenceModel;
import com.marrowlabs.rfid.rfidandroid.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matija on 12-Sep-17.
 */

public class CreateUserFragment extends BaseFragment {

    public static final String TAG = CreateUserFragment.class.getName();

    public static CreateUserFragment newInstance() {
        CreateUserFragment createUserFragment = new CreateUserFragment();
        return createUserFragment;
    }


    @Bind(R.id.name_input)
    EditText inputName;
    @Bind(R.id.surname_input)
    EditText surnameInput;
    @Bind(R.id.card_input)
    EditText cardInput;
    @Bind(R.id.confirm_button)
    Button confirmButton;

    private String name, surname, cardNumber;
    private AbsenceReasonsAdapter mAdapter;
    private ArrayList<AbsenceModel> absenceModels = new ArrayList<>();
    private String checkInOutId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.confirm_button)
    public void onConfirmButtonClicked() {
        boolean errorFound = false;
        name = inputName.getText().toString();
        surname = surnameInput.getText().toString();
        cardNumber = cardInput.getText().toString();
        if (TextUtils.isEmpty(name)) {
            inputName.setError(getString(R.string.required));
            errorFound = true;
        }
        if (TextUtils.isEmpty(surname)) {
            inputName.setError(getString(R.string.required));
            errorFound = true;
        }
        if (TextUtils.isEmpty(cardNumber)) {
            cardInput.setError(getString(R.string.required));
            errorFound = true;
        }
        if (errorFound) {
            return;
        } else {
            RfidService.createUser(cardNumber, name, surname);
        }
    }

    @Subscribe
    public void onCreateUserSuccess(UserModel userModel) {
        Log.d("Test", userModel.toString());
        Toast.makeText(getActivity(),getString(R.string.user_succesfully_added),Toast.LENGTH_LONG).show();

        getActivity().onBackPressed();

    }

}