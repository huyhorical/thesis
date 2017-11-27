package com.kdoctor.api;

/**
 * Created by Huy on 10/23/2017.
 */

public class RestServices {
    private static RestServices restServices;
    public static RestServices getInstance(){
        return restServices == null ? new RestServices() : restServices;
    }
    public static final String URL = "http://kdoctor.somee.com/";
    private retrofit.RestAdapter restAdapter;
    private Functions functions;

    public RestServices()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        functions = restAdapter.create(Functions.class);
    }

    public Functions getServices()
    {
        return functions;
    }
}
