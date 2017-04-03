package com.example.younes.pollutionchecker.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.younes.pollutionchecker.DetailsActivity;
import com.example.younes.pollutionchecker.MainActivity;
import com.example.younes.pollutionchecker.R;
import com.example.younes.pollutionchecker.db.DatabaseHandler;
import com.example.younes.pollutionchecker.model.GlobalObject;
import com.example.younes.pollutionchecker.model.MessageObject;
import com.example.younes.pollutionchecker.services.AqicnAPI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Binds data with the view that represents them
 * Created by elyandoy on 13/02/2017.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    //Properties
    private Context mCtx;
    private ArrayList<GlobalObject> mDataset;
    private DatabaseHandler db;

    /**
     * Provide a suitable constructor
     * @param ctx
     * @param initialCities
     */
    public CityAdapter(Context ctx, ArrayList<GlobalObject> initialCities) {
        this.mCtx = ctx;
        this.mDataset = initialCities;
        this.db = new DatabaseHandler(ctx);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView polIndex, global, gps, max, min, lastUpdate;
        public ImageButton refresh;
        public Context mCtx;
        public ArrayList<GlobalObject> mDataset;

        public ViewHolder(View v, Context ctx, ArrayList<GlobalObject> dataset) {
            super(v);
            this.mCtx = ctx;
            this.mDataset = dataset;
            v.setOnClickListener(this);
            polIndex = (TextView) v.findViewById(R.id.polIndex);
            global = (TextView) v.findViewById(R.id.globalinfo);
            /*gps = (TextView) v.findViewById(R.id.gps);
            max = (TextView) v.findViewById(R.id.pm10Max);
            min = (TextView) v.findViewById(R.id.pm10Min);*/
            lastUpdate = (TextView) v.findViewById(R.id.lastUpdate);
            refresh = (ImageButton) v.findViewById(R.id.refresh);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(this.mCtx, DetailsActivity.class);
            // Pass values of the clicked item
            intent.putExtra("aqi", mDataset.get(position).getRxs().getObs().get(0).getMsg().getAqi());
            this.mCtx.startActivity(intent);
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param parent : The ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType : view type of the new View
     * @return
     */
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(v, mCtx, mDataset);
        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder : The ViewHolder which should be updated to represent the contents
     *               of the item at the given position in the data set
     * @param position : position in dataset
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that elementpublic Button mButton;
        MessageObject msg = mDataset.get(position).getRxs().getObs().get(0).getMsg();

        if(msg.getAqi() < 50) {
            holder.polIndex.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.green));
        } else if(msg.getAqi() < 100) {
            holder.polIndex.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.orange));
        } else {
            holder.polIndex.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.red));
        }

        holder.polIndex.setText(String.valueOf(msg.getAqi()));
        //holder.max.setText(msg.getIaqi().get(i).getV().get(2).toString());
        //holder.min.setText(msg.getIaqi().get(i).getV().get(1).toString());

        String city = msg.getCity().getName();
        if(city.length()>20) {
            city = city.substring(0,20)+"...";
        }
        holder.global.setText(city+" "+msg.getIaqi().get(0).getV().get(0)+"°C");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(msg.getTimestamp()*1000);
        holder.lastUpdate.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        //holder.gps.setText(msg.getCity().getGeo()[0].substring(0,6)+", "+msg.getCity().getGeo()[1].substring(0,6));
        final String ncity = city;
        holder.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, mCtx.getResources().getString(R.string.msg_refresh) + ncity, Toast.LENGTH_SHORT).show();
                updateCityData(String.valueOf(mDataset.get(0).getRxs().getObs().get(0).getMsg().getCity().getIdx()), position);
            }
        });
    }

    public void updateCityData(String city, final int pos) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AqicnAPI.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newFixedThreadPool(1))
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
                    mDataset.get(1).setDt(result.getDt());
                    mDataset.get(1).setRxs(result.getRxs());
                    notifyDataSetChanged();
                } else {}
            }
            @Override
            public void onFailure(Call<GlobalObject> call, Throwable t) {
                // something went completely south (like no internet connection)
            }
        });
    }

    /**
     * Count items of dataset
     * @return int : the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //Methods
    /**
     * Removes and delete a list item from view and database
     * @param pos : the position of the item in list
     */
    public void swipeItem(int pos) {
        db.open();
        db.removeObj(mDataset.get(pos));
        db.close();
        mDataset.remove(pos);
        notifyItemRemoved(pos);
    }

    /**
     * Allows to drag item from list to a specific position
     * @param viewHolderPos : the index of one element to be swapped
     * @param targetPos : the index of the other element to be swapped
     */
    public void moveItem(int viewHolderPos, int targetPos){
        Collections.swap(mDataset, viewHolderPos, targetPos);
        notifyItemMoved(viewHolderPos, targetPos);
    }
}
