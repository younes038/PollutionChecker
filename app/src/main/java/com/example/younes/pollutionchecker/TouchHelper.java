package com.example.younes.pollutionchecker;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.younes.pollutionchecker.adapter.CityAdapter;

/**
 * Created by younes on 01/04/2017.
 */

public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private CityAdapter mMyAdapter;

    public TouchHelper(CityAdapter myAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mMyAdapter = myAdapter;
    }

    /**
     * Remove and delete item on swipe
     * @param viewHolder : The ViewHolder which should be updated to represent the contents
     *                   of the item at the given position in the data set
     * @param direction : direction of swipe
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mMyAdapter.swipeItem(viewHolder.getAdapterPosition());
    }

    /**
     * Drag the item to an other position in list
     * @param recyclerView : The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder : The ViewHolder which is being dragged by the user
     * @param target : The ViewHolder over which the currently active item is being dragged
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mMyAdapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }
}
