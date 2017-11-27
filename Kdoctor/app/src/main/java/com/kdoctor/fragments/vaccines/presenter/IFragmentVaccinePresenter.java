package com.kdoctor.fragments.vaccines.presenter;

import com.kdoctor.models.Function;

import java.util.List;

/**
 * Created by Huy on 10/23/2017.
 */

public interface IFragmentVaccinePresenter {
    void getVaccines();
    void adjustList(List<Function> functions);
}
