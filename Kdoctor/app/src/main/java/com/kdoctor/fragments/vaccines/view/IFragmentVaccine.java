package com.kdoctor.fragments.vaccines.view;


import com.kdoctor.bases.IView;
import com.kdoctor.models.Function;
import com.kdoctor.models.Vaccine;

import java.util.List;

/**
 * Created by Huy on 10/23/2017.
 */

public interface IFragmentVaccine extends IView {
    void onGetVaccinesSuccess(List<Vaccine> vaccines);
    void onGetVaccinesFailure(String error);

    void displayVaccinesFromLocalDb(List<Vaccine> vaccines);

    void onUpdateFunctionsSuccess(List<Function> functions);
    void onUpdateFunctionsFailure(String error);
}
