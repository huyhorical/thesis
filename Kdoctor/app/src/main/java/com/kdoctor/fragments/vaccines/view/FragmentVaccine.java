package com.kdoctor.fragments.vaccines.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.kdoctor.R;
import com.kdoctor.api.RestServices;
import com.kdoctor.api.models.Vaccine;
import com.kdoctor.fragments.vaccines.adapters.RecyclerViewAdapterVaccine;
import com.kdoctor.fragments.vaccines.view.IFragmentVaccine;

import java.util.ArrayList;
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

    List<Vaccine> vaccines;

    @BindView(R.id.rv_vaccine)
    RecyclerView rvVaccine;

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

        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(getActivity());

        vaccines = new ArrayList<Vaccine>();
        rvVaccine.setLayoutManager(layoutManager);
        final RecyclerViewAdapterVaccine adapter = new RecyclerViewAdapterVaccine(vaccines);
        rvVaccine.setAdapter(adapter);
        rvVaccine.setHasFixedSize(true);

        RestServices.getInstance().getServices().getVaccines(new Callback<List<Vaccine>>() {
            @Override
            public void success(List<Vaccine> vaccines, Response response) {
//                for (Vaccine v:vaccines
//                        ) {
//                    Log.i("huy",v.getActivity());
//                }
                adapter.updateVaccineList(vaccines);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onGetVaccinesSuccess(List<Vaccine> vaccines) {

    }

    @Override
    public void onGetVaccinesFailure(String error) {

    }
}
