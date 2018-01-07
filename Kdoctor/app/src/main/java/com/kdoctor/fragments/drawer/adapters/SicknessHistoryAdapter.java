package com.kdoctor.fragments.drawer.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.models.Diagnosis;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INI\huy.trinh on 23/11/2017.
 */

public class SicknessHistoryAdapter extends RecyclerView.Adapter<SicknessHistoryAdapter.DiagnosisViewHolder>{
    OnClickListener listener;

    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        for (int i = 0; i < diagnoses.size() / 2; i++){
            Diagnosis temp = diagnoses.get(i);
            diagnoses.set(i, diagnoses.get(diagnoses.size()-1-i));
            diagnoses.set(diagnoses.size()-1-i, temp);
        }
        this.diagnoses = diagnoses;
    }

    private List<Diagnosis> diagnoses = new ArrayList<Diagnosis>();

    public SicknessHistoryAdapter(List<Diagnosis> diagnoses, OnClickListener listener){
        this.diagnoses = diagnoses;
        this.listener = listener;
    }

    @Override
    public DiagnosisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_diagnosis, parent, false);
        return new DiagnosisViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiagnosisViewHolder holder, final int position) {
        final Diagnosis diagnosis = diagnoses.get(position);
        holder.tvSicknessName.setText(diagnosis.getResult());
        holder.tvDate.setText("Vào lúc " + diagnosis.getDate());
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMoreClickListener(diagnosis);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diagnoses.size();
    }

    public class DiagnosisViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_sickness_name)
        TextView tvSicknessName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_more)
        TextView tvMore;

        public DiagnosisViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener{
        void onMoreClickListener(Diagnosis diagnosis);
    }
}
