package com.droidko.voltfeed.ui.widget.RecyclerView.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.droidko.voltfeed.R;

public class LoadingRowViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar mProgressBar;
    public ImageView mDivider;

    public LoadingRowViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        this.mProgressBar = (ProgressBar) itemLayoutView.findViewById(R.id.timeline_loading_indicator);
        this.mDivider = (ImageView) itemLayoutView.findViewById(R.id.recycler_divider);
    }

}
