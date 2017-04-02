package com.example.younes.pollutionchecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.younes.pollutionchecker.adapter.CityAdapter;
import com.example.younes.pollutionchecker.model.GlobalObject;
import com.example.younes.pollutionchecker.services.AqicnAPI;
import com.example.younes.pollutionchecker.db.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    // Properties
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHandler db;
    private ArrayList<GlobalObject> initialCities = new ArrayList<>();
    private ArrayList<String> myDataset = new ArrayList<>();
    private FloatingActionButton ic_search;

    // Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ic_search = (FloatingActionButton) findViewById(R.id.ic_search);
        ic_search.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(i);
            }
        });

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

        // get cities from db
        db = new DatabaseHandler(this);
        db.open();
        List<GlobalObject> tmpCities = db.getAllObj();
        db.close();

        for (int i = 0; i < tmpCities.size(); i++) {
            myDataset.add("@" + tmpCities.get(i).getRxs().getObs().get(0).getMsg().getCity().getIdx());
        }

        for (int i = 0; i < myDataset.size(); i++) {
            getCityData(myDataset.get(i));
        }

        // Setup ItemTouchHelper
        ItemTouchHelper.Callback callback = new TouchHelper((CityAdapter) mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    // Methods
    /**
     * Retrieve data about a specific city
     * @param city : the city for which we get data
     */
    private void getCityData(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AqicnAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2
        AqicnAPI aqicnObj = retrofit.create(AqicnAPI.class);
        Call<GlobalObject> call = aqicnObj.getCityFeed(city, null);

        // asynchronous call
        call.enqueue(new Callback<GlobalObject>() {
            @Override
            public void onResponse(Call<GlobalObject> call, Response<GlobalObject> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)

                    GlobalObject result = response.body();
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
