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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.fragments.vaccines.adapters.RecyclerViewAdapterVaccine;
import com.kdoctor.models.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class SicknessQuestionDialog extends DialogFragment{
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.rv_answers)
    RecyclerView rvAnswers;

    AnswersAdapter adapter;

    Question question;
    OnAnswerListener onAnswerListener;

    @SuppressLint("ValidFragment")
    public SicknessQuestionDialog(Question question, OnAnswerListener onAnswerListener){
        this.question = question;
        this.onAnswerListener = onAnswerListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_sickness_question, null);
        ButterKnife.bind(this, rootView);

        tvQuestion.setText(question.getQuestionContaint());

        rvAnswers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AnswersAdapter(question.getAnswers(), new AnswersAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String answer) {
                String input = question.getQuestionId()+"*"+question.getQuestionTitle()+"="+answer;
                onAnswerListener.onAnswerListener(input);
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
        void onAnswerListener(String value);
    }

    public static class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder>{
        private List<String> answers = new ArrayList<String>();
        OnItemClickListener onItemClickListener;

        public AnswersAdapter(List<String> answers, OnItemClickListener onItemClickListener){
            this.answers = answers;
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_answer, parent, false);
            return new AnswerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AnswerViewHolder holder, final int position) {
            final String answer = answers.get(position);
            holder.tvAnswer.setText(answer);
            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(answer);
                }
            });
        }

        @Override
        public int getItemCount() {
            try {
                return answers.size();
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
            void onItemClickListener(String answer);
        }
    }

}
