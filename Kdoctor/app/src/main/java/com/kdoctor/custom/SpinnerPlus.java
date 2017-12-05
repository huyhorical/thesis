package com.kdoctor.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Spinner;

/**
 * Created by Huy on 12/4/2017.
 */

public class SpinnerPlus extends Spinner {
    AdapterView.OnItemSelectedListener listener;

    public SpinnerPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(this, getSelectedView(), position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }
}