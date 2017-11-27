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

/**
 * Created by INI\huy.trinh on 25/10/2017.
 */

@SuppressLint("ValidFragment")
public class QuestionDialog extends DialogFragment {
    OnOneChoiceSelection onOneChoiceSelection;
    OnTwoChoicesSelection onTwoChoicesSelection;

    String title = "";
    String message = "";
    String pString = "Hoàn tất";

    public QuestionDialog(String title, String message, OnOneChoiceSelection onOneChoiceSelection){
        this.title = title;
        this.message = message;
        this.onOneChoiceSelection = onOneChoiceSelection;
    }

    public QuestionDialog(String title, String message, OnTwoChoicesSelection onTwoChoicesSelection){
        this.title = title;
        this.message = message;
        this.onTwoChoicesSelection = onTwoChoicesSelection;
    }

    public QuestionDialog(String title, String message, String pString, OnTwoChoicesSelection onTwoChoicesSelection){
        this.title = title;
        this.message = message;
        this.pString = pString;
        this.onTwoChoicesSelection = onTwoChoicesSelection;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);

        builder.setMessage(message);

        if (onOneChoiceSelection != null){
            builder.setNeutralButton("Kết thúc", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onOneChoiceSelection.onButtonClick();
                }
            });
        }

        if (onTwoChoicesSelection != null){
            builder.setPositiveButton(pString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onTwoChoicesSelection.onPositiveButtonClick();
                }
            });

            builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onTwoChoicesSelection.onNegativeButtonClick();
                }
            });
        }
        return builder.create();
    }

    public interface OnOneChoiceSelection{
        void onButtonClick();
    }

    public interface OnTwoChoicesSelection{
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
