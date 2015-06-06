package com.droidko.voltfeed.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.entities.News;

import java.util.ArrayList;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<News> mItemsNews = new ArrayList<News>();
    private NewsRowViewHolder mNewsRowViewHolder;
    private LoadingRowViewHolder mLoadingRowViewHolder;
    private OnViewHolderListener mOnViewHolderListener;
    private int mCurrentPos = 0;
    private ArrayList<News> mLoadingNewsBackstack = new ArrayList<News>();

    public interface OnViewHolderListener {
        void onNextPageRequired();
    }

    public void setOnViewHolderListener(OnViewHolderListener mOnViewHolderListener) {
        this.mOnViewHolderListener = mOnViewHolderListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemsNews.get(position).isLoader()) return 1;
        else return 0;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View itemLayoutView;

        switch (viewType) {
            default:
            case 0:
                itemLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_news, null);
                return new NewsRowViewHolder(itemLayoutView, mViewHolderClickListener);
            case 1:
                itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, null);
                return new LoadingRowViewHolder(itemLayoutView);
        }
    }

    NewsRowViewHolder.ViewHolderClicks mViewHolderClickListener = new NewsRowViewHolder.ViewHolderClicks() {
        @Override
        public void onClickRow(View caller) {
            Log.d(Config.LOG_DEBUG,"On Row Click");
        }

        @Override
        public void onClickLike(ImageView callerImage) {
            Log.d(Config.LOG_DEBUG, "On Like Click");
        }
    };

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            default:
            case 0:
                mNewsRowViewHolder = (NewsRowViewHolder) viewHolder;
                mNewsRowViewHolder.mTitle.setText(mItemsNews.get(position).getTitle());
                mNewsRowViewHolder.mContent.setText(mItemsNews.get(position).getText());
                mNewsRowViewHolder.mImage.setImageResource(R.drawable.item_news_placeholder); //todo desharcodear esto
                if (true) mNewsRowViewHolder.mLike.setImageResource(R.drawable.likeon); //todo desharcodear esto
                else mNewsRowViewHolder.mLike.setImageResource(R.drawable.likeoff);
                mNewsRowViewHolder.mDate.setText("15m");//todo desharcodear esto

                if (mOnViewHolderListener != null && position == getItemCount() - 1 - Config.NEWSFEED_FECTH_THRESHOLD) mOnViewHolderListener.onNextPageRequired();
            case 1:
                //Do something with the loading row if needed
        }

        mCurrentPos = position;
    }

    // Return the size of your mItemsNews (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItemsNews.size();
    }

    public int getCurrentPos() {
        return mCurrentPos;
    }

    //Dataset manipulators
    public void addNews(int position, News newsToInsert) {
        mItemsNews.add(position, newsToInsert);
        notifyItemInserted(position);
    }

    public void addNewsArray(News[] newsArray) {
        for(News item : newsArray) {
            addNews(getItemCount(), item);
        }
    }

    public void removeNewsByPos(int position) {
        mItemsNews.remove(position);
        notifyItemRemoved(position);
    }

    public int getNewsPosition(News newsToLocate) {
        return mItemsNews.indexOf(newsToLocate);
    }

    public void removeNews(News newsToRemove) {
        int pos = getNewsPosition(newsToRemove);
        if (pos >= 0) removeNewsByPos(pos);
    }

    private void moveNews(int fromPos, int toPos) {
        News temp = mItemsNews.remove(fromPos);
        mItemsNews.add(toPos, temp);
    }

    public void pushLoadingRow() {
        News news = new News();
        news.setLoader(true);
        addNews(getItemCount(), news);
        mLoadingNewsBackstack.add(news);
    }

    public void popLoadingRow() {
        if (mLoadingNewsBackstack.size() > 0) {
            removeNews(mLoadingNewsBackstack.get(mLoadingNewsBackstack.size() - 1));
            mLoadingNewsBackstack.remove(mLoadingNewsBackstack.size() - 1);
        }
    }

}
