package com.kdoctor.fragments.drugs.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.dialogs.DisplayTypeDialog;
import com.kdoctor.dialogs.DrugInfoDialog;
import com.kdoctor.dialogs.SicknessesDialog;
import com.kdoctor.fragments.drugs.view.FragmentDrug;
import com.kdoctor.models.Drug;
import com.kdoctor.sql.DbManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapterDrug extends RecyclerView.Adapter<RecyclerViewAdapterDrug.DrugViewHolder>{
    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    private List<Drug> drugs = new ArrayList<Drug>();

    OnClickListener onClickListener;

    public RecyclerViewAdapterDrug(List<Drug> drugs, OnClickListener onClickListener){
        this.drugs = drugs;
        this.onClickListener = onClickListener;
    }

    public void updateDrugList(List<Drug> drugs){
        this.drugs = drugs;
        notifyDataSetChanged();
    }

    @Override
    public DrugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_fragment_drug, parent, false);
        return new DrugViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DrugViewHolder holder, final int position) {
        final Drug drug = drugs.get(position);
        String urlImage = drug.getImageURL().contains("~") ? RestServices.URL + drug.getImageURL().replace("~/", "") : drug.getImageURL();
        Picasso.with(Kdoctor.getInstance().getAppContext()).load(urlImage).fit().into(holder.ivDrug);
        holder.tvName.setText(drug.getName());
        holder.tvProducer.setText(drug.getProducer());
        holder.tvResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onResearchClickListener(drug);
            }
        });

        List<Drug> drugs = DbManager.getInstance(Kdoctor.getInstance().getAppContext()).getRecords(DbManager.DRUGS, Drug.class);

        boolean isSelected = false;
        if (drugs != null){
            for (Drug d: drugs
                    ) {
                if (d.getId() == drug.getId()){
                    isSelected = d.isSelected();
                }
            }
        }
        holder.cbNote.setChecked(isSelected);

        holder.cbNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onSelectListener(drug, holder.cbNote.isChecked());
            }
        });
    }

    public interface OnClickListener{
        void onResearchClickListener(Drug drug);
        void onSelectListener(Drug drug, boolean isSelected);
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    public class DrugViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_drug)
        ImageView ivDrug;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_producer)
        TextView tvProducer;
        @BindView(R.id.tv_research)
        TextView tvResearch;
        @BindView(R.id.cb_note)
        CheckBox cbNote;

        public DrugViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
