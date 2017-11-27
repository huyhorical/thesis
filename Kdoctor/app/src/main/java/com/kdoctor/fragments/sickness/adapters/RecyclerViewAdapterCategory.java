package com.kdoctor.fragments.sickness.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.dialogs.SicknessQuestionDialog;
import com.kdoctor.models.SicknessCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 11/5/2017.
 */

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.AnswerViewHolder>{
    private List<SicknessCategory> categories = new ArrayList<SicknessCategory>();
    OnItemClickListener onItemClickListener;

    public RecyclerViewAdapterCategory(List<SicknessCategory> categories,OnItemClickListener onItemClickListener){
        this.categories = categories;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_category, parent, false);
        return new AnswerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AnswerViewHolder holder, final int position) {
        final String name = categories.get(position).getName();
        holder.tvAnswer.setText(name);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClickListener(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return categories.size();
        }
        catch (Exception e){

        }
        return 0;
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.tv_answer)
        TextView tvAnswer;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(SicknessCategory category);
    }
}
