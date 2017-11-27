package com.kdoctor.api;

import com.kdoctor.models.Drug;
import com.kdoctor.models.Sickness;
import com.kdoctor.models.SicknessCategory;
import com.kdoctor.models.Vaccine;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Huy on 10/23/2017.
 */

public interface Functions {
    @GET("/api/vaccineapi")
    public void getVaccines(Callback<List<Vaccine>> callback);
    @GET("/api/dmbenhapi")
    public void getCategories(Callback<List<SicknessCategory>> callback);
    @GET("/api/dmbenhapi")
    public void getSicknessCategory(@Query("input") int input, @Query("sindex") int sindex, @Query("eindex") int eindex, Callback<List<Sickness>> callback);
    @GET("/api/benhapi")
    public void searchSickness(@Query("input") String input, Callback<List<Sickness>> callback);
    @GET("/api/benhapi/{id}")
    public void getSickness(@Path("id") int id, Callback<Sickness> callback);
    @GET("/api/thuocapi")
    public void getDrugs(@Query("sindex") int sindex, @Query("eindex") int eindex, Callback<List<Drug>> callback);
    @GET("/api/thuocapi")
    public void searchDrug(@Query("input") String input, Callback<List<Drug>> callback);
}
