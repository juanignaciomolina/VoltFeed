package com.droidko.voltfeed.ui.widget.RecyclerView.modules;

import com.droidko.voltfeed.R;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerAdapter;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerItem;

import java.util.ArrayList;

public class ModLoaders<E extends RecyclerItem> extends ModDatasetManipulators<E> {

    public interface ModLoadersInterface<E> {
        public void addLoadingRow();

        public void addLoadingRow(int position);

        public void removeLoadingRow();

        public void removeLoadingRow(int itemPosition);

        public void removeLoadingRowByLoaderPos(int loaderPosition);

        public boolean isLoader(int position);
    }

    //Vars
    private ArrayList<Integer> mLoaders = new ArrayList<Integer>(); //List of item's positions
    private int mLoaderLayout = R.layout.recycler_item_loading;
    private int mLoaderDividerColor = R.color.recycler_divider;

    //Constructor
    public ModLoaders(RecyclerAdapter<E> aRecyclerViewAdapter) {
        super(aRecyclerViewAdapter);
    }

    //Getters and Setters
    public int getLoaderLayout() {
        return mLoaderLayout;
    }

    public void setLoaderLayout(int loaderLayout) {
        this.mLoaderLayout = loaderLayout;
    }

    public int getLoaderDividerColor() {
        return mLoaderDividerColor;
    }

    public void setLoaderDividerColor(int loaderDividerColor) {
        mLoaderDividerColor = loaderDividerColor;
    }

    //** Start of LOADERS MODULE methods **
    public void addLoadingRow() {
        addLoadingRow(super.getRecyclerViewAdapter().getItems().size());
    }

    @SuppressWarnings("unchecked")
    public void addLoadingRow(int position) {
        ModUtils.insertOrderedRow(mLoaders, position);
        super.addItemToPos(
                position,
                (E) new RecyclerItem());
    }

    public void removeLoadingRow() {
        removeLoadingRowByLoaderPos(mLoaders.size() - 1);
    }

    public void removeLoadingRow(int itemPosition) {
        removeLoadingRowByLoaderPos(mLoaders.indexOf(itemPosition));
    }

    public void removeLoadingRowByLoaderPos(int loaderPosition) {
        if (loaderPosition < 0) return;
        int itemPosition = mLoaders.get(loaderPosition);
        if (itemPosition >= 0 && isLoader(itemPosition)) {
            ModUtils.removeOrderedRow(mLoaders, loaderPosition);
            super.removeItemByPos(itemPosition);
        }
    }

    public void moveLoadingRow(int fromPos, int toPos) {
        if (!isLoader(fromPos)) return;
        int loaderPos = mLoaders.indexOf(fromPos);
        if (loaderPos < 0) return;
        ModUtils.removeOrderedRow(mLoaders, loaderPos);
        ModUtils.insertOrderedRow(mLoaders, toPos);
        super.moveItem(fromPos, toPos);
    }

    public boolean isLoader(int position) {
        for (int aLoaderPosition : mLoaders) {
            if (aLoaderPosition == position) return true;
        }
        return false;
    }
    //** End of LOADERS MODULE methods **
}
