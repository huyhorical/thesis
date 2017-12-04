package com.kdoctor.fragments.drugs.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.dialogs.DisplayTypeDialog;
import com.kdoctor.dialogs.DrugInfoDialog;
import com.kdoctor.dialogs.DrugsDialog;
import com.kdoctor.dialogs.QuestionDialog;
import com.kdoctor.dialogs.TypeDrugInfoDialog;
import com.kdoctor.fragments.drugs.adapters.RecyclerViewAdapterDrug;
import com.kdoctor.fragments.drugs.presenter.FragmentDrugPresenter;
import com.kdoctor.models.Drug;
import com.kdoctor.sql.DbManager;

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

public class FragmentDrug extends Fragment implements IFragmentDrug{
    FragmentDrugPresenter presenter;
    ProgressDialog progressDialog;

    public RecyclerViewAdapterDrug getAdapter() {
        return adapter;
    }

    RecyclerViewAdapterDrug adapter;

    @BindView(R.id.rv_drug)
    RecyclerView rvDrug;
    @BindView(R.id.fam)
    FloatingActionsMenu fam;
    @BindView(R.id.fab_heart)
    FloatingActionButton fabHeart;
    @BindView(R.id.fab_find)
    FloatingActionButton fabFind;
    @BindView(R.id.fl_label)
    FrameLayout flLabel;
    @BindView(R.id.srl_drug)
    SwipeRefreshLayout srlDrug;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_drug, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new FragmentDrugPresenter(this);

        srlDrug.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlDrug.setRefreshing(false);
                //srlDrug.setRefreshing(true);
                //presenter.getDrugs();
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

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvDrug.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterDrug(new ArrayList<Drug>(), new RecyclerViewAdapterDrug.OnClickListener() {
            @Override
            public void onResearchClickListener(Drug drug) {
                DrugInfoDialog dialog = new DrugInfoDialog(drug, new DrugInfoDialog.OnClickListener() {
                    @Override
                    public void onSelectListener(Drug d, boolean isSelected) {
                        d.setSelected(isSelected);
                        DbManager.getInstance(Kdoctor.getInstance().getAppContext()).selectRecord(DbManager.DRUGS, d, d.getId());
                    }
                });
                dialog.show(getFragmentManager(), "");
            }

            @Override
            public void onSelectListener(Drug drug, boolean isSelected) {
                drug.setSelected(isSelected);
                DbManager.getInstance(getContext()).selectRecord(DbManager.DRUGS, drug, drug.getId());
            }
        });

        rvDrug.setAdapter(adapter);
        rvDrug.setHasFixedSize(false);

        rvDrug.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            presenter.getDrugs(totalItemCount, totalItemCount+1);
                        }
                    }
                }
            }
        });

        fabHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Drug> drugs = DbManager.getInstance(getContext()).getRecords(DbManager.DRUGS, Drug.class);
                if (drugs != null){
                    for (int i = drugs.size() - 1; i >= 0; i--){
                        if (drugs.get(i).isSelected() == false){
                            drugs.remove(i);
                        }
                    }
                }
                DrugsDialog dialog = new DrugsDialog(drugs, new DrugsDialog.OnClickListener() {
                    @Override
                    public void onResearchClickListener(Drug drug) {
                        DrugInfoDialog infoDialog = new DrugInfoDialog(drug, new DrugInfoDialog.OnClickListener() {
                            @Override
                            public void onSelectListener(Drug d, boolean isSelected) {
                                d.setSelected(isSelected);
                                DbManager.getInstance(getContext()).selectRecord(DbManager.DRUGS, d, d.getId());
                            }
                        });
                        infoDialog.show(getFragmentManager(), "");
                    }

                    @Override
                    public void onNoteClickListener(Drug drug, boolean isSelected) {
                        drug.setSelected(isSelected);
                        DbManager.getInstance(getContext()).selectRecord(DbManager.DRUGS, drug, drug.getId());
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });

        fabFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TypeDrugInfoDialog dialog = new TypeDrugInfoDialog("Nhập tên thuốc", new TypeDrugInfoDialog.OnClickListener() {
                    @Override
                    public void onPositiveButtonClickListener(String value) {
                        RestServices.getInstance().getServices().searchDrug(value, new Callback<List<Drug>>() {
                            @Override
                            public void success(List<Drug> sicknesses, Response response) {
                                if (sicknesses.size() == 0) {
                                    Toast.makeText(getContext(), "Không có kết quả nào...", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                DrugsDialog drugsDialog = new DrugsDialog(sicknesses, new DrugsDialog.OnClickListener() {
                                    @Override
                                    public void onResearchClickListener(final Drug drug) {
                                        DrugInfoDialog infoDialog = new DrugInfoDialog(drug, new DrugInfoDialog.OnClickListener() {
                                            @Override
                                            public void onSelectListener(Drug drug1, boolean isSelected) {
                                                drug1.setSelected(true);
                                                DbManager.getInstance(getContext()).selectRecord(DbManager.DRUGS, drug1, drug1.getId());
                                            }
                                        });
                                        infoDialog.show(getFragmentManager(), "");
                                    }

                                    @Override
                                    public void onNoteClickListener(Drug drug, boolean isSelected) {
                                        //sickness.setSelected(isSelected);
                                        //DbManager.getInstance(getContext()).selectRecord(DbManager.SICKNESSES, sickness, sickness.getId());
                                    }
                                });
                                drugsDialog.show(getFragmentManager(), "");
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getContext(), "Kết nối yếu...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNegativeButtonClickListener() {

                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });
    }

    @Override
    public void onGetDrugsSuccess(List<Drug> drugs) {
        //adapter.updateDrugList(drugs);
        loading = true;
        adapter.getDrugs().addAll(drugs);
        adapter.notifyDataSetChanged();
        if (srlDrug.isRefreshing()) {
            srlDrug.setRefreshing(false);
        }
        hideLoading();
    }

    @Override
    public void onGetDrugsFailure(String error) {
        loading = true;
        if (srlDrug.isRefreshing()) {
            srlDrug.setRefreshing(false);
        }
        hideLoading();
        QuestionDialog questionDialog = new QuestionDialog("Lỗi", "Bạn muốn tải lại danh sách tiêm chủng không?", new QuestionDialog.OnTwoChoicesSelection() {
            @Override
            public void onPositiveButtonClick() {
                showLoading();
                presenter.getDrugs(totalItemCount, 1);
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
        if (isVisibleToUser && (adapter.getDrugs() == null || adapter.getDrugs().size() == 0)){
            showLoading();
            presenter.getDrugs(0, 1);
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
    public void displayDrugsFromLocalDb(List<Drug> drugs) {
        adapter.updateDrugList(drugs);
        hideLoading();
    }
}
