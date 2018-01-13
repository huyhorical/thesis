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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.custom.SpinnerPlus;
import com.kdoctor.models.Code;
import com.kdoctor.models.CodeItem;
import com.kdoctor.models.CodeItemGet;
import com.kdoctor.models.Question;
import com.kdoctor.models.Sickness;
import com.kdoctor.sql.DbManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 10/30/2017.
 */

@SuppressLint("ValidFragment")
public class CodeDetailsDialog extends DialogFragment{
    public static int UPDATE = 0;
    public static int CREATE = 1;
    public static int NO_ID = -9999;

    public static CodeDetailsDialog getInstance(){
        return codeDetailsDialog;
    }
    static CodeDetailsDialog codeDetailsDialog;

    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    @BindView(R.id.tv_note)
    TextView tvNote;

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

        codeDetailsDialog = this;

        rvItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (type == UPDATE){
            List<CodeItemGet.ItemGet> itemGets = itemGet.getItemGetList();
        }
        else {
            tvNote.setVisibility(View.GONE);
        }
        adapter = new CodeItemAdapter(items, itemGet, new CodeItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(Code code) {
                dismiss();
            }
        });

        rvItems.setAdapter(adapter);
        rvItems.setHasFixedSize(false);
/*
        edtNote.setText(note);

        if (itemGet != null){
            edtNote.setText(itemGet.getNote());
        }
*/
        int totalSickness = (code == null || code.getSicknesses() == null) ? 0 : code.getSicknesses().size();
        if (totalSickness > 0 && code.getSicknesses().get(0).equals("0")){
            tvNote.setText("Trẻ bình thường");
        }
        else {
            tvNote.setText("Có " + totalSickness + " kết quả tham khảo");
        }

        if (totalSickness > 0 && !code.getSicknesses().get(0).equals("0")){
            tvNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Question question = new Question();
                    question.setQuestionContaint("Danh sách gợi ý:");
                    question.setAnswers(code.getSicknesses());

                    SicknessQuestionDialog dialog = new SicknessQuestionDialog(question, new SicknessQuestionDialog.OnAnswerListener() {
                        @Override
                        public void onAnswerListener(String value) {
                            try{
                                value = value.split("=")[1];
                            }
                            catch (Exception e){

                            }
                            //Log.i("huy",""+value);
                            RestServices.getInstance().getServices().searchSickness(value, new Callback<List<Sickness>>() {
                                @Override
                                public void success(List<Sickness> sicknesses, Response response) {
                                    if (sicknesses.size() > 0){
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
                    dialog.show(getFragmentManager(),"");
                }
            });
        }

        builder.setTitle("Danh sách triệu chứng...");
        builder.setView(rootView);

        builder.setPositiveButton(type == UPDATE ? "  Cập nhật" : "  Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type != UPDATE){
                    onCall.onCreate(items, tvNote.getText().toString());
                }
                else{
                    onCall.onUpdate(items, tvNote.getText().toString(), ID, code.getCategotyDataPath());
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
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
                    //holder.edtType.setText("");
                    //holder.spnItem.performClick();
                }
            });

            holder.edtType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        holder.spnItem.performClick();
                    }
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
                    CodeDetailsDialog codeDetailsDialog = CodeDetailsDialog.getInstance();
                }
            });

            List<String> newList = item.getAnswers();
            boolean isAlive = false;
            for (String str :
                    newList) {
                if (str.equals("Không")){
                    isAlive = true;
                    break;
                }
            }
            if (!isAlive){
                //newList.add("Không");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Kdoctor.getInstance().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, item.getAnswers());
            holder.spnItem.setAdapter(adapter);
            holder.spnItem.setSelected(false);
            //holder.spnItem.setSelection(-1,true);

            holder.spnItem.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    holder.edtType.setText(holder.spnItem.getSelectedItem().toString());
                    holder.spnItem.setSelection(holder.spnItem.getSelectedItemPosition(),false);

                    if (codeDetailsDialog != null){
                        codeDetailsDialog.tvNote.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (itemGet != null){
                for (CodeItemGet.ItemGet i:itemGet.getItemGetList()
                        ) {
                    if (i.getPrognostic().equals(item.getPrognostic())){
                        //if (!i.getAnswer().equals("Không")) {
                            holder.edtType.setText(i.getAnswer());
                        //}
                        break;
                    }
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
            SpinnerPlus spnItem;

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
