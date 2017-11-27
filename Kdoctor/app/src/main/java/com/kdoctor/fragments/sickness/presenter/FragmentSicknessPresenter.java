package com.kdoctor.fragments.sickness.presenter;

import com.kdoctor.bases.BasePresenter;
import com.kdoctor.bases.IView;
import com.kdoctor.fragments.sickness.view.IFragmentSickness;
import com.kdoctor.models.SicknessCategory;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huy on 11/5/2017.
 */

public class FragmentSicknessPresenter extends BasePresenter implements IFragmentSicknessPresenter{
    public FragmentSicknessPresenter(IView view) {
        super(view);
    }


    @Override
    public void getCategories() {
        getRestAPI().getCategories(new Callback<List<SicknessCategory>>() {
            @Override
            public void success(List<SicknessCategory> categories, Response response) {
                ((IFragmentSickness)getView()).onGetCategoriesSuccess(categories);
            }

            @Override
            public void failure(RetrofitError error) {
                ((IFragmentSickness)getView()).onGetCategoriesFailure(error.getMessage());
            }
        });
    }
}
