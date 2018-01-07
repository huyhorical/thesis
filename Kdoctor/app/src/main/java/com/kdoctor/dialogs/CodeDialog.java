package com.kdoctor.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdoctor.R;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.main.view.MainActivity;
import com.kdoctor.models.Code;
import com.kdoctor.models.SicknessCategory;
import com.kdoctor.sql.DbManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class CodeDialog extends DialogFragment{
    private static CodeDialog codeDialog;
    public static CodeDialog getCurrentIntance(){
        return codeDialog;
    }

    @BindView(R.id.rv_codes)
    RecyclerView rvCodes;

    public CodesAdapter getAdapter() {
        return adapter;
    }

    CodesAdapter adapter;

    List<Code> codes;
    OnClickListener onClickListener;

    @SuppressLint("ValidFragment")
    public CodeDialog(List<Code> codes, OnClickListener onClickListener){
        this.codes = codes;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_code_list, null);
        ButterKnife.bind(this, rootView);

        rvCodes.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CodesAdapter(codes, new CodesAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(Code code) {
                onClickListener.onMoreClickListener(code);
                dismiss();
            }

            @Override
            public void onDeleteClickListener(Code code, CodesAdapter adapter) {
                onClickListener.onDeleteClickListener(code, adapter);
            }
        });

        rvCodes.setAdapter(adapter);
        rvCodes.setHasFixedSize(false);

        builder.setTitle("Danh sách khảo sát...");
        builder.setView(rootView);

        builder.setPositiveButton("  Thêm mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickListener.onAddNew();
            }
        });

        builder.setNegativeButton("  Kết thúc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        codeDialog = this;
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface OnClickListener{
        void onMoreClickListener(Code code);
        void onAddNew();
        void onDeleteClickListener(Code code, CodesAdapter adapter);
    }

    public static class CodesAdapter extends RecyclerView.Adapter<CodesAdapter.CodeViewHolder>{
        public List<Code> getCodes() {
            return codes;
        }

        public void setCodes(List<Code> codes) {
            this.codes = codes;
        }

        private List<Code> codes = new ArrayList<Code>();
        OnItemClickListener onItemClickListener;

        public CodesAdapter(List<Code> codes, OnItemClickListener onItemClickListener){
            this.codes = codes;
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public CodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_code, parent, false);
            return new CodeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CodeViewHolder holder, final int position) {
            final Code code = codes.get(position);
            holder.tvCode.setText(code.getValue());
            holder.tvCategory.setText(code.getCategotyName());
            holder.tvDate.setText(code.getDate());
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(codes.get(position));
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onDeleteClickListener(code, CodesAdapter.this);
                }
            });
        }

        @Override
        public int getItemCount() {
            try {
                return codes.size();
            }
            catch (Exception e){

            }
            return 0;
        }

        public class CodeViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_code)
            TextView tvCode;
            @BindView(R.id.tv_category)
            TextView tvCategory;
            @BindView(R.id.tv_date)
            TextView tvDate;
            @BindView(R.id.tv_more)
            TextView tvMore;
            @BindView(R.id.iv_delete)
            ImageView ivDelete;

            public CodeViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public interface OnItemClickListener {
            void onItemClickListener(Code code);
            void onDeleteClickListener(Code code, CodesAdapter adapter);
        }
    }
}
