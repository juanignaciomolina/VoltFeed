package com.droidko.voltfeed.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.droidko.voltfeed.R;

public class LoadingRowViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar mProgressBar;

    public LoadingRowViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        this.mProgressBar = (ProgressBar) itemLayoutView.findViewById(R.id.loading_indicator);
    }

}
