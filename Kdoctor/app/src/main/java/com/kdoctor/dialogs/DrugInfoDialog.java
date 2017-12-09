package com.kdoctor.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.fragments.drugs.view.FragmentDrug;
import com.kdoctor.fragments.sickness.view.FragmentSickness;
import com.kdoctor.main.view.MainActivity;
import com.kdoctor.models.Drug;
import com.kdoctor.models.Sickness;
import com.kdoctor.sql.DbManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class DrugInfoDialog extends DialogFragment{
    @BindView(R.id.iv_drug)
    ImageView ivDrug;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_producer)
    TextView tvProducer;
    @BindView(R.id.tv_component)
    TextView tvComponent;
    @BindView(R.id.tv_uses)
    TextView tvUses;
    @BindView(R.id.tv_guide)
    TextView tvGuide;
    @BindView(R.id.tv_caution)
    TextView tvCaution;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.cb_note)
    CheckBox cbNote;

    Drug drug;
    OnClickListener onClickListener;

    @SuppressLint("ValidFragment")
    public DrugInfoDialog(Drug drug, OnClickListener onClickListener){
        this.drug = drug;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_drug_info, null);
        ButterKnife.bind(this, rootView);

        String urlImage = drug.getImageURL().contains("~") ? RestServices.URL + drug.getImageURL().replace("~/", "") : drug.getImageURL();
        Picasso.with(DrugInfoDialog.this.getContext()).load(urlImage).fit().into(ivDrug);
        tvName.setText(drug.getName());
        if (drug.getName() == null || drug.getName().equals("")) tvName.setText("Chưa có dữ liệu.");

        tvProducer.setText(drug.getProducer());
        if (drug.getProducer() == null || drug.getProducer().equals("")) tvProducer.setText("Chưa có dữ liệu.");

        tvComponent.setText(drug.getComponent());
        if (drug.getComponent() == null || drug.getComponent().equals("")) tvComponent.setText("Chưa có dữ liệu.");

        tvUses.setText(drug.getUses());
        if (drug.getUses() == null || drug.getUses().equals("")) tvUses.setText("Chưa có dữ liệu.");

        tvGuide.setText(drug.getGuide());
        if (drug.getGuide() == null || drug.getGuide().equals("")) tvGuide.setText("Chưa có dữ liệu.");

        tvCaution.setText(drug.getCaution());
        if (drug.getCaution() == null || drug.getCaution().equals("")) tvCaution.setText("Chưa có dữ liệu.");

        tvNote.setText(drug.getNote());
        if (drug.getNote() == null || drug.getNote().equals("")) tvNote.setText("Chưa có dữ liệu.");

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
        cbNote.setChecked(isSelected);

        cbNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onClickListener.onSelectListener(drug, b);
            }
        });

        builder.setTitle("Thông tin");
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
        void onSelectListener(Drug drug, boolean isSeleted);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).getFragmentDrug().getAdapter().notifyDataSetChanged();

        ((MainActivity)getActivity()).getFragmentSickness().getSicknessesAdapter().notifyDataSetChanged();

        try{
            List<Drug> drugs = new ArrayList<>();

            List<Object> objects = DbManager.getInstance(getContext()).getRecords(DbManager.DRUGS, Drug.class);
            for (Object o:objects
                    ) {
                Drug drug = (Drug)o;
                if (drug.isSelected()){
                    drugs.add(drug);
                }
            }
            DrugsDialog.getCurrentInstance().getAdapter().setDrugs(drugs);
            DrugsDialog.getCurrentInstance().getAdapter().notifyDataSetChanged();
        }
        catch (Exception e){

        }
    }
}
