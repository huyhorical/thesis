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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.kdoctor.dialogs.DisplayTypeDialog;
import com.kdoctor.dialogs.QuestionDialog;
import com.kdoctor.fragments.vaccines.adapters.RecyclerViewAdapterVaccine;
import com.kdoctor.fragments.vaccines.presenter.FragmentVaccinePresenter;
import com.kdoctor.fragments.vaccines.view.IFragmentVaccine;
import com.kdoctor.models.Function;
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
    @BindView(R.id.srl_vaccines)
    SwipeRefreshLayout srlVaccines;


    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vaccine, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new FragmentVaccinePresenter(this);

        srlVaccines.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlVaccines.setRefreshing(true);
                presenter.getVaccines();
            }
        });

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
        adapter = new RecyclerViewAdapterVaccine(new ArrayList<Vaccine>());
        rvVaccine.setAdapter(adapter);
        rvVaccine.setHasFixedSize(false);

        List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
        presenter.adjustList(functions);

        fabDisplayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        if (VaccineService.isRunning()){
            tvReminder.setText("Thông báo: Bật");
        }

        fabReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VaccineService.isRunning()) {
                    VaccineService.setIsRunning(false);
                    tvReminder.setText("Thông báo: Tắt");
                }
                else{
                    VaccineService.setIsRunning(true);
                    getActivity().startService(new Intent(getActivity().getBaseContext(), VaccineService.class));
                    tvReminder.setText("Nhắc nhở: Bật");
                }
            }
        });

        fabBirthdayFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
            }
        });
    }

    @Override
    public void onGetVaccinesSuccess(List<Vaccine> vaccines) {
        VaccineService.setVaccines(vaccines);
        adapter.updateVaccineList(vaccines);
        List<Function> functions = DbManager.getInstance(null).getRecords(DbManager.FUNCTIONS, Function.class);
        presenter.adjustList(functions);
        if (srlVaccines.isRefreshing()) {
            srlVaccines.setRefreshing(false);
        }
        hideLoading();
    }

    @Override
    public void onGetVaccinesFailure(String error) {
        if (srlVaccines.isRefreshing()) {
            srlVaccines.setRefreshing(false);
        }
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
                tvBirthdayFilter.setText("Ngày sinh: Chưa thiết lập");
                break;
            default:
                tvBirthdayFilter.setText("Ngày sinh: "+status);
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
        adapter = new RecyclerViewAdapterVaccine(vaccines);
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
