package com.marrowlabs.rfid.home.absence_reasons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marrowlabs.rfid.base.BaseFragment;
import com.marrowlabs.rfid.commons.core.Constants;
import com.marrowlabs.rfid.commons.service.RfidService;
import com.marrowlabs.rfid.commons.views.RfidRecyclerView;
import com.marrowlabs.rfid.home.absence_reasons.adapter.AbsenceReasonsAdapter;
import com.marrowlabs.rfid.home.absence_reasons.model.AbsenceModel;
import com.marrowlabs.rfid.home.absence_reasons.model.AddAbsenceReasonResponse;
import com.marrowlabs.rfid.rfidandroid.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matija on 12-Sep-17.
 */

public class AbsenceReasonsFragment extends BaseFragment implements AbsenceReasonsAdapter.AbsenceReasonsAdapterListener {

    public static final String TAG = AbsenceReasonsFragment.class.getName();

    public static AbsenceReasonsFragment newInstance(String id, ArrayList<AbsenceModel> absenceModels) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CHECK_IN_OUT_ID, id);
        bundle.putParcelableArrayList(Constants.ARRAY_LIST_ABSENCE_MODEL, absenceModels);
        AbsenceReasonsFragment absenceReasonsFragment = new AbsenceReasonsFragment();
        absenceReasonsFragment.setArguments(bundle);
        return absenceReasonsFragment;
    }


    @Bind(R.id.absence_list)
    RfidRecyclerView absenceListRecyclerView;

    private AbsenceReasonsAdapter mAdapter;
    private ArrayList<AbsenceModel> absenceModels = new ArrayList<>();
    private String checkInOutId;
    private String[] gridColor = {
            "#E74C3C",
            "#9B59B6",
            "#5499C7",
            "#48C9B0",
            "#45B39D",
            "#52BE80",
            "#58D68D",
            "#F4D03F",
            "#F5B041",
            "#EB984E",
            "#D35400",
            "#BDC3C7",
            "#FFFF00",
            "#FFF0F5",
            "#EE82EE",
            "#DC143C",
            "#C0C0C0"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.checkInOutId = getArguments().getString(Constants.CHECK_IN_OUT_ID);
        this.absenceModels = getArguments().getParcelableArrayList(Constants.ARRAY_LIST_ABSENCE_MODEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_absence_reason, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void setupRecyclerView() {
        mAdapter = new AbsenceReasonsAdapter(getActivity(), absenceModels, gridColor);
        mAdapter.setListener(this);
        absenceListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        absenceListRecyclerView.setHasFixedSize(true);
        absenceListRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onAbsenceClick(AbsenceModel absenceModel) {
        RfidService.addAbsenceReason(checkInOutId, String.valueOf(absenceModel.getId()));
    }

    @Subscribe
    public void onAbsenceAddSuccess(AddAbsenceReasonResponse addAbsenceReasonResponse) {
        Toast.makeText(getActivity(), "Uspješno dodan razlog izlaska", Toast.LENGTH_LONG).show();

        getActivity().onBackPressed();
    }
}
