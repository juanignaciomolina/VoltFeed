package com.droidko.voltfeed.ui.widget.RecyclerView.modules;

import android.util.Log;

import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerAdapter;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerItem;

import java.util.ArrayList;

public class ModUtils<E extends RecyclerItem> {

    public interface ModUtilsInterface<E> {
        public int insertOrderedRow(ArrayList<Integer> itemsList, int value);

        public int removeOrderedRow(ArrayList<Integer> itemsList, int positionToRemove);
    }

    //Vars
    RecyclerAdapter<E> mRecyclerViewAdapter;

    //Constructor
    public ModUtils(RecyclerAdapter<E> aRecyclerViewAdapter) {
        this.mRecyclerViewAdapter = aRecyclerViewAdapter;
    }

    //** Start of UTILS MODULE methods **
    public static int insertOrderedRow(ArrayList<Integer> itemsList, int valueToInsert) {
        int i = 0;
        int size = itemsList.size();
        while (i < size && itemsList.get(i) < valueToInsert) {
            i++;
        }
        if (i < size) {
            itemsList.add(itemsList.get(size - 1) + 1);
            for (int j = size - 1; j > i; j--) {
                itemsList.set(j, itemsList.get(j - 1) + 1); //+1 because there is a new row
            }
            itemsList.set(i, valueToInsert);
        } else itemsList.add(valueToInsert);
        Log.d("Loaders array +", String.valueOf(itemsList));
        return i;
    }

    public static int removeOrderedRow(ArrayList<Integer> itemsList, int positionToRemove) {
        int size = itemsList.size();
        if (positionToRemove < size && positionToRemove >= 0) {
            for (int j = size - 1; j > positionToRemove; j--) {
                itemsList.set(j, itemsList.get(j) - 1); //-1 because there is one less row
            }
            itemsList.remove(positionToRemove);
        } else return -1;
        Log.d("Loaders array -", String.valueOf(itemsList));
        return positionToRemove;
    }
    //** End of UTILS MODULE methods **
}
