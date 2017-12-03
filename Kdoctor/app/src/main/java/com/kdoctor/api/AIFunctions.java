package com.kdoctor.api;

import com.kdoctor.models.Vaccine;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by Huy on 10/23/2017.
 */

public interface AIFunctions {
    @GET("/{category}/")
    void getQuestion(@Path("category") String category, @Query("input") String value, Callback<String> callback);
}
