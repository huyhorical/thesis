package com.kdoctor.fragments.drawer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kdoctor.R;
import com.kdoctor.dialogs.DiagnosisInfoDialog;
import com.kdoctor.fragments.drawer.adapters.SicknessHistoryAdapter;
import com.kdoctor.fragments.drawer.presenter.FragmentDrawerPresenter;
import com.kdoctor.models.Diagnosis;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDrawer extends Fragment implements IFragmentDrawer{

    private static FragmentDrawer drawer;
    public static FragmentDrawer getInstance(){
        return drawer == null ? new FragmentDrawer() : drawer;
    }

    private View rootView;

    public SicknessHistoryAdapter getAdapter() {
        return adapter;
    }

    SicknessHistoryAdapter adapter;
    FragmentDrawerPresenter presenter;

    @BindView(R.id.rv_sickness_history)
    RecyclerView rvSicknessHistory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_drawer, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new FragmentDrawerPresenter(this);

        rvSicknessHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SicknessHistoryAdapter(new ArrayList<Diagnosis>(), new SicknessHistoryAdapter.OnClickListener() {
            @Override
            public void onMoreClickListener(Diagnosis diagnosis) {
                DiagnosisInfoDialog dialog = new DiagnosisInfoDialog(diagnosis);
                dialog.show(getFragmentManager(), "");
            }
        });
        rvSicknessHistory.setAdapter(adapter);
        rvSicknessHistory.setHasFixedSize(false);

        drawer = this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
