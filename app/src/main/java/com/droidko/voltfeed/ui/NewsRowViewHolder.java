package com.droidko.voltfeed.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class NewsRowViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public TextView mContent;
    public RoundedImageView mImage;
    public ImageView mLike;
    public TextView mDate;

    public ViewHolderClicks mListener;

    public NewsRowViewHolder(View itemLayoutView, ViewHolderClicks viewHolderClicks) {
        super(itemLayoutView);
        this.mTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
        this.mContent = (TextView) itemLayoutView.findViewById(R.id.item_content);
        this.mImage = (RoundedImageView) itemLayoutView.findViewById(R.id.item_image);
        this.mLike = (ImageView) itemLayoutView.findViewById(R.id.item_like);
        this.mDate = (TextView) itemLayoutView.findViewById(R.id.item_date);

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
