package com.droidko.voltfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.entities.Post;
import com.droidko.voltfeed.entities.TimelineRow;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerAdapter;
import com.droidko.voltfeed.utils.TimelineHelper;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineRecyclerViewAdapter extends RecyclerAdapter<TimelineRow> {

    private OnViewHolderListener mOnViewHolderListener;
    private TimelineHelper mTimelineHelper = new TimelineHelper();

    public interface OnViewHolderListener {
        void onNextPageRequired();
    }

    public void setOnViewHolderListener(OnViewHolderListener mOnViewHolderListener) {
        this.mOnViewHolderListener = mOnViewHolderListener;
    }

    // Get item view type (invoked by the customized RecyclerAdapter)
    @Override
    public int recyclerGetItemViewType(int position) {
        return getItems().get(position).getPost().getType();
    }

    // Create new views (invoked by the RecyclerAdapter)
    @Override
    public RecyclerView.ViewHolder recyclerOnCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        return mTimelineHelper.getTimelineViewHolder(viewType, parent.getContext());
    }

    // Replace the contents of a view (invoked by the RecyclerAdapter)
    @Override
    public void recyclerOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        mTimelineHelper.populateTimelineViewHolder(getItems().get(position).getPost(), viewHolder);

        if (mOnViewHolderListener != null
                && position == getItemCount() - 1 - Config.FEED_FECTH_THRESHOLD)
            mOnViewHolderListener.onNextPageRequired();
    }

    public void addPostsToRecycler(List<ParseObject> list) {
        ArrayList<TimelineRow> mTimelineRows = new ArrayList<TimelineRow>();
        for (ParseObject parseObject : list) {
            TimelineRow timelineRow = new TimelineRow();
            Post post = new Post(parseObject);
            timelineRow.setPost(post);
            mTimelineRows.add(timelineRow);
        }
        addAllItems(mTimelineRows);
    }

}
