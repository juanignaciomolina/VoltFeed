package com.droidko.voltfeed.ui.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.droidko.voltfeed.R;

public class TimelineIdeaPostViewHolder extends RecyclerView.ViewHolder {

    public TextView mContent;
    public View mVoltButton;
    public TextView mVoltsCounter;
    public TextView mDate;
    public View mCard;

    public TimelineIdeaPostViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        this.mContent = (TextView) itemLayoutView.findViewById(R.id.timeline_post_idea_content);
        this.mVoltButton = itemLayoutView.findViewById(R.id.timeline_post_idea_volt_button);
        this.mVoltsCounter = (TextView) itemLayoutView.findViewById(R.id.timeline_post_idea_volt_count);
        this.mDate = (TextView) itemLayoutView.findViewById(R.id.timeline_post_idea_date);
        this.mCard = itemLayoutView.findViewById(R.id.timeline_post_idea_card);
    }

}
