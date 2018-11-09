package com.marrowlabs.rfid.home.absence_reasons.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marrowlabs.rfid.home.absence_reasons.model.AbsenceModel;
import com.marrowlabs.rfid.rfidandroid.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Matija on 12-Sep-17.
 */

public class AbsenceReasonsAdapter extends RecyclerView.Adapter<AbsenceReasonsAdapter.ViewHolder> {

    public interface AbsenceReasonsAdapterListener {
        void onAbsenceClick(AbsenceModel absenceModel);
    }

    private ArrayList<AbsenceModel> absenceModels = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private AbsenceReasonsAdapterListener listener;
    private String[] cellColor;

    public AbsenceReasonsAdapter(Context context, ArrayList<AbsenceModel> absenceModels, String[] cellColor) {
        this.absenceModels.clear();
        this.absenceModels.addAll(absenceModels);
        this.context = context;
        this.cellColor = cellColor;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_absence_reason, null);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final AbsenceModel absenceModel = absenceModels.get(position);
        viewHolder.absenceName.setText(absenceModel.getTitle());
        viewHolder.absenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAbsenceClick(absenceModel);
            }
        });

        viewHolder.absenceLayout.setBackgroundColor(Color.parseColor(cellColor[position]));

    }


    @Override
    public int getItemCount() {
        int size = absenceModels.size();
        return size;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.absence_name)
        TextView absenceName;
        @Bind(R.id.absence_layout)
        RelativeLayout absenceLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void refreshData(ArrayList<AbsenceModel> absenceModels) {
        this.absenceModels.clear();
        this.absenceModels.addAll(absenceModels);
        notifyDataSetChanged();
    }

    public void setListener(AbsenceReasonsAdapterListener listener) {
        this.listener = listener;
    }

}
