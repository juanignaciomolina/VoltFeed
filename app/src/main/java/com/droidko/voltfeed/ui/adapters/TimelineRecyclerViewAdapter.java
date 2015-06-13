package com.droidko.voltfeed.ui.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.droidko.voltfeed.utils.UiHelper;
import com.parse.ParseObject;

import org.ocpsoft.pretty.time.PrettyTime;

import java.util.ArrayList;
import java.util.List;

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
                .inflate(R.layout.timeline_post, null);
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
        UiHelper.setFontVarela(((TimelinePostViewHolder) viewHolder).mTitle);
        ((TimelinePostViewHolder) viewHolder).mContent.setText(getItems().get(position).getPost().getText());
        UiHelper.setFontVarela(((TimelinePostViewHolder) viewHolder).mContent);
        ((TimelinePostViewHolder) viewHolder).mDate.setText(
                new PrettyTime().format(getItems().get(position).getPost().getCreatedAt())
        );
        UiHelper.setFontVarela(((TimelinePostViewHolder) viewHolder).mDate);

        if (!TextUtils.isEmpty(getItems().get(position).getPost().getPicture())) {
            Uri uri;
            uri = Uri.parse(getItems().get(position).getPost().getPicture());
            UiHelper.setProgessiveFrescoImage(((TimelinePostViewHolder) viewHolder).mPicture, uri, null, true);
        }

        if (true) ((TimelinePostViewHolder) viewHolder).mLike.setImageResource(R.drawable.likeon); //todo desharcodear esto
        else ((TimelinePostViewHolder) viewHolder).mLike.setImageResource(R.drawable.likeoff);

        if (mOnViewHolderListener != null && position == getItemCount() - 1 - Config.FEED_FECTH_THRESHOLD)
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
