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

    private PaginationListener mPaginationListener;
    private TimelineHelper mTimelineHelper = new TimelineHelper();

    private Post mFirstPost;
    private Post mLastPost;

    public interface PaginationListener {
        void onPreviousPageRequired();
        void onNextPageRequired();
    }

    public void setPaginationListener(PaginationListener mPaginationListener) {
        this.mPaginationListener = mPaginationListener;
    }

    public Post getFirstPost() {
        return mFirstPost;
    }

    public Post getLastPost() {
        return mLastPost;
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

        if (mPaginationListener != null) {
            //Aproaching end of dataset -> require next page
            if (position == getItemCount() - 1 - Config.FEED_FECTH_THRESHOLD)
                mPaginationListener.onNextPageRequired();
            //Aproaching start of dataset -> requiere previous page
            else if (position == Config.FEED_FECTH_THRESHOLD)
                mPaginationListener.onPreviousPageRequired();
        }
    }

    public void addPostToRecyclerStart(Post post) {
        TimelineRow timelineRow = new TimelineRow();
        timelineRow.setPost(post);
        addItemToPos(0, timelineRow);
        mFirstPost = post;
        if (mLastPost == null) mLastPost = post;
    }

    public void addPostsListToRecyclerStart(List<ParseObject> list) {
        for (ParseObject parseObject : list) {
            addPostToRecyclerStart(new Post(parseObject));
        }
    }

    public void addPostsListToRecyclerEnd(List<ParseObject> list) {
        ArrayList<TimelineRow> timelineRowsList = new ArrayList<TimelineRow>();
        for (ParseObject parseObject : list) {
            TimelineRow timelineRow = new TimelineRow();
            Post post = new Post(parseObject);
            timelineRow.setPost(post);
            timelineRowsList.add(timelineRow);
            if (mFirstPost == null) mFirstPost = post;
            mLastPost = post;
        }
        addAllItems(timelineRowsList);
    }

}
