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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.models.SicknessCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class GuideDialog extends DialogFragment{
    @BindView(R.id.sv_tab1)
    ScrollView svTab1;
    @BindView(R.id.sv_tab2)
    ScrollView svTab2;
    @BindView(R.id.sv_tab3)
    ScrollView svTab3;
    @BindView(R.id.iv_tab)
    ImageView ivTab;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_guide, null);
        ButterKnife.bind(this, rootView);

        //builder.setTitle("Hướng dẫn...");
        builder.setView(rootView);

        builder.setPositiveButton("Đã hiểu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ivTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (svTab1.getVisibility() == View.VISIBLE && svTab2.getVisibility() == View.GONE) {
                    svTab1.setVisibility(View.GONE);
                    svTab2.setVisibility(View.VISIBLE);
                    tvTitle.setText("Mục 2: Các loại thuốc");
                }
                else if (svTab2.getVisibility() == View.VISIBLE && svTab3.getVisibility() == View.GONE) {
                    svTab2.setVisibility(View.GONE);
                    svTab3.setVisibility(View.VISIBLE);
                    tvTitle.setText("Mục 3: Lịch tiêm chủng");
                    ivTab.setVisibility(View.GONE);
                }
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        //super.onDismiss(dialog);
    }
}
