package com.kdoctor.api;

import com.kdoctor.api.models.Vaccine;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Huy on 10/23/2017.
 */

public interface Functions {
    @GET("/api/vaccine")
    public void getVaccines(Callback<List<Vaccine>> callback);
}
