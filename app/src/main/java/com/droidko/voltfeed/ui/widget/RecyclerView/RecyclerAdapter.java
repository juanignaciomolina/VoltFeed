package com.droidko.voltfeed.ui.widget.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidko.voltfeed.ui.widget.RecyclerView.holders.LoadingRowViewHolder;
import com.droidko.voltfeed.ui.widget.RecyclerView.modules.ModDatasetManipulators;
import com.droidko.voltfeed.ui.widget.RecyclerView.modules.ModLoaders;

import java.util.ArrayList;
import java.util.Collection;


public abstract class RecyclerAdapter<E extends RecyclerItem>
        extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements
        ModDatasetManipulators.ModDatasetManipulatorsInterface<E>,
        ModLoaders.ModLoadersInterface<E> {

    //Constants
    private static final int TYPE_LOADER = 10001;

    //Vars
    private ArrayList<E> mItems = new ArrayList<E>(); //List of items for the recycler

    //Getters and Setters
    public ArrayList<E> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<E> mItems) {
        this.mItems = mItems;
    }

    //** Start of ABSTRACTS AND OVERRIDES **

    // * Return dataset's size *
    // Invoked by the layout manager (required by the RecyclerView.Adapter)
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // * Get items type *
    // Invoked by the layout manager
    @Override
    public int getItemViewType(int position) {
        if (isLoader(position)) return TYPE_LOADER;
        return recyclerGetItemViewType(position);
    }
    // Custom behaviour CAN be redefined by the child class
    public int recyclerGetItemViewType(int position) {
        return 0;
    }

    // * Create new views *
    // Invoked by the layout manager (required by the RecyclerView.Adapter)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADER) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(mModuleLoaders.getLoaderLayout(), null);
            return new LoadingRowViewHolder(itemLayoutView);
        }

        return recyclerOnCreateViewHolder(parent, viewType);
    }
    // Custom behaviour MUST be defined by the child class
    public abstract RecyclerView.ViewHolder recyclerOnCreateViewHolder(
            ViewGroup parent,
            int viewType);

    // * Replace the contents of a view *
    // Invoked by the layout manager (required by the RecyclerView.Adapter)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_LOADER) {
            LoadingRowViewHolder loadingRowViewHolder = (LoadingRowViewHolder) viewHolder;
            loadingRowViewHolder.mDivider.setBackgroundColor(
                    getModuleLoaders().getLoaderDividerColor());
            return;
        }
        getModuleDatasetManipulators().setCurrentPosition(position);
        recyclerOnBindViewHolder(viewHolder, position);
    }
    // Custom behaviour MUST be defined by the child class
    public abstract void recyclerOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    //** End of ABSTRACTS AND OVERRIDES **

    //** Start of MODULES **

    //* Start of DATASET MANIPULATORS MODULE *
    private ModDatasetManipulators<E> mModuleDatasetManipulators
            = new ModDatasetManipulators<E>(this);

    protected ModDatasetManipulators<E> getModuleDatasetManipulators() {
        return mModuleDatasetManipulators;
    }

    public void addItem(E item) {
        getModuleDatasetManipulators().addItem(item);
    }

    public void addItemToPos(int position, E item) {
        getModuleDatasetManipulators().addItemToPos(position, item);
    }

    public void addAllItems(Collection<E> itemsArray) {
        getModuleDatasetManipulators().addAllItems(itemsArray);
    }

    public void removeItemByPos(int position) {
        getModuleDatasetManipulators().removeItemByPos(position);
    }

    public int getItemPosition(E itemToLocate) {
        return getModuleDatasetManipulators().getItemPosition(itemToLocate);
    }

    public void removeItem(E itemToRemove) {
        getModuleDatasetManipulators().removeItem(itemToRemove);
    }

    public void moveItem(int fromPos, int toPos) {
        if (isLoader(fromPos)) getModuleLoaders().moveLoadingRow(fromPos, toPos);
        else getModuleDatasetManipulators().moveItem(fromPos, toPos);
    }
    public int getCurrentPosition() {
        return getModuleDatasetManipulators().getCurrentPosition();
    }
    //* End of DATASET MANIPULATORS MODULE *

    //* Start of LOADERS MODULE *
    private ModLoaders<E> mModuleLoaders = new ModLoaders<E>(this);

    public ModLoaders<E> getModuleLoaders() {
        return mModuleLoaders;
    }

    public void addLoadingRow() {
        getModuleLoaders().addLoadingRow();
    }

    public void addLoadingRow(int position) {
        getModuleLoaders().addLoadingRow(position);
    }

    public void removeLoadingRow() {
        getModuleLoaders().removeLoadingRow();
    }

    public void removeLoadingRow(int itemPosition) {
        getModuleLoaders().removeLoadingRow(itemPosition);
    }

    public void removeLoadingRowByLoaderPos(int loaderPosition) {
        getModuleLoaders().removeLoadingRowByLoaderPos(loaderPosition);
    }

    public boolean isLoader(int position) {
        return getModuleLoaders().isLoader(position);
    }

    public void setLoaderDividerColor(int color) {
        getModuleLoaders().setLoaderDividerColor(color);
    }

    public int getLoaderDividerColor() {
        return getModuleLoaders().getLoaderDividerColor();
    }

    //* End of LOADERS MODULE *

    //** End of MODULES **
}
