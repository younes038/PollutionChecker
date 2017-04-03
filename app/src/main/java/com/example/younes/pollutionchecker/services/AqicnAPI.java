package com.example.younes.pollutionchecker.services;

import com.example.younes.pollutionchecker.model.GlobalObject;
import com.example.younes.pollutionchecker.model.GlobalSearch;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by elyandoy on 13/02/2017.
 */

public interface AqicnAPI {
    String baseURL = "https://api.waqi.info/";
    String windURL = "https://wind.waqi.info/";
    String TOKEN = "af073d16e3707f6d085660cfcd0137a61b961365";

    @GET("api/feed/{city}/obs.fr.json")
    Call<GlobalObject> getCityFeed(@Path("city") String city, @Query(TOKEN) String token);

    @GET("nsearch/station/{city}")
    Call<GlobalSearch> getStations(@Path("city") String city);

    @GET("/feed/geo:{lat};:{lng}/")
    Call<JSONObject> getNearestCity(@Path("lat") Double lat, @Path("lng") Double lng, @Query(TOKEN) String token);
}
