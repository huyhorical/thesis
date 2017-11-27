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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.fragments.sickness.view.FragmentSickness;
import com.kdoctor.main.view.MainActivity;
import com.kdoctor.models.Sickness;
import com.kdoctor.models.SicknessCategory;
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
public class SicknessInfoDialog extends DialogFragment{
    @BindView(R.id.iv_sickness)
    ImageView ivSickness;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_prognostic)
    TextView tvPrognostic;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.tv_treatment)
    TextView tvTreatment;
    @BindView(R.id.cb_note)
    CheckBox cbNote;

    Sickness sickness;
    OnClickListener onClickListener;

    @SuppressLint("ValidFragment")
    public SicknessInfoDialog(Sickness sickness, OnClickListener onClickListener){
        this.sickness = sickness;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_sickness_info, null);
        ButterKnife.bind(this, rootView);

        String urlImage = sickness.getImageURL().contains("~") ? RestServices.URL + sickness.getImageURL().replace("~/", "") : sickness.getImageURL();
        Picasso.with(SicknessInfoDialog.this.getContext()).load(urlImage).fit().into(ivSickness);
        tvName.setText("Tên bệnh: " + sickness.getName());
        tvPrognostic.setText("Biểu hiện: " + sickness.getPrognostic());
        tvSummary.setText("Tổng quan: " + sickness.getSummary());
        tvTreatment.setText("Điều trị: " + sickness.getTreatment());

        List<Sickness> sicknesses = DbManager.getInstance(Kdoctor.getInstance().getAppContext()).getRecords(DbManager.SICKNESSES, Sickness.class);
        boolean isSelected = false;
        if (sicknesses != null){
            for (Sickness s: sicknesses
                    ) {
                if (s.getId() == sickness.getId()){
                    isSelected = s.isSelected();
                }
            }
        }
        cbNote.setChecked(isSelected);

        cbNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onClickListener.onSelectListener(sickness, b);
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
        void onSelectListener(Sickness sickness, boolean isSeleted);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).getFragmentSickness().getSicknessesAdapter().notifyDataSetChanged();

        try{
            List<Sickness> sicknesses = new ArrayList<>();

            List<Object> objects = DbManager.getInstance(getContext()).getRecords(DbManager.SICKNESSES, Sickness.class);
            for (Object o:objects
                 ) {
                Sickness sickness = (Sickness)o;
                if (sickness.isSelected()){
                    sicknesses.add(sickness);
                }
            }
            SicknessesDialog.getCurrentInstance().getAdapter().setSicknesses(sicknesses);
            SicknessesDialog.getCurrentInstance().getAdapter().notifyDataSetChanged();
        }
        catch (Exception e){

        }
    }
}
