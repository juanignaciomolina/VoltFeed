package com.droidko.voltfeed.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class TimelinePostViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public TextView mContent;
    public ImageView mPostIcon;
    public ImageView mLike;
    public TextView mDate;
    public SimpleDraweeView mPicture;

    public ViewHolderClicks mListener;

    public TimelinePostViewHolder(View itemLayoutView, ViewHolderClicks viewHolderClicks) {
        super(itemLayoutView);
        this.mTitle = (TextView) itemLayoutView.findViewById(R.id.timeline_post_title);
        this.mContent = (TextView) itemLayoutView.findViewById(R.id.timeline_post_content);
        this.mPostIcon = (ImageView) itemLayoutView.findViewById(R.id.timeline_post_icon);
        this.mLike = (ImageView) itemLayoutView.findViewById(R.id.timeline_post_like);
        this.mDate = (TextView) itemLayoutView.findViewById(R.id.timeline_post_date);
        this.mPicture = (SimpleDraweeView) itemLayoutView.findViewById(R.id.timeline_post_picture);

        this.mListener = viewHolderClicks;

        itemLayoutView.setOnClickListener(mRowClickListener);
        mLike.setOnClickListener(mLikeClickListener);
    }

    View.OnClickListener mLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mListener.onClickLike((ImageView) view);
        }
    };

    View.OnClickListener mRowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mListener.onClickRow(view);
        }
    };

    public static interface ViewHolderClicks {
        public void onClickRow(View caller);
        public void onClickLike(ImageView callerImage);
    }
}
