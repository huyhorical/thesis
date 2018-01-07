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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.models.Question;
import com.kdoctor.models.SicknessCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class CategoryDialog extends DialogFragment{
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.rv_answers)
    RecyclerView rvAnswers;

    CategoriesAdapter adapter;

    List<SicknessCategory> categories;
    OnAnswerListener onAnswerListener;

    @SuppressLint("ValidFragment")
    public CategoryDialog(List<SicknessCategory> categories, OnAnswerListener onAnswerListener){
        this.categories = categories;
        this.onAnswerListener = onAnswerListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_sickness_question, null);
        ButterKnife.bind(this, rootView);

        tvQuestion.setText("Triệu chứng mà trẻ gặp như thế nào?");

        rvAnswers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CategoriesAdapter(categories, new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(SicknessCategory answer) {
                onAnswerListener.onAnswerListener(answer);
                dismiss();
            }
        });

        rvAnswers.setAdapter(adapter);
        rvAnswers.setHasFixedSize(false);

        builder.setTitle("Chuẩn đoán...");
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

    public interface OnAnswerListener{
        void onAnswerListener(SicknessCategory answer);
    }

    public static class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>{
        private List<SicknessCategory> categories = new ArrayList<SicknessCategory>();
        OnItemClickListener onItemClickListener;

        public CategoriesAdapter(List<SicknessCategory> categories, OnItemClickListener onItemClickListener){
            this.categories = categories;
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_answer, parent, false);
            return new CategoryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
            final String answer = categories.get(position).getDescription();
            holder.tvAnswer.setText(answer);
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

        public class CategoryViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.ll_item)
            LinearLayout llItem;
            @BindView(R.id.tv_answer)
            TextView tvAnswer;

            public CategoryViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public interface OnItemClickListener {
            void onItemClickListener(SicknessCategory answer);
        }
    }

}
