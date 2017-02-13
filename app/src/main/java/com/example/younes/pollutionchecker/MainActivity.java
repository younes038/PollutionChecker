package com.example.younes.pollutionchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.younes.pollutionchecker.adapter.CityAdapter;
import com.example.younes.pollutionchecker.model.GlobalObject;
import com.example.younes.pollutionchecker.services.AqicnAPI;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<GlobalObject> initialCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CityAdapter(this, initialCities);
        mRecyclerView.setAdapter(mAdapter);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AqicnAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2
        AqicnAPI aqicnObj = retrofit.create(AqicnAPI.class);
        Call<GlobalObject> call = aqicnObj.getCityFeed("@3067", "af073d16e3707f6d085660cfcd0137a61b961365");

        //asynchronous call
        call.enqueue(new Callback<GlobalObject>() {
            @Override
            public void onResponse(Call<GlobalObject> call, Response<GlobalObject> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GlobalObject result = response.body();
                    //mTextView.setText(result.getRxs().getObs().get(0).getMsg().getCity().getName());
                    initialCities.add(result);
                    mAdapter.notifyDataSetChanged();
                } else {
                    // response received but request not successful (like 400,401,403 etc)
                    // Handle errors
                }
            }
            @Override
            public void onFailure(Call<GlobalObject> call, Throwable t) {
                // something went completely south (like no internet connection)
            }
        });
    }
}
