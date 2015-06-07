package com.droidko.voltfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.entities.Post;
import com.droidko.voltfeed.entities.TimelineRow;
import com.droidko.voltfeed.ui.holders.TimelinePostViewHolder;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerAdapter;

import java.util.ArrayList;

public class TimelineRecyclerViewAdapter extends RecyclerAdapter<TimelineRow> {

    private OnViewHolderListener mOnViewHolderListener;

    public interface OnViewHolderListener {
        void onNextPageRequired();
    }

    public void setOnViewHolderListener(OnViewHolderListener mOnViewHolderListener) {
        this.mOnViewHolderListener = mOnViewHolderListener;
    }

    // Create new views (invoked by the RecyclerAdapter)
    @Override
    public RecyclerView.ViewHolder recyclerOnCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View itemLayoutView;
        itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, null);
        return new TimelinePostViewHolder(itemLayoutView, mViewHolderClickListener);
    }

    TimelinePostViewHolder.ViewHolderClicks mViewHolderClickListener = new TimelinePostViewHolder.ViewHolderClicks() {
        @Override
        public void onClickRow(View caller) {
            Log.d(Config.LOG_DEBUG,"On Row Click");
        }

        @Override
        public void onClickLike(ImageView callerImage) {
            Log.d(Config.LOG_DEBUG, "On Like Click");
        }
    };

    // Replace the contents of a view (invoked by the RecyclerAdapter)
    @Override
    public void recyclerOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((TimelinePostViewHolder) viewHolder).mTitle.setText(getItems().get(position).getPost().getTitle());
        ((TimelinePostViewHolder) viewHolder).mContent.setText(getItems().get(position).getPost().getText());
        ((TimelinePostViewHolder) viewHolder).mImage.setImageResource(R.drawable.item_news_placeholder); //todo desharcodear esto
        if (true) ((TimelinePostViewHolder) viewHolder).mLike.setImageResource(R.drawable.likeon); //todo desharcodear esto
        else ((TimelinePostViewHolder) viewHolder).mLike.setImageResource(R.drawable.likeoff);
        ((TimelinePostViewHolder) viewHolder).mDate.setText("15m");//todo desharcodear esto

        if (mOnViewHolderListener != null && position == getItemCount() - 1 - Config.FEED_FECTH_THRESHOLD)
            mOnViewHolderListener.onNextPageRequired();
    }

    public void addPostsToRecycler(Post[] posts) {
        ArrayList<TimelineRow> mTimelineRows = new ArrayList<TimelineRow>();
        for (Post post : posts) {
            TimelineRow timelineRow = new TimelineRow();
            timelineRow.setPost(post);
            mTimelineRows.add(timelineRow);
        }
        addAllItems(mTimelineRows);
    }

}
