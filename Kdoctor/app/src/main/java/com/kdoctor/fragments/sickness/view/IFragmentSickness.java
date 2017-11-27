package com.kdoctor.fragments.sickness.view;

import com.kdoctor.bases.IView;
import com.kdoctor.models.SicknessCategory;

import java.util.List;

/**
 * Created by Huy on 11/5/2017.
 */

public interface IFragmentSickness extends IView{
    void onGetCategoriesSuccess(List<SicknessCategory> categories);
    void onGetCategoriesFailure(String error);
}
