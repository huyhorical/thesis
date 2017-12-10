package com.kdoctor.fragments.vaccines.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;

import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.dialogs.SicknessesDialog;
import com.kdoctor.models.Function;
import com.kdoctor.models.Vaccine;
import com.kdoctor.services.VaccineService;
import com.kdoctor.sql.DbManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapterVaccine extends RecyclerView.Adapter<RecyclerViewAdapterVaccine.VaccineViewHolder>{
    public List<Vaccine> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<Vaccine> vaccines) {
        this.vaccines = vaccines;
    }

    private List<Vaccine> vaccines = new ArrayList<Vaccine>();
    List<Function> functions;

    OnClickListener onClickListener;

    public RecyclerViewAdapterVaccine(List<Vaccine> vaccines, OnClickListener onClickListener){
        this.vaccines = vaccines;
        this.functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
        this.onClickListener = onClickListener;
    }

    public void updateVaccineList(List<Vaccine> vaccines){
        this.vaccines = vaccines;
        sort();
        notifyDataSetChanged();
    }

    @Override
    public VaccineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_fragment_vaccine, parent, false);
        return new VaccineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VaccineViewHolder holder, final int position) {
        final Vaccine vaccine = vaccines.get(position);

        if (getCategoryName(position).equals("")){
            holder.tvVaccineCategory.setVisibility(View.GONE);
        }
        else{
            holder.tvVaccineCategory.setVisibility(View.VISIBLE);
        }
        holder.tvVaccineCategory.setText(getCategoryName(position));
        holder.cbActivityDone.setOnCheckedChangeListener(null);
        holder.cbActivityDone.setChecked(vaccine.isSelected());

        if (vaccine.isSelected()){
            holder.llSelected.setVisibility(View.VISIBLE);
        }
        else{
            holder.llSelected.setVisibility(View.GONE);
        }

        holder.cbActivityDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vaccine element =  vaccines.get(position);
                if (((CheckBox)view).isChecked()) {
                    element.setSelected(true);
                    element.setRead(false);
                    DbManager.getInstance(null).selectRecord(DbManager.VACCINES, element, element.getId());
                    holder.llSelected.setVisibility(View.VISIBLE);
                }
                else{
                    onClickListener.onUnSelectedClickListener(vaccines.get(position), holder.cbActivityDone);
                }

                List<Vaccine> vaccines = DbManager.getInstance(Kdoctor.getInstance().getApplicationContext()).getRecords(DbManager.VACCINES, Vaccine.class);
                VaccineService.setVaccines(vaccines);
            }
        });

        if (!vaccine.getActivity().equals("") && !vaccine.getActivity().equals(null)){
            holder.tvActivity.setText(vaccine.getActivity());
        }

        String time = getTimeStringByMonths(vaccine.getStartMonth(), IS_START_MONTH);
        String temp = getTimeStringByMonths(vaccine.getEndMonth(), IS_END_MONTH);
        if (temp.equals("") && vaccine.getStartMonth() > 0){
            time += " trở lên";
        }
        else {
            time += temp;
        }
        holder.tvTime.setText(time);

        if (vaccine.getMessage() != null && !vaccine.getMessage().equals("")){
            holder.tvMessage.setText(vaccine.getMessage());
        }
        else{
            holder.tvMessage.setText("Chưa có.");
        }

        if (vaccine.getAlarmDate() != null && !vaccine.getAlarmDate().equals("")){
            holder.tvDate.setText(vaccine.getAlarmDate());
        }
        else{
            holder.tvDate.setText("Chưa thiết lập.");
        }

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onEditClickListener(vaccine.getId());
            }
        });

        String note = ((vaccine.getNote() == null || vaccine.getNote().equals(""))) ? "" : "Lưu ý: " + vaccine.getNote();
        if (note.equals("")){
            holder.tvNote.setVisibility(View.GONE);
        }
        else{
            holder.tvNote.setVisibility(View.VISIBLE);
        }
        holder.tvNote.setText(note);

    }

    public void sort(){
        for (int i=0; i<vaccines.size()-1; i++){
            for (int j=i+1; j<vaccines.size(); j++){
                if (vaccines.get(j).getStartMonth() < vaccines.get(i).getStartMonth()){
                    Vaccine temp = vaccines.get(i);
                    vaccines.set(i, vaccines.get(j));
                    vaccines.set(j, temp);
                }
                else if (vaccines.get(j).getStartMonth() == vaccines.get(i).getStartMonth()){
                    if (vaccines.get(j).getEndMonth() < vaccines.get(i).getEndMonth()){
                        Vaccine temp = vaccines.get(i);
                        vaccines.set(i, vaccines.get(j));
                        vaccines.set(j, temp);
                    }
                }
            }
        }
    }

    String getCategoryName(int position){
        if (position == 0){
            return getTimeStringByMonths(vaccines.get(position).getStartMonth(), IS_START_MONTH);
        }

        Vaccine previousElement = vaccines.get(position - 1);
        Vaccine currentElement = vaccines.get(position);

        if (currentElement.getStartMonth() == previousElement.getStartMonth()){
            return "";
        }
        else{
            return getTimeStringByMonths(vaccines.get(position).getStartMonth(), IS_START_MONTH);
        }
    }

    public static final int IS_START_MONTH = 0;
    public static final int IS_END_MONTH = 1;
    public static String getTimeStringByMonths(int value, int type){
        String result = "";

        int years = value/12;
        int months = value - years*12;

        if (years == 0 && months == 0) {
            result = "lúc mới sinh";
        }
        else{
            if (years != 0 && months != 0){
                result = years + " tuổi " + months + " tháng";
            }
            if (years == 0){
                result = months + " tháng";
            }
            if (months == 0){
                result = years + " tuổi";
            }
        }

        if (type == IS_START_MONTH){
            return "Từ " + result;
        }
        else{
            if (years == 0 && months == 0){
                return "";
            }
            return " đến " + result;
        }
    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    public class VaccineViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_vaccine_category)
        TextView tvVaccineCategory;
        @BindView(R.id.cb_activity_done)
        CheckBox cbActivityDone;
        @BindView(R.id.tv_activity)
        TextView tvActivity;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_note)
        TextView tvNote;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.ll_selected)
        LinearLayout llSelected;

        public VaccineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener{
        void onEditClickListener(int id);
        void onUnSelectedClickListener(Vaccine vaccine, CheckBox checkBox);
    }
}
