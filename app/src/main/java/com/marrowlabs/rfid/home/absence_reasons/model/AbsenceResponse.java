package com.marrowlabs.rfid.home.absence_reasons.model;

import java.util.ArrayList;

/**
 * Created by Matija on 06-Sep-17.
 */

public class AbsenceResponse {

    private ArrayList<AbsenceModel> absenceModelArrayList;

    public ArrayList<AbsenceModel> getAbsenceModelArrayList() {
        return absenceModelArrayList;
    }

    public void setAbsenceModelArrayList(ArrayList<AbsenceModel> absenceModelArrayList) {
        this.absenceModelArrayList = absenceModelArrayList;
    }
}
