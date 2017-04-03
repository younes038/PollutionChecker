package com.example.younes.pollutionchecker.services;

import com.example.younes.pollutionchecker.model.GeolocationObject;
import com.example.younes.pollutionchecker.model.GlobalObject;
import com.example.younes.pollutionchecker.model.GlobalSearch;
import com.example.younes.pollutionchecker.model.NearestObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AqicnAPI {
    String baseURL = "https://api.waqi.info/";
    String windURL = "https://wind.waqi.info/";
    String TOKEN = "af073d16e3707f6d085660cfcd0137a61b961365";

    @GET("api/feed/{city}/obs.fr.json")
    Call<GlobalObject> getCityFeed(@Path("city") String city, @Query(TOKEN) String token);

    @GET("nsearch/station/{city}")
    Call<GlobalSearch> getStations(@Path("city") String city);

    @GET("feed/geo:{lat};{lng}/")
    Call<GeolocationObject> getNearestCity(@Path("lat") String lat, @Path("lng") String lng, @Query(TOKEN) String token);

    /*
    @GET("mapq/nearest")
    Call<NearestObject> getNearestCity();
    */
}
