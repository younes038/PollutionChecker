package com.example.younes.pollutionchecker.services;

import com.example.younes.pollutionchecker.model.GlobalObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by elyandoy on 13/02/2017.
 */

public interface AqicnAPI {
    String baseURL = "https://api.waqi.info/";

    @GET("search/")
    Call<GlobalObject> getCityData(@Query("keyword") String city, @Query("token") String token);

    @GET("api/feed/{city}/obs.fr.json")
    Call<GlobalObject> getCityFeed(@Path("city") String city, @Query("token") String token);

}
