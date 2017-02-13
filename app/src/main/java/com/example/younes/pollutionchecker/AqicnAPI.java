package com.example.younes.pollutionchecker;

import com.example.younes.pollutionchecker.model.GlobalObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by elyandoy on 13/02/2017.
 */

public interface AqicnAPI {
    String ENDPOINT = "https://api.waqi.info/api/";

    @GET("feed/{city}/obs.fr.json?token=af073d16e3707f6d085660cfcd0137a61b961365")
    Call<GlobalObject> getCity(@Path("city") String city, @Query("token") String token);

}
