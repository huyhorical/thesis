package com.kdoctor.fragments.vaccines.view;

import com.kdoctor.api.models.Vaccine;
import com.kdoctor.bases.IView;

import java.util.List;

/**
 * Created by Huy on 10/23/2017.
 */

public interface IFragmentVaccine extends IView {
    void onGetVaccinesSuccess(List<Vaccine> vaccines);
    void onGetVaccinesFailure(String error);
}
