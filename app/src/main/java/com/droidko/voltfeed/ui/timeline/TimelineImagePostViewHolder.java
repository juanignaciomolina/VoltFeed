package com.droidko.voltfeed.ui.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class TimelineImagePostViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public ImageView mPostIcon;
    public ImageView mLike;
    public TextView mDate;
    public SimpleDraweeView mPicture;

    public TimelineImagePostViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        this.mTitle = (TextView) itemLayoutView.findViewById(R.id.timeline_post_image_title);
        this.mPostIcon = (ImageView) itemLayoutView.findViewById(R.id.timeline_post_icon);
        this.mLike = (ImageView) itemLayoutView.findViewById(R.id.timeline_post_image_like);
        this.mDate = (TextView) itemLayoutView.findViewById(R.id.timeline_post_image_date);
        this.mPicture = (SimpleDraweeView) itemLayoutView.findViewById(R.id.timeline_post_image_picture);

    }

}
