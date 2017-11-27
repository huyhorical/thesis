package com.kdoctor.api;

/**
 * Created by Huy on 10/23/2017.
 */

public class RestAIServices {
    private static RestAIServices restServices;
    public static RestAIServices getInstance(){
        //return restServices == null ? new RestAIServices() : restServices;
        return new RestAIServices();
    }
    private static final String URL = "http://kdoctor.somee.com/";
    private retrofit.RestAdapter restAdapter;
    private AIFunctions functions;

    public RestAIServices()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        functions = restAdapter.create(AIFunctions.class);
    }

    public AIFunctions getServices()
    {
        return functions;
    }
}
