package com.kdoctor.fragments.vaccines.presenter;

import com.kdoctor.bases.BasePresenter;
import com.kdoctor.bases.IView;
import com.kdoctor.fragments.vaccines.view.IFragmentVaccine;
import com.kdoctor.models.Function;
import com.kdoctor.models.Vaccine;
import com.kdoctor.services.VaccineService;
import com.kdoctor.sql.DbManager;

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
                List<Vaccine> selectedValues = DbManager.getInstance(null).getRecords(DbManager.VACCINES, Vaccine.class);
                for (Vaccine value :
                        selectedValues) {
                    for (Vaccine vaccine:
                            vaccines) {
                        if (vaccine.getId() == value.getId() && value.isSelected()){
                            vaccine.setSelected(true);
                            break;
                        }
                    }
                }
                try {
                    DbManager.getInstance(null).updateRecords(DbManager.VACCINES, vaccines);
                    ((IFragmentVaccine)getView()).onGetVaccinesSuccess(vaccines);
                } catch (Exception e) {
                    ((IFragmentVaccine)getView()).onGetVaccinesFailure(e.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try{
                    List<Vaccine> vaccines = DbManager.getInstance(null).getRecords(DbManager.VACCINES, Vaccine.class);
                    VaccineService.setVaccines(vaccines);
                    ((IFragmentVaccine)getView()).displayVaccinesFromLocalDb(vaccines);
                }
                catch (Exception e){
                    ((IFragmentVaccine)getView()).onGetVaccinesFailure(e.toString() + error.toString());
                }
            }
        });
    }

    @Override
    public void adjustList(List<Function> functions) {
        try{
            DbManager.getInstance(null).updateRecords(DbManager.FUNCTIONS, functions);
            ((IFragmentVaccine)getView()).onUpdateFunctionsSuccess(functions);
        }
        catch (Exception e){
            ((IFragmentVaccine)getView()).onGetVaccinesFailure(e.toString());
        }
    }
}
