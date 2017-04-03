package com.example.younes.pollutionchecker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.younes.pollutionchecker.adapter.SearchAdapter;
import com.example.younes.pollutionchecker.db.DatabaseHandler;
import com.example.younes.pollutionchecker.model.GlobalSearch;
import com.example.younes.pollutionchecker.model.ResultSearch;
import com.example.younes.pollutionchecker.model.SearchObject;
import com.example.younes.pollutionchecker.services.AqicnAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity that handles search and adding of new cities
 * Created by younes on 01/04/2017.
 */

public class SearchActivity extends AppCompatActivity {

    // Properties
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<SearchObject> searchedCities = new ArrayList<SearchObject>();
    private DatabaseHandler db;
    SearchView mSearchView;
    TextView emptyView;

    // Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        /*
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        */

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        emptyView = (TextView) findViewById(R.id.empty_search);

        // add an event listener on search view
        final Context ctx = this;
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // do something when text changes
                searchedCities = new ArrayList<SearchObject>();
                mAdapter = new SearchAdapter(ctx, searchedCities);
                mRecyclerView.setAdapter(mAdapter);
                searchCity(mSearchView.getQuery().toString());

                return false;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // set list item divider
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        /*getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Expand the search view and request focus
        searchItem.expandActionView();
        searchView.requestFocus();
        searchView.setOnQueryTextListener(this);*/

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle click on item of action bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // User chose the "Search" action
                Toast.makeText(getApplicationContext(), "Recherche", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // Methods
    /**
     *
     */
    private void searchCity(String inputSearch) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AqicnAPI.windURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2
        AqicnAPI aqicnObj = retrofit.create(AqicnAPI.class);
        Call<GlobalSearch> call = aqicnObj.getStations(inputSearch);

        //asynchronous call
        call.enqueue(new Callback<GlobalSearch>() {
            @Override
            public void onResponse(Call<GlobalSearch> call, Response<GlobalSearch> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    try {
                        ArrayList<ResultSearch> result = response.body().getResults();
                        for (int i = 0; i < result.size(); i++) {
                            searchedCities.add(new SearchObject(result.get(i).getX(), result.get(i).getN().get(0)));
                        }
                        showLayout(searchedCities.size());
                        mAdapter.notifyDataSetChanged();
                    } catch(Exception e) {

                    }

                } else {
                    // response received but request not successful (like 400,401,403 etc)
                    // Handle errors
                }
            }

            @Override
            public void onFailure(Call<GlobalSearch> call, Throwable t) {
                // something went completely south (like no internet connection)
            }
        });
    }

    //Methods
    /**
     * Displays either the recyclerview
     * or the textview depending on dataset size
     * @param number : size of dataset
     */
    private void showLayout(int number) {
        if (number == 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public static void goBack(SearchObject sObj) {
        /*Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        //Get the value of the item you clicked
        intent.putExtra("cityIdx", mtList.get(pos).getIdx());
        intent.putExtra("cityName", mtList.get(pos).getCityName());
        startActivity(intent);*/
    }

}
