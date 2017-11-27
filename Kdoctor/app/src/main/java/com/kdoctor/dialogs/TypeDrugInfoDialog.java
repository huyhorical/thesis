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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.models.Drug;
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
public class TypeDrugInfoDialog extends DialogFragment{
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.rv_sickness)
    RecyclerView rvDrug;

    String message;
    OnClickListener onClickListener;
    TextAdapter adapter;
    List<Drug> drugs;

    @SuppressLint("ValidFragment")
    public TypeDrugInfoDialog(String message, OnClickListener onClickListener){
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

        rvDrug.setLayoutManager(new LinearLayoutManager(getActivity()));
        drugs = new ArrayList<>();
        adapter = new TextAdapter(drugs, new OnItemClickListener() {
            @Override
            public void onItemClickListener(Drug drug) {

                DrugInfoDialog dialog = new DrugInfoDialog(drug, new DrugInfoDialog.OnClickListener() {
                    @Override
                    public void onSelectListener(Drug drug, boolean isSelected) {
                        drug.setSelected(isSelected);
                        DbManager.getInstance(getContext()).selectRecord(DbManager.DRUGS, drug, drug.getId());
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });
        rvDrug.setAdapter(adapter);
        rvDrug.setHasFixedSize(false);

        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RestServices.getInstance().getServices().searchDrug(s.toString(), new Callback<List<Drug>>() {
                    @Override
                    public void success(List<Drug> drugs, Response response) {
                        if (drugs == null){
                            return;
                        }
                        try {
                            adapter.drugs = drugs;
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
        public List<Drug> drugs = new ArrayList<Drug>();
        OnItemClickListener onItemClickListener;

        public TextAdapter(List<Drug> drugs, OnItemClickListener onItemClickListener){
            this.drugs = drugs;
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
            Drug drug = drugs.get(position);
            final String text = drug.getName();
            holder.tvText.setText(text);
            String urlImage = drug.getImageURL().contains("~") ? RestServices.URL + drug.getImageURL().replace("~/", "") : drug.getImageURL();
            Picasso.with(TypeDrugInfoDialog.this.getContext()).load(urlImage).fit().into(holder.ivDrug);
            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(drugs.get(position));
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

        public class TextViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_text)
            TextView tvText;
            @BindView(R.id.ll_item)
            LinearLayout llItem;
            @BindView(R.id.iv_sickness)
            ImageView ivDrug;

            public TextViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClickListener(Drug drug);
    }
}
