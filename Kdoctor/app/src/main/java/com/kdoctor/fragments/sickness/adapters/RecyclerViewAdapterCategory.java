package com.kdoctor.fragments.sickness.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.custom.RoundRectCornerImageView;
import com.kdoctor.dialogs.SicknessQuestionDialog;
import com.kdoctor.models.SicknessCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        final SicknessCategory category = categories.get(position);
        holder.tvAnswer.setText(category.getName());
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClickListener(categories.get(position));
            }
        });
        String urlImage = category.getImageURL().contains("~") ? RestServices.URL + category.getImageURL().replace("~/", "") : category.getImageURL();
        Picasso.with(Kdoctor.getInstance().getAppContext()).load(urlImage).fit().into(holder.ivCategory);
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
        @BindView(R.id.iv_category)
        RoundRectCornerImageView ivCategory;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(SicknessCategory category);
    }
}
