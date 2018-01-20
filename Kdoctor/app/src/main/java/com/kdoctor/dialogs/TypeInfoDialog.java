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
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.fragments.sickness.view.FragmentSickness;
import com.kdoctor.models.Sickness;
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
public class TypeInfoDialog extends DialogFragment{
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.rv_sickness)
    RecyclerView rvSickness;

    String message;
    OnClickListener onClickListener;
    TextAdapter adapter;
    List<Sickness> sicknesses;

    @SuppressLint("ValidFragment")
    public TypeInfoDialog(String message, OnClickListener onClickListener){
        this.onClickListener = onClickListener;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_type_info, null);
        ButterKnife.bind(this, rootView);

        rvSickness.setLayoutManager(new LinearLayoutManager(getActivity()));
        sicknesses = new ArrayList<>();
        adapter = new TextAdapter(sicknesses, new OnItemClickListener() {
            @Override
            public void onItemClickListener(Sickness sickness) {
                //etInfo.setText(sickness.getName());
                SicknessInfoDialog dialog = new SicknessInfoDialog(sickness, new SicknessInfoDialog.OnClickListener() {
                    @Override
                    public void onSelectListener(Sickness sickness, boolean isSelected) {
                        sickness.setSelected(isSelected);
                        DbManager.getInstance(getContext()).selectRecord(DbManager.SICKNESSES, sickness, sickness.getId());
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });
        rvSickness.setAdapter(adapter);
        rvSickness.setHasFixedSize(false);

        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RestServices.getInstance().getServices().searchSickness(s.toString(), new Callback<List<Sickness>>() {
                    @Override
                    public void success(List<Sickness> sicknesses, Response response) {
                        if (sicknesses == null){
                            return;
                        }

                        for (Sickness s:sicknesses
                                ) {
                            try{
                                s.listToLinkRef();
                            }
                            catch (Exception e){

                            }
                        }

                        try {
                            adapter.sicknesses = sicknesses;
                            adapter.notifyDataSetChanged();
                        }
                        catch (Exception e){

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });

        builder.setTitle(message);
        builder.setView(rootView);

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickListener.onPositiveButtonClickListener(etInfo.getText().toString());
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
        void onPositiveButtonClickListener(String value);
        void onNegativeButtonClickListener();
    }

    public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder>{
        public List<Sickness> sicknesses = new ArrayList<Sickness>();
        OnItemClickListener onItemClickListener;

        public TextAdapter(List<Sickness> sicknesses, OnItemClickListener onItemClickListener){
            this.sicknesses = sicknesses;
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_text, parent, false);
            return new TextViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TextViewHolder holder, final int position) {
            Sickness sickness = sicknesses.get(position);
            final String text = sickness.getName();
            holder.tvText.setText(text);
            String urlImage = "";
            try {
                urlImage = sickness.getImageURL().contains("~") ? RestServices.URL + sickness.getImageURL().replace("~/", "") : sickness.getImageURL();
                Picasso.with(TypeInfoDialog.this.getContext()).load(urlImage).fit().into(holder.ivSickness);
            }
            catch (Exception e){

            }
            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(sicknesses.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            try {
                return sicknesses.size();
            }
            catch (Exception e){

            }
            return 0;
        }

        public class TextViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_text)
            TextView tvText;
            @BindView(R.id.ll_item)
            LinearLayout llItem;
            @BindView(R.id.iv_sickness)
            ImageView ivSickness;

            public TextViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClickListener(Sickness sickness);
    }
}
