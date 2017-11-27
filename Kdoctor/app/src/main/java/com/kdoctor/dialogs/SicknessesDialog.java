package com.kdoctor.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.fragments.vaccines.adapters.RecyclerViewAdapterVaccine;
import com.kdoctor.main.view.MainActivity;
import com.kdoctor.models.Question;
import com.kdoctor.models.Sickness;
import com.kdoctor.models.SicknessCategory;
import com.kdoctor.sql.DbManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class SicknessesDialog extends DialogFragment{

    private static SicknessesDialog sicknessesDialog;
    public static SicknessesDialog getCurrentInstance(){
        return sicknessesDialog;
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_sickness)
    RecyclerView rvSickness;

    public SicknessesAdapter getAdapter() {
        return adapter;
    }

    SicknessesAdapter adapter;

    SicknessCategory category;
    List<Sickness> sicknesses;
    OnClickListener onClickListener;

    @SuppressLint("ValidFragment")
    public SicknessesDialog(SicknessCategory category, OnClickListener onClickListener){
        this.category = category;
        this.onClickListener = onClickListener;
    }

    public SicknessesDialog(List<Sickness> sicknesses, OnClickListener onClickListener){
        this.sicknesses = sicknesses;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_sickness_category, null);
        ButterKnife.bind(this, rootView);

        sicknessesDialog = this;

        if (category != null) {
            tvTitle.setText(category.getName());
        }

        rvSickness.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Sickness> sicknesses = new ArrayList<>();

        adapter = new SicknessesAdapter(sicknesses, new OnClickListener() {
            @Override
            public void onResearchClickListener(Sickness sickness) {
                onClickListener.onResearchClickListener(sickness);
            }

            @Override
            public void onNoteClickListener(Sickness sickness, boolean isSelected) {
                sickness.setSelected(isSelected);
                DbManager.getInstance(getContext()).selectRecord(DbManager.SICKNESSES, sickness, sickness.getId());
            }
        });

        rvSickness.setAdapter(adapter);
        rvSickness.setHasFixedSize(false);

        if (this.sicknesses != null || this.sicknesses.size() > 0){
            adapter.sicknesses.clear();
            adapter.sicknesses.addAll(this.sicknesses);
            adapter.notifyDataSetChanged();
        }
        else {
            RestServices.getInstance().getServices().getSicknessCategory(category.getId(), 0, 5, new Callback<List<Sickness>>() {
                @Override
                public void success(List<Sickness> sicknesses, Response response) {
                    adapter.sicknesses.clear();
                    adapter.sicknesses.addAll(sicknesses);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
        if (category != null) {
            builder.setTitle(category.getName());
        }
        else{
            builder.setTitle("Kết quả");
        }
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
        void onResearchClickListener(Sickness sickness);
        void onNoteClickListener(Sickness sickness, boolean isSelected);
    }

    public class SicknessesAdapter extends RecyclerView.Adapter<SicknessesAdapter.SicknessViewHolder>{
        public List<Sickness> getSicknesses() {
            return sicknesses;
        }

        public void setSicknesses(List<Sickness> sicknesses) {
            this.sicknesses = sicknesses;
        }

        private List<Sickness> sicknesses = new ArrayList<Sickness>();
        OnClickListener onClickListener;

        public SicknessesAdapter(List<Sickness> sicknesses, OnClickListener onClickListener){
            this.sicknesses = sicknesses;
            this.onClickListener = onClickListener;
        }

        @Override
        public SicknessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_fragment_sickness, parent, false);
            return new SicknessViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SicknessViewHolder holder, final int position) {
            final Sickness sickness = sicknesses.get(position);

            String urlImage = sickness.getImageURL().contains("~") ? RestServices.URL + sickness.getImageURL().replace("~/", "") : sickness.getImageURL();
            Picasso.with(SicknessesDialog.this.getContext()).load(urlImage).fit().into(holder.ivSickness);
            holder.tvName.setText(sickness.getName());
            holder.tvPrognostic.setText(sickness.getPrognostic());
            holder.tvSummary.setText(sickness.getSummary());
            holder.tvResearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onResearchClickListener(sickness);
                }
            });
            holder.cbNote.setChecked(sickness.isSelected());
            holder.cbNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onClickListener.onNoteClickListener(sickness, b);
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

        public class SicknessViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.ll_item)
            LinearLayout llItem;
            @BindView(R.id.tv_research)
            TextView tvResearch;
            @BindView(R.id.iv_sickness)
            ImageView ivSickness;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_prognostic)
            TextView tvPrognostic;
            @BindView(R.id.tv_summary)
            TextView tvSummary;
            @BindView(R.id.cb_note)
            CheckBox cbNote;

            public SicknessViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
