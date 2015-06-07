package com.droidko.voltfeed.ui.widget.RecyclerView.modules;

import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerAdapter;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerItem;

import java.util.Collection;


public class ModDatasetManipulators<E extends RecyclerItem> {

    public interface ModDatasetManipulatorsInterface<E> {
        public void addItem(E item);

        public void addItemToPos(int position, E item);

        public void addAllItems(Collection<E> itemsArray);

        public void removeItemByPos(int position);

        public int getItemPosition(E itemToLocate);

        public void removeItem(E itemToRemove);

        public void moveItem(int fromPos, int toPos);
    }

    //Vars
    RecyclerAdapter<E> mRecyclerViewAdapter;
    private int mCurrentPosition = 0;

    //Constructor
    public ModDatasetManipulators(RecyclerAdapter<E> aRecyclerViewAdapter) {
        this.mRecyclerViewAdapter = aRecyclerViewAdapter;
    }

    // Getters and Setters
    public RecyclerAdapter<E> getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

    public void setRecyclerViewAdapter(RecyclerAdapter<E> mRecyclerViewAdapter) {
        this.mRecyclerViewAdapter = mRecyclerViewAdapter;
    }

    //** Start of DATASET MANIPULATORS MODULE methods **
    public void addItem(E item) {
        addItemToPos(mRecyclerViewAdapter.getItemCount(), item);
    }

    public void addItemToPos(int position, E item) {
        if (!isAvailablePosition(position)) return;
        mRecyclerViewAdapter.getItems().add(position, item);
        mRecyclerViewAdapter.notifyItemInserted(position);
    }

    public void addAllItems(Collection<E> itemsArray) {
        for (E item : itemsArray) {
            addItem(item);
        }
    }

    public void removeItemByPos(int position) {
        if (!isInListRange(position)) return;
        mRecyclerViewAdapter.getItems().remove(position);
        mRecyclerViewAdapter.notifyItemRemoved(position);
    }

    public int getItemPosition(E itemToLocate) {
        return mRecyclerViewAdapter.getItems().indexOf(itemToLocate);
    }

    public void removeItem(E itemToRemove) {
        int position = getItemPosition(itemToRemove);
        removeItemByPos(position);
    }

    public void moveItem(int fromPos, int toPos) {
        if (!isInListRange(fromPos)) return;
        if (!isInListRange(toPos)) return;
        E temp = mRecyclerViewAdapter.getItems().remove(fromPos);
        mRecyclerViewAdapter.getItems().add(toPos, temp);
    }

    public boolean isInListRange(int position) {
        return position >= 0
                && position <= (mRecyclerViewAdapter.getItemCount() - 1);
    }

    public boolean isAvailablePosition(int position) {
        return position >= 0
                && position <= mRecyclerViewAdapter.getItemCount();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
    }
    //** End of DATASET MANIPULATORS MODULE methods **
}
