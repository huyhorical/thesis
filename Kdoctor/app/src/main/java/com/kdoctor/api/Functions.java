package com.kdoctor.api;

import com.kdoctor.models.CodeItem;
import com.kdoctor.models.CodeItemGet;
import com.kdoctor.models.Drug;
import com.kdoctor.models.Sickness;
import com.kdoctor.models.SicknessCategory;
import com.kdoctor.models.Vaccine;
import com.kdoctor.models.VaccineCenter;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
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
    @GET("/api/AnswerAPI")
    public void getCodeItems(@Query("dmbenh") String input, Callback<List<CodeItem>> callback);


    @GET("/api/{category}")
    public void getCode(@Path("category") String category, @Query("code") String code, Callback<CodeItemGet> callback);

    @POST("/api/{category}")
    public void postCode(@Path("category") String category, @Body HashMap<String, String> data, Callback<String> callback);
    @PUT("/api/{category}")
    public void putCode(@Path("category") String category, @Body HashMap<String, String> data, Callback<String> callback);

    @GET("/api/DiaDiemTCAPI")
    public void getVaccineCenter(@Query("bankinh") double radii, @Query("vido") double lat, @Query("kinhdo") double lon, Callback<List<VaccineCenter>> callback);
    @GET("/api/DiaDiemTCAPI")
    public void getNearestVaccineCenter(@Query("vido") double lat, @Query("kinhdo") double lon, Callback<VaccineCenter> callback);

    @GET("/api/BVNhiAPI")
    public void getHospital(@Query("bankinh") double radii, @Query("vido") double lat, @Query("kinhdo") double lon, Callback<List<VaccineCenter>> callback);
    @GET("/api/BVNhiAPI")
    public void getNearestHospital(@Query("vido") double lat, @Query("kinhdo") double lon, Callback<VaccineCenter> callback);
}
