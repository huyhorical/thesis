package com.kdoctor.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.models.Drug;
import com.kdoctor.sql.DbManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class DrugsDialog extends DialogFragment{

    private static DrugsDialog drugsDialog;
    public static DrugsDialog getCurrentInstance(){
        return drugsDialog;
    }

    @BindView(R.id.rv_drug)
    RecyclerView rvDrug;
    @BindView(R.id.tv_alert)
    TextView tvAlert;

    public DrugsAdapter getAdapter() {
        return adapter;
    }

    DrugsAdapter adapter;

    List<Drug> drugs;
    OnClickListener onClickListener;

    @SuppressLint("ValidFragment")
    public DrugsDialog(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public DrugsDialog(List<Drug> drugs, OnClickListener onClickListener){
        this.drugs = drugs;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_drugs, null);
        ButterKnife.bind(this, rootView);

        drugsDialog = this;

        if (this.drugs.size() < 1){
            tvAlert.setVisibility(View.VISIBLE);
        }

        rvDrug.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Drug> drugs = new ArrayList<>();

        adapter = new DrugsAdapter(drugs, onClickListener);
/*
        adapter = new DrugsAdapter(drugs, new OnClickListener() {
            @Override
            public void onResearchClickListener(Drug drug) {
                onClickListener.onResearchClickListener(drug);
            }

            @Override
            public void onNoteClickListener(Drug drug, boolean isSelected) {
                //DbManager.getInstance(getContext()).selectRecord(DbManager.DRUGS, drug, drug.getId());
            }
        });
*/
        rvDrug.setAdapter(adapter);
        rvDrug.setHasFixedSize(false);

        if (this.drugs != null || this.drugs.size() > 0){
            adapter.drugs.clear();
            adapter.drugs.addAll(this.drugs);
            adapter.notifyDataSetChanged();
        }
        else {
            RestServices.getInstance().getServices().getDrugs(0, 5, new Callback<List<Drug>>() {
                @Override
                public void success(List<Drug> drugList, Response response) {
                    adapter.drugs.clear();
                    adapter.drugs.addAll(drugList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
        builder.setTitle("Kết quả");
        builder.setView(rootView);

        builder.setNeutralButton("Kết thúc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface OnClickListener{
        void onResearchClickListener(Drug Drug);
        void onNoteClickListener(Drug Drug, boolean isSelected);
    }

    public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.DrugViewHolder>{
        public List<Drug> getDrugs() {
            return drugs;
        }

        public void setDrugs(List<Drug> drugs) {
            this.drugs = drugs;
        }

        private List<Drug> drugs = new ArrayList<Drug>();
        OnClickListener onClickListener;

        public DrugsAdapter(List<Drug> drugs, OnClickListener onClickListener){
            this.drugs = drugs;
            this.onClickListener = onClickListener;
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
            holder.cbNote.setChecked(drug.isSelected());
            holder.cbNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onNoteClickListener(drug, holder.cbNote.isChecked());
                }
            });
        }

        @Override
        public int getItemCount() {
            try {
                return drugs.size();
            }
            catch (Exception e){

            }
            return 0;
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
}
