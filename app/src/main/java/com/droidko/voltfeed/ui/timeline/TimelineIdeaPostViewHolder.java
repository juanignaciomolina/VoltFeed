package com.droidko.voltfeed.ui.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidko.voltfeed.R;

public class TimelineIdeaPostViewHolder extends RecyclerView.ViewHolder {

    public TextView mContent;
    public ImageView mPostIcon;
    public ImageView mLike;
    public TextView mDate;

    public TimelineIdeaPostViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        this.mContent = (TextView) itemLayoutView.findViewById(R.id.timeline_post_idea_content);
        this.mPostIcon = (ImageView) itemLayoutView.findViewById(R.id.timeline_idea_icon);
        this.mLike = (ImageView) itemLayoutView.findViewById(R.id.timeline_post_idea_like);
        this.mDate = (TextView) itemLayoutView.findViewById(R.id.timeline_post_idea_date);

    }

}
