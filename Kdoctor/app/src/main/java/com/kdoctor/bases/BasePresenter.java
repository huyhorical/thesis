package com.kdoctor.bases;

import com.kdoctor.api.Functions;
import com.kdoctor.api.RestServices;

/**
 * Created by Huy on 10/21/2017.
 */

public class BasePresenter {
    private IView view;
    Functions functions;
    public BasePresenter(IView view){
        this.view = view;
        functions = RestServices.getInstance().getServices();
    }

    protected IView getView(){
        return view;
    }

    public Functions getRestAPI() {
        return functions;
    }
}
