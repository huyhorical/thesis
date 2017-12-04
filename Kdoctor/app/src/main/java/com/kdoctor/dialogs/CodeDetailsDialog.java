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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.models.Code;
import com.kdoctor.models.CodeItem;
import com.kdoctor.models.CodeItemGet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class CodeDetailsDialog extends DialogFragment{
    public static int UPDATE = 0;
    public static int CREATE = 1;
    public static int NO_ID = -9999;

    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    @BindView(R.id.edt_note)
    EditText edtNote;

    CodeItemAdapter adapter;
    OnCall onCall;
    int type;
    int ID;
    CodeItemGet itemGet;
    Code code;

    public List<CodeItem> getItems() {
        return items;
    }

    public void setItems(List<CodeItem> items) {
        this.items = items;
    }

    List<CodeItem> items;
    String note;

    @SuppressLint("ValidFragment")
    public CodeDetailsDialog(List<CodeItem> items, String note, Code code, CodeItemGet itemGet, OnCall onCall, int type, int ID){
        this.items = items;
        this.note = note;
        this.type = type;
        this.itemGet = itemGet;
        this.code = code;
        this.onCall = onCall;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fragment_code_details, null);
        ButterKnife.bind(this, rootView);

        rvItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (type == UPDATE){
            List<CodeItemGet.ItemGet> itemGets = itemGet.getItemGetList();

        }
        adapter = new CodeItemAdapter(items, itemGet, new CodeItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(Code code) {
                dismiss();
            }
        });

        rvItems.setAdapter(adapter);
        rvItems.setHasFixedSize(false);

        edtNote.setText(note);

        if (itemGet != null){
            edtNote.setText(itemGet.getNote());
        }

        builder.setTitle("Danh sách triệu chứng...");
        builder.setView(rootView);

        builder.setPositiveButton(type == UPDATE ? "  Cập nhật" : "  Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type != UPDATE){
                    onCall.onCreate(items, edtNote.getText().toString());
                }
                else{
                    onCall.onUpdate(items, edtNote.getText().toString(), ID, code.getCategotyDataPath());
                }
            }
        });

        builder.setNegativeButton("  Kết thúc", new DialogInterface.OnClickListener() {
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

    public interface OnCall{
        void onCreate(List<CodeItem> items, String note);
        void onUpdate(List<CodeItem> items, String note, int ID, String dataPath);
    }

    public static class CodeItemAdapter extends RecyclerView.Adapter<CodeItemAdapter.CodeItemViewHolder>{
        private List<CodeItem> items = new ArrayList<CodeItem>();
        OnItemClickListener onItemClickListener;
        CodeItemGet itemGet;

        public CodeItemAdapter(List<CodeItem> items, CodeItemGet itemGet, OnItemClickListener onItemClickListener){
            this.items = items;
            this.itemGet = itemGet;
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public CodeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_code_item, parent, false);
            return new CodeItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CodeItemViewHolder holder, final int position) {
            final CodeItem item = items.get(position);
            holder.tvQuestion.setText(item.getQuestion());
            holder.edtType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.edtType.setText("");
                }
            });

            holder.edtType.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.setAnswer(s.toString());
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Kdoctor.getInstance().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, item.getAnswers());
            holder.spnItem.setAdapter(adapter);
            holder.spnItem.setSelected(false);
            holder.spnItem.setSelection(0,true);
            holder.spnItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    holder.edtType.setText(holder.spnItem.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (itemGet != null){
                for (CodeItemGet.ItemGet i:itemGet.getItemGetList()
                        ) {
                    if (i.getPrognostic().equals(item.getPrognostic())){
                        holder.edtType.setText(i.getAnswer());
                    }
                    break;
                }
            }
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

        public class CodeItemViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_question)
            TextView tvQuestion;
            @BindView(R.id.edt_type)
            EditText edtType;
            @BindView(R.id.spn_item)
            Spinner spnItem;

            public CodeItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public interface OnItemClickListener {
            void onItemClickListener(Code code);
        }
    }

}
