package com.kdoctor.fragments.vaccines.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kdoctor.R;
import com.kdoctor.api.RestServices;

import com.kdoctor.bases.IView;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.dialogs.CategoryDialog;
import com.kdoctor.dialogs.DisplayTypeDialog;
import com.kdoctor.dialogs.ProvinceDialog;
import com.kdoctor.dialogs.QuestionDialog;
import com.kdoctor.dialogs.TypeTextDateDialog;
import com.kdoctor.dialogs.TypeTextDialog;
import com.kdoctor.dialogs.VaccineMapDialog;
import com.kdoctor.fragments.vaccines.adapters.RecyclerViewAdapterVaccine;
import com.kdoctor.fragments.vaccines.presenter.FragmentVaccinePresenter;
import com.kdoctor.fragments.vaccines.view.IFragmentVaccine;
import com.kdoctor.models.Function;
import com.kdoctor.models.SicknessCategory;
import com.kdoctor.models.Vaccine;
import com.kdoctor.services.VaccineService;
import com.kdoctor.sql.DbManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 11/6/2016.
 */

public class FragmentVaccine extends Fragment implements IFragmentVaccine{
    FragmentVaccinePresenter presenter;
    ProgressDialog progressDialog;

    RecyclerViewAdapterVaccine adapter;

    @BindView(R.id.rv_vaccine)
    RecyclerView rvVaccine;
    @BindView(R.id.fam)
    FloatingActionsMenu fam;
    @BindView(R.id.fab_display_type)
    FloatingActionButton fabDisplayType;
    @BindView(R.id.fab_reminder)
    FloatingActionButton fabReminder;
    @BindView(R.id.fab_map)
    FloatingActionButton fabMap;
    @BindView(R.id.fab_birthday_filter)
    FloatingActionButton fabBirthdayFilter;
    @BindView(R.id.fl_label)
    FrameLayout flLabel;
    @BindView(R.id.tv_display_type)
    TextView tvDisplayType;
    @BindView(R.id.tv_birthday_filter)
    TextView tvBirthdayFilter;
    @BindView(R.id.tv_reminder)
    TextView tvReminder;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vaccine, container, false);
        return rootView;
    }

    boolean isWaiting = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new FragmentVaccinePresenter(this);

        fam.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                flLabel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                flLabel.setVisibility(View.GONE);
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Vui lòng đợi...");

        rvVaccine.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapterVaccine(new ArrayList<Vaccine>(), new RecyclerViewAdapterVaccine.OnClickListener() {
            @Override
            public void onEditClickListener(final int id) {
                TypeTextDateDialog dialog = new TypeTextDateDialog("Nhập ghi chú", new TypeTextDateDialog.OnClickListener() {
                    @Override
                    public void onPositiveButtonClickListener(String text, String dateString) {
                        List<Vaccine> vaccineList = DbManager.getInstance(getContext()).getRecords(DbManager.VACCINES, Vaccine.class);
                        for (Vaccine vaccine:vaccineList
                                ) {
                            if (vaccine.getId() == id){
                                vaccine.setMessage(text);
                                vaccine.setAlarmDate(dateString);
                            }
                        }
                        DbManager.getInstance(getContext()).updateRecords(DbManager.VACCINES, vaccineList);
                        adapter.setVaccines(vaccineList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                });
                dialog.show(getFragmentManager(), "");
            }

            @Override
            public void onUnSelectedClickListener(final Vaccine vaccine, final CheckBox checkBox) {
                QuestionDialog dialog = new QuestionDialog("Xác nhận", "Việc làm này sẽ đồng thời hủy bỏ ghi chú (nếu có), bạn chắc chắn chứ?", "Xác nhận", new QuestionDialog.OnTwoChoicesSelection() {
                    @Override
                    public void onPositiveButtonClick() {
                        vaccine.setSelected(false);
                        vaccine.setAlarmDate("");
                        vaccine.setMessage("");
                        DbManager.getInstance(null).selectRecord(DbManager.VACCINES, vaccine, vaccine.getId());

                        adapter.setVaccines(DbManager.getInstance(getContext()).getRecords(DbManager.VACCINES, Vaccine.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegativeButtonClick() {
                        checkBox.setChecked(true);
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });
        rvVaccine.setAdapter(adapter);
        rvVaccine.setHasFixedSize(false);

        List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
        presenter.adjustList(functions);

        fabDisplayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWaiting){
                    return;
                }
                isWaiting = true;

                String status = "";
                List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
                for (Function function :
                        functions) {
                    if (function.getTab().equals(DbManager.VACCINES) && function.getFunction().equals("DISPLAY_TYPE")){
                        status=function.getStatus();
                        break;
                    }
                }
                DisplayTypeDialog displayTypeDialog = new DisplayTypeDialog(status, new DisplayTypeDialog.OnClickListener() {
                    @Override
                    public void onPositiveButtonClickListener(String type) {
                        List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
                        for (Function function :
                                functions) {
                            if (function.getTab().equals(DbManager.VACCINES) && function.getFunction().equals("DISPLAY_TYPE")) {
                                function.setStatus(type);
                                break;
                            }
                        }
                        presenter.adjustList(functions);
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                });
                displayTypeDialog.show(getFragmentManager(), "");

                isWaiting = false;
            }
        });

        if (VaccineService.isRunning()){
            tvReminder.setText("Thông báo: Bật");
        }

        fabReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWaiting){
                    return;
                }
                isWaiting = true;

                if (VaccineService.isRunning()) {
                    VaccineService.setIsRunning(false);
                    tvReminder.setText("Nhắc nhở: Tắt");
                }
                else{
                    List<Vaccine> vaccines = DbManager.getInstance(getContext()).getRecords(DbManager.VACCINES, Vaccine.class);
                    VaccineService.setVaccines(vaccines);
                    VaccineService.setIsRunning(true);
                    getActivity().startService(new Intent(getActivity().getBaseContext(), VaccineService.class));
                    tvReminder.setText("Nhắc nhở: Bật");
                }

                isWaiting = false;
            }
        });

        fabBirthdayFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWaiting){
                    return;
                }
                isWaiting = true;

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //DbManager.getInstance(Kdoctor.getInstance().getApplicationContext()).deleteAllRecord(DbManager.VACCINES);

                        List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
                        for (Function function :
                                functions) {
                            if (function.getTab().equals(DbManager.VACCINES) && function.getFunction().equals("BIRTHDAY_FILTER")) {
                                Calendar c = Calendar.getInstance();
                                c.set(year,month,dayOfMonth);
                                String dateString = (new SimpleDateFormat("dd/MM/yyyy")).format(c.getTime());
                                function.setStatus(dateString);
                                break;
                            }
                        }
                        presenter.adjustList(functions);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
                            for (Function function :
                                    functions) {
                                if (function.getTab().equals(DbManager.VACCINES) && function.getFunction().equals("BIRTHDAY_FILTER")){
                                    function.setStatus("OFF");
                                    break;
                                }
                            }
                            presenter.adjustList(functions);
                        }
                    }
                });

                datePickerDialog.show();

                isWaiting = false;
            }
        });

        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWaiting){
                    return;
                }
                isWaiting = true;

                try {
                    //Toast.makeText(getActivity().getApplicationContext(), "Hiện tại chỉ hỗ trợ tại khu vực TP HCM...", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){

                }

                /*
                List<SicknessCategory> categories = new ArrayList<>();
                categories.add(new SicknessCategory(1,"Hà Nội", ""));
                categories.add(new SicknessCategory(2,"Hồ Chí Minh", ""));
                ProvinceDialog dialog = new ProvinceDialog(categories, new ProvinceDialog.OnAnswerListener() {
                    @Override
                    public void onAnswerListener(SicknessCategory answer) {
                        VaccineMapDialog dialog = new VaccineMapDialog(answer.getId());
                        dialog.show(getFragmentManager(), "");
                    }
                });
                dialog.show(getFragmentManager(),"");
                */
                VaccineMapDialog dialog = new VaccineMapDialog(2);
                dialog.show(getFragmentManager(), "");

                isWaiting = false;
            }
        });
    }

    @Override
    public void onGetVaccinesSuccess(List<Vaccine> vaccines) {
        VaccineService.setVaccines(vaccines);
        adapter.updateVaccineList(vaccines);
        List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
        presenter.adjustList(functions);
        hideLoading();
    }

    @Override
    public void onGetVaccinesFailure(String error) {
        hideLoading();
        QuestionDialog questionDialog = new QuestionDialog("Lỗi", "Bạn muốn tải lại danh sách tiêm chủng không?", new QuestionDialog.OnTwoChoicesSelection() {
            @Override
            public void onPositiveButtonClick() {
                showLoading();
                presenter.getVaccines();
            }

            @Override
            public void onNegativeButtonClick() {

            }
        });
        questionDialog.show(getFragmentManager(), "");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && (adapter.getVaccines() == null || adapter.getVaccines().size() == 0)){
            showLoading();
            presenter.getVaccines();
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }

    @Override
    public void displayVaccinesFromLocalDb(List<Vaccine> vaccines) {
        adapter.updateVaccineList(vaccines);
        hideLoading();
    }

    @Override
    public void onUpdateFunctionsSuccess(List<Function> functions) {
        List<Vaccine> vaccines = DbManager.getInstance(null).getRecords(DbManager.VACCINES, Vaccine.class);
        String status = "";

        for (Function function :
                functions) {
            if (function.getTab().equals(DbManager.VACCINES) && function.getFunction().equals("BIRTHDAY_FILTER")){
                status=function.getStatus();

                Date date = null;
                if (!status.equals("OFF")){
                    try {
                        date = (new SimpleDateFormat("dd/MM/yyyy")).parse(status);
                    } catch (ParseException e) {
                        date = null;
                    }
                }
                VaccineService.setBirthDay(date);
                break;
            }
        }

        switch (status){
            case "OFF":
                tvBirthdayFilter.setText("Gợi ý theo ngày sinh: Trống");
                break;
            default:
                tvBirthdayFilter.setText("Gợi ý theo ngày sinh: "+status);
                Date date;
                try {
                    date = (new SimpleDateFormat("dd/MM/yyyy")).parse(status);
                } catch (ParseException e) {
                    date = new Date();
                }
                for (int i=vaccines.size()-1; i>=0; i--) {
                    int monthNumber = daysBetween(date, new Date())/30;
                    if (vaccines.get(i).getStartMonth() < monthNumber){
                        vaccines.remove(i);
                    }
                }
                break;
        }

        for (Function function :
                functions) {
            if (function.getTab().equals(DbManager.VACCINES) && function.getFunction().equals("DISPLAY_TYPE")){
                status=function.getStatus();
                break;
            }
        }

        switch (status){
            case "ALL":
                tvDisplayType.setText("Hiển thị: Tất cả");
                break;
            case "SELECTED":
                tvDisplayType.setText("Hiển thị: Đã chọn");
                break;
            case "UNSELECTED":
                tvDisplayType.setText("Hiển thị: Chưa chọn");
                break;
        }

        for (int i=vaccines.size()-1; i>=0; i--){
            switch (status){
                case "ALL":
                    break;
                case "SELECTED":
                    if (!vaccines.get(i).isSelected()){
                        vaccines.remove(i);
                    }
                    break;
                case "UNSELECTED":
                    if (vaccines.get(i).isSelected()){
                        vaccines.remove(i);
                    }
                    break;
            }
        }

        rvVaccine.setAdapter(null);
        rvVaccine.setLayoutManager(null);
        adapter = new RecyclerViewAdapterVaccine(vaccines, new RecyclerViewAdapterVaccine.OnClickListener() {
            @Override
            public void onEditClickListener(final int id) {
                TypeTextDateDialog dialog = new TypeTextDateDialog("Nhập ghi chú", new TypeTextDateDialog.OnClickListener() {
                    @Override
                    public void onPositiveButtonClickListener(String text, String dateString) {
                        List<Vaccine> vaccineList = DbManager.getInstance(getContext()).getRecords(DbManager.VACCINES, Vaccine.class);
                        for (Vaccine vaccine:vaccineList
                                ) {
                            if (vaccine.getId() == id){
                                vaccine.setMessage(text);
                                vaccine.setAlarmDate(dateString);
                            }
                        }
                        DbManager.getInstance(getContext()).updateRecords(DbManager.VACCINES, vaccineList);
                        adapter.setVaccines(vaccineList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                });
                dialog.show(getFragmentManager(), "");
            }

            @Override
            public void onUnSelectedClickListener(final Vaccine vaccine, final CheckBox checkBox) {
                QuestionDialog dialog = new QuestionDialog("Xác nhận", "Việc làm này sẽ đồng thời hủy bỏ ghi chú (nếu có), bạn chắc chắn chứ?", "Xác nhận", new QuestionDialog.OnTwoChoicesSelection() {
                    @Override
                    public void onPositiveButtonClick() {
                        vaccine.setSelected(false);
                        vaccine.setAlarmDate("");
                        vaccine.setMessage("");
                        DbManager.getInstance(null).selectRecord(DbManager.VACCINES, vaccine, vaccine.getId());

                        adapter.setVaccines(DbManager.getInstance(getContext()).getRecords(DbManager.VACCINES, Vaccine.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegativeButtonClick() {
                        checkBox.setChecked(true);
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });
        rvVaccine.setAdapter(adapter);
        rvVaccine.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.sort();
        adapter.notifyDataSetChanged();
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    @Override
    public void onUpdateFunctionsFailure(String error) {

    }
}
