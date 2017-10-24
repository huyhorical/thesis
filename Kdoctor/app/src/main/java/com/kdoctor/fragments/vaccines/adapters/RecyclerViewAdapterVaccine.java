package com.kdoctor.fragments.vaccines.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;
import com.kdoctor.api.models.Vaccine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapterVaccine extends RecyclerView.Adapter<RecyclerViewAdapterVaccine.VaccineViewHolder>{
    private List<Vaccine> vaccines = new ArrayList<Vaccine>();

    public RecyclerViewAdapterVaccine(List<Vaccine> vaccines){
        this.vaccines = vaccines;
    }

    public void updateVaccineList(List<Vaccine> vaccines){
        this.vaccines = vaccines;
        notifyDataSetChanged();
    }

    @Override
    public VaccineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_fragment_vaccine, parent, false);
        return new VaccineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VaccineViewHolder holder, final int position) {

        final Vaccine vaccine = vaccines.get(position);

        holder.cbActivityDone.setOnCheckedChangeListener(null);
        holder.cbActivityDone.setChecked(vaccine.isSelected());
        holder.cbActivityDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaccine.setSelected(true);
            }
        });

        if (!vaccine.getActivity().equals("") && !vaccine.getActivity().equals(null)){
            holder.tvActivity.setText(vaccine.getActivity());
        }

        String time = getTimeStringByMonths(vaccine.getStartMonth(), IS_START_DATE);
        time += getTimeStringByMonths(vaccine.getEndMonth(), IS_END_DATE);
        holder.tvTime.setText(time);

        String note = (vaccine.getNote() == "") || (vaccine.getNote() == null) ? "" : "Ghi chu: " + vaccine.getNote();
        holder.tvNote.setText(note);

    }

    private static int IS_START_DATE = 0;
    private static int IS_END_DATE = 1;
    String getTimeStringByMonths(int value, int type){
        String result = "";

        int years = value/12;
        int months = value - years*12;

        if (years == 0 && months == 0) {
            result = "luc moi sinh";
        }
        else{
            if (years != 0 && months != 0){
                result = years + " tuoi" + months + " thang";
            }
            if (years == 0){
                result = months + " thang";
            }
            if (months == 0){
                result = years + " tuoi";
            }
        }

        if (type == IS_START_DATE){
            return "Tu " + result;
        }
        else{
            if (years == 0 && months == 0){
                return "";
            }
            return " den " + result;
        }
    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    public class VaccineViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_vaccine_category)
        TextView tvVaccineCategory;
        @BindView(R.id.cb_activity_done)
        CheckBox cbActivityDone;
        @BindView(R.id.tv_activity)
        TextView tvActivity;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_note)
        TextView tvNote;

        public VaccineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
