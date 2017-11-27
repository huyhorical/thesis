package com.kdoctor.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kdoctor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class DisplayTypeDialog extends DialogFragment{
    @BindView(R.id.rb_selected)
    RadioButton rbSelected;
    @BindView(R.id.rb_unselected)
    RadioButton rbUnselected;
    @BindView(R.id.rb_select_all)
    RadioButton rbSelecteAll;
    @BindView(R.id.rg)
    RadioGroup rg;

    OnClickListener onClickListener;
    String type = "ALL";

    @SuppressLint("ValidFragment")
    public DisplayTypeDialog(String type, OnClickListener onClickListener){
        this.onClickListener = onClickListener;
        this.type = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_display_type, null);
        ButterKnife.bind(this, rootView);

        switch (type){
            case "ALL":
                rbSelecteAll.setChecked(true);
                break;
            case "SELECTED":
                rbSelected.setChecked(true);
                break;
            case "UNSELECTED":
                rbUnselected.setChecked(true);
                break;
        }

        builder.setTitle("Chọn loại hiển thị...");
        builder.setView(rootView);

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (rbSelecteAll.isChecked()){
                    onClickListener.onPositiveButtonClickListener("ALL");
                }
                if (rbSelected.isChecked()){
                    onClickListener.onPositiveButtonClickListener("SELECTED");
                }
                if (rbUnselected.isChecked()){
                    onClickListener.onPositiveButtonClickListener("UNSELECTED");
                }
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickListener.onNegativeButtonClickListener();
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
        void onPositiveButtonClickListener(String type);
        void onNegativeButtonClickListener();
    }
}
