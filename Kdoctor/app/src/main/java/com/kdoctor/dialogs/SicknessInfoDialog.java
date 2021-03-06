package com.kdoctor.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.cb_note)
    CheckBox cbNote;
    @BindView(R.id.rv_ref)
    RecyclerView rvRef;
    @BindView(R.id.tv_read_more)
    TextView tvReadMore;


    Sickness sickness;
    OnClickListener onClickListener;

    LinkAdapter adapter;

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

        String urlImage = "";
        if (sickness.getImageURL() != null) {
            urlImage = sickness.getImageURL().contains("~") ? RestServices.URL + sickness.getImageURL().replace("~/", "") : sickness.getImageURL();
        }
        try {
            Picasso.with(SicknessInfoDialog.this.getContext()).load(urlImage).fit().into(ivSickness);
        }
        catch (Exception e){

        }
        tvName.setText("Tên bệnh: " + sickness.getName());
        if (sickness.getName() == null || sickness.getName().equals("")) tvName.setText("Chưa có dữ liệu.");

        tvPrognostic.setText(sickness.getPrognostic());
        if (sickness.getPrognostic() == null || sickness.getPrognostic().equals("")) tvPrognostic.setText("Chưa có dữ liệu.");

        tvSummary.setText(sickness.getSummary());
        if (sickness.getSummary() == null || sickness.getSummary().equals("")) tvSummary.setText("Chưa có dữ liệu.");

        tvTreatment.setText(sickness.getTreatment());
        if (sickness.getTreatment() == null || sickness.getTreatment().equals("")) tvTreatment.setText("Chưa có dữ liệu.");

        tvNote.setText(sickness.getNote());
        if (sickness.getNote() == null || sickness.getNote().equals("")) tvNote.setText("Chưa có dữ liệu.");

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

        if (sickness.getLinkRefs() == null || sickness.getLinkRefs().size() < 1){
            try{
                sickness.linkRefToList();
            }
            catch (Exception e){

            }
        }
        if (sickness.getLinkRefs() != null && sickness.getLinkRefs().size() > 0){
            tvReadMore.setVisibility(View.VISIBLE);
        }
        else {
            tvReadMore.setVisibility(View.GONE);
        }

        rvRef.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LinkAdapter(sickness.getLinkRefs());

        rvRef.setAdapter(adapter);
        rvRef.setHasFixedSize(false);

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
        void onSelectListener(Sickness sickness, boolean isSelected);
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

    public static class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder>{
        public List<Sickness.LinkRef> getLinks() {
            return links;
        }

        public void setLinks(List<Sickness.LinkRef> links) {
            this.links = links;
        }

        private List<Sickness.LinkRef> links = new ArrayList<Sickness.LinkRef>();

        public LinkAdapter(List<Sickness.LinkRef> links){
            this.links = links;
        }

        @Override
        public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_str, parent, false);
            return new LinkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final LinkViewHolder holder, final int position) {
            final String link = links.get(position).getLinkBV();
            final String title = links.get(position).getTenBV();
            holder.tvText.setText(title);
            holder.tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newLink = link;
                    if (!newLink.startsWith("http://") && !newLink.startsWith("https://"))
                        newLink = "http://" + newLink;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newLink));
                    Kdoctor.getInstance().getApplicationContext().startActivity(browserIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            try {
                return links.size();
            }
            catch (Exception e){

            }
            return 0;
        }

        public class LinkViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_text)
            TextView tvText;

            public LinkViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
