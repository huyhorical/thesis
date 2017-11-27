package com.kdoctor.fragments.drugs.view;


import com.kdoctor.bases.IView;
import com.kdoctor.models.Drug;
import com.kdoctor.models.Function;
import com.kdoctor.models.Vaccine;

import java.util.List;

/**
 * Created by Huy on 10/23/2017.
 */

public interface IFragmentDrug extends IView {
    void onGetDrugsSuccess(List<Drug> drugs);
    void onGetDrugsFailure(String error);

    void displayDrugsFromLocalDb(List<Drug> drugs);
}
