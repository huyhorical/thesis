package com.kdoctor.fragments.drugs.presenter;

import com.kdoctor.bases.BasePresenter;
import com.kdoctor.bases.IView;
import com.kdoctor.fragments.drugs.view.IFragmentDrug;
import com.kdoctor.fragments.sickness.presenter.IFragmentSicknessPresenter;
import com.kdoctor.fragments.sickness.view.IFragmentSickness;
import com.kdoctor.models.Drug;
import com.kdoctor.models.Sickness;
import com.kdoctor.models.SicknessCategory;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 11/5/2017.
 */

public class FragmentDrugPresenter extends BasePresenter implements IFragmentDrugPresenter{
    public FragmentDrugPresenter(IView view) {
        super(view);
    }

    @Override
    public void getDrugs(int startIndex, int endIndex) {
        getRestAPI().getDrugs(startIndex, endIndex, new Callback<List<Drug>>() {
            @Override
            public void success(List<Drug> drugs, Response response) {
                ((IFragmentDrug)getView()).onGetDrugsSuccess(drugs);
            }

            @Override
            public void failure(RetrofitError error) {
                ((IFragmentDrug)getView()).onGetDrugsFailure(error.getMessage());
            }
        });
    }
}
