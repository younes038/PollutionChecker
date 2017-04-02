package com.example.younes.pollutionchecker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.younes.pollutionchecker.R;
import com.example.younes.pollutionchecker.SearchActivity;
import com.example.younes.pollutionchecker.db.DatabaseHandler;
import com.example.younes.pollutionchecker.model.SearchObject;

import java.util.ArrayList;

/**
 * Binds data with the view that represents them
 * Created by younes on 02/04/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    // Properties
    private ArrayList<SearchObject> mtList ;
    public Context mcontext;
    private DatabaseHandler db;
    ViewHolder viewHolder;

    // initializes some private fields to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView city;

        public ViewHolder(View v) {
            super(v);
            city = (TextView) v.findViewById(R.id.search_item);
        }
    }

    public SearchAdapter(Context context, ArrayList<SearchObject> list) {
        mtList = list;
        mcontext = context;
        db = new DatabaseHandler(context);
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, null);
        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position ) {
        viewHolder.city.setText(mtList.get(position).getCityName());
        final int pos = position;
        viewHolder.city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                db.add(mtList.get(pos));
                db.close();
                Toast.makeText(mcontext, viewHolder.city.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return mtList.size();
    }
}