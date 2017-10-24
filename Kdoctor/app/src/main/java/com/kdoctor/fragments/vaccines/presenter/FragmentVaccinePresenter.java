package com.kdoctor.fragments.vaccines.presenter;

import com.kdoctor.api.RestServices;
import com.kdoctor.api.models.Vaccine;
import com.kdoctor.bases.BasePresenter;
import com.kdoctor.bases.IView;
import com.kdoctor.fragments.vaccines.view.IFragmentVaccine;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 10/23/2017.
 */

public class FragmentVaccinePresenter extends BasePresenter implements IFragmentVaccinePresenter{
    public FragmentVaccinePresenter(IView view) {
        super(view);
    }

    @Override
    public void getVaccines() {
        getRestAPI().getVaccines(new Callback<List<Vaccine>>() {
            @Override
            public void success(List<Vaccine> vaccines, Response response) {
                ((IFragmentVaccine)getView()).onGetVaccinesSuccess(vaccines);
            }

            @Override
            public void failure(RetrofitError error) {
                ((IFragmentVaccine)getView()).onGetVaccinesFailure(error.toString());
            }
        });
    }
}
