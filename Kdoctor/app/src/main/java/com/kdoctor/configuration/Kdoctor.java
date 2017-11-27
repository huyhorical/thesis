package com.kdoctor.configuration;

import android.app.Application;
import android.content.Context;

import com.kdoctor.sql.DbManager;

/**
 * Created by INI\huy.trinh on 01/11/2017.
 */

public class Kdoctor extends Application {
    private static Kdoctor kdoctor;
    public static Kdoctor getInstance(){
        return kdoctor;
    }

    private Context context;

    public void onCreate() {
        super.onCreate();
        kdoctor = this;
        context = getApplicationContext();
        DbManager.getInstance(getApplicationContext());
    }

    public Context getAppContext() {
        return context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DbManager.getInstance(null).close();
    }
}
