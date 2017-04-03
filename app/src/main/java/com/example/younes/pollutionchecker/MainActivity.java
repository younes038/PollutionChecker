package com.example.younes.pollutionchecker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.location.Location;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.younes.pollutionchecker.adapter.CityAdapter;
import com.example.younes.pollutionchecker.model.DataGeo;
import com.example.younes.pollutionchecker.model.GeolocationObject;
import com.example.younes.pollutionchecker.model.GlobalObject;
import com.example.younes.pollutionchecker.model.NearestObject;
import com.example.younes.pollutionchecker.services.AqicnAPI;
import com.example.younes.pollutionchecker.db.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // Properties
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHandler db;
    private ArrayList<GlobalObject> initialCities = new ArrayList<>();
    private FloatingActionButton ic_search;
    private Criteria criteria;
    private LocationListener listener;
    private TextView mGeoText;
    private JSONObject json;

    private LocationManager locationManager;
    public Location location;

    // Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGeoText = (TextView) findViewById(R.id.nearest_point);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        ic_search = (FloatingActionButton) findViewById(R.id.ic_search);

        ic_search.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // Get the location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getClosestLocation(location);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configGeoloc();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CityAdapter(this, initialCities);
        mRecyclerView.setAdapter(mAdapter);

        // get cities from db...
        db = new DatabaseHandler(this);
        db.open();
        List<GlobalObject> tmpCities = db.getAllObj();
        db.close();
        // ... and add to adapter to diplay them
        int i = 0;
        for (final GlobalObject obj : tmpCities) {
            getCityData("@" + obj.getRxs().getObs().get(0).getMsg().getCity().getIdx());
        }

        // Setup ItemTouchHelper
        ItemTouchHelper.Callback callback = new TouchHelper((CityAdapter) mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configGeoloc();
                break;
            default:
                break;
        }
    }

    void configGeoloc() {
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        //criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        //noinspection MissingPermission
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }

    // Methods
    /**
     * Retrieve data about a specific city
     * @param city : the city for which we get data
     */
    public void getCityData(String city) {
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

    /**
     * Get the nearest pollution point based on user position
     * @param loc : latitude and longitude of position
     * note : ne fonctionnait pas avec Retrofit 2.0
     */
    private void getClosestLocation(Location loc) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url= "https://api.waqi.info/feed/geo:"+loc.getLatitude()+";"+loc.getLongitude()+"/?token=af073d16e3707f6d085660cfcd0137a61b961365";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            json = new JSONObject(response);
                            String nrstPoint = json.getJSONObject("data").getJSONObject("city").getString("name");
                            mGeoText.setText(mGeoText.getText() + nrstPoint);

                        } catch (JSONException e) {}

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
