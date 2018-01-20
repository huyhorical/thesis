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
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.models.Diagnosis;
import com.kdoctor.models.Sickness;
import com.kdoctor.models.SicknessCategory;
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
public class DiagnosisInfoDialog extends DialogFragment{

    @BindView(R.id.rv_diagnosis_item)
    RecyclerView rvDiagnosisItem;
    @BindView(R.id.tv_sickness_name)
    TextView tvSicknessName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_category_des)
    TextView tvCategoryDes;

    ItemAdapter adapter;

    Diagnosis diagnosis;

    @SuppressLint("ValidFragment")
    public DiagnosisInfoDialog(Diagnosis diagnosis){
        this.diagnosis = diagnosis;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_diagnosis_info, null);
        ButterKnife.bind(this, rootView);

        rvDiagnosisItem.setLayoutManager(new LinearLayoutManager(getActivity()));

        diagnosis.init();
        List<Diagnosis.Item> items = diagnosis.getItemList().size() < 1 ? new ArrayList<Diagnosis.Item>() : diagnosis.getItemList();

        adapter = new ItemAdapter(items);

        rvDiagnosisItem.setAdapter(adapter);
        rvDiagnosisItem.setHasFixedSize(false);

        tvSicknessName.setText("Dự đoán: " + diagnosis.getResult());
        tvCategoryDes.setText("(Chuyên mục: "+diagnosis.getCategoryName()+")");
        tvDate.setText("Vào lúc " + diagnosis.getDate());

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestServices.getInstance().getServices().searchSickness(diagnosis.getResult(), new Callback<List<Sickness>>() {
                    @Override
                    public void success(List<Sickness> sicknesses, Response response) {
                        if (sicknesses.size() > 0){
                            try{
                                sicknesses.get(0).listToLinkRef();
                            }
                            catch (Exception e){

                            }
                            SicknessInfoDialog infoDialog = new SicknessInfoDialog(sicknesses.get(0), new SicknessInfoDialog.OnClickListener() {
                                @Override
                                public void onSelectListener(Sickness sickness, boolean isSelected) {
                                    sickness.setSelected(isSelected);
                                    DbManager.getInstance(getContext()).selectRecord(DbManager.SICKNESSES, sickness, sickness.getId());
                                }
                            });
                            infoDialog.show(getFragmentManager(),"");
                        }
                        else{
                            Toast.makeText(Kdoctor.getInstance().getApplicationContext(), "Dữ liệu về bệnh này chưa được cập nhật.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(Kdoctor.getInstance().getApplicationContext(), "Lỗi kết nối.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setTitle("Kdoctor chuẩn đoán");
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

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
        private List<Diagnosis.Item> items = new ArrayList<>();

        public ItemAdapter(List<Diagnosis.Item> items){
            this.items = items;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_diagnosis_item, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            final Diagnosis.Item item = items.get(position);
            holder.tvQuestion.setText("Kdoctor hỏi: " + item.getQuestion());
            holder.tvAnswer.setText(item.getAnswer());
        }

        @Override
        public int getItemCount() {
            try {
                return items.size();
            }
            catch (Exception e){

            }
            return 0;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_question)
            TextView tvQuestion;
            @BindView(R.id.tv_answer)
            TextView tvAnswer;


            public ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
