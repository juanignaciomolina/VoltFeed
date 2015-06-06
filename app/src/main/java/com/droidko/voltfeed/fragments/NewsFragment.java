package com.droidko.voltfeed.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.TrainingApp;
import com.droidko.voltfeed.activities.MainActivity;
import com.droidko.voltfeed.api.NewsRequestAdapter;
import com.droidko.voltfeed.api.NewsService;
import com.droidko.voltfeed.entities.User;
import com.droidko.voltfeed.ui.NewsRecyclerViewAdapter;
import com.droidko.voltfeed.utils.UiHelper;
import com.melnykov.fab.FloatingActionButton;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewsFragment extends Fragment {

    private Activity mActivity;
    private NewsService mNewsService;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFab;
    private LinearLayout mNoNewsHolder;
    private LinearLayout mNoInternetHolder;
    private ProgressBar mProgressBar;

    private NewsRecyclerViewAdapter mNewsRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private User mUser;
    private int mActualPage = 0;
    private boolean mNewsLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        initUi();
        initApiConnection();

        setLoadingUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initUi() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.recycler_view_news);
        mFab = (FloatingActionButton) mActivity.findViewById(R.id.fab);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mActivity.findViewById(R.id.swipe_refresh_layout);
        mNoNewsHolder = (LinearLayout) mActivity.findViewById(R.id.no_news_holder);
        mNoInternetHolder = (LinearLayout) mActivity.findViewById(R.id.no_internet_holder);
        mProgressBar = (ProgressBar) mActivity.findViewById(R.id.loading_indicator);

        mLinearLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mNewsRecyclerViewAdapter= new NewsRecyclerViewAdapter();
        mNewsRecyclerViewAdapter.setOnViewHolderListener(mViewHolderListener);
        mRecyclerView.setAdapter(mNewsRecyclerViewAdapter);
        // todo customize animations extending RecyclerView.ItemAnimator class
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFab.attachToRecyclerView(mRecyclerView); //this is for the mFab animation
        mFab.setOnClickListener(mFabClickListener);

        mSwipeRefreshLayout.setOnRefreshListener(mSwipeRefreshListener);
    }

    private void initApiConnection() {
        mNewsService = TrainingApp.getRestAdapter().create(NewsService.class);
    }

    private void populateUi() {
        mProgressBar.setVisibility(View.GONE);
        mFab.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        if (mNewsRecyclerViewAdapter != null
            && mNewsRecyclerViewAdapter.getItemCount() > 0)
                displayNoNews(false);
        else displayNoNews(true);
    }

    private void displayNoNews(boolean state) {
        if (state) mNoNewsHolder.setVisibility(View.VISIBLE);
        else mNoNewsHolder.setVisibility(View.GONE);
    }

    private void displayNoInternet(boolean state) {
        if (state) {
            mNoInternetHolder.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
        else mNoInternetHolder.setVisibility(View.GONE);
    }

    private void setLoadingUi() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        displayNoNews(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void setLoadingRowFromBackground(final boolean state) {
        //This is important: You can't notify the adapter from changes in the dataset
        //from a non UI thread. So if we want to make changes in the dataset from a callback
        //that runs on the background (on a different thread) we MUST implement a handler
        android.os.Handler mHandler = mActivity.getWindow().getDecorView().getHandler();
        mHandler.post(new Runnable() {
            public void run(){
                //change adapter contents
                if (state) mNewsRecyclerViewAdapter.pushLoadingRow();
                else mNewsRecyclerViewAdapter.popLoadingRow();
            }
        });
    }

    private void doGetNews() {
        mNewsLoading = true;
        mNewsService.getNews(
                mActualPage * Config.NEWSFEED_PAGE_SIZE + 1,
                Config.NEWSFEED_PAGE_SIZE,
                mGetNewsCallback);
        mActualPage ++;
    }

    private void refreshNews() {
        // Load items
        // ...

        // Load complete
        onRefreshNewsComplete();
    }

    private void onRefreshNewsComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // ** EVENT BUS **

    public void onEvent(MainActivity.LogInEvent event){
        this.mUser = event.mUser;
        doGetNews();
    }

    public void onEvent(MainActivity.NoInternetEvent event){
        displayNoNews(false);
        displayNoInternet(true);
    }

    // ** End of EVENT BUS **

    // ** ANONYMOUS CLASSES **

    NewsRecyclerViewAdapter.OnViewHolderListener mViewHolderListener = new NewsRecyclerViewAdapter.OnViewHolderListener() {
        @Override
        public void onNextPageRequired() {
            if (!mNewsLoading) {
                setLoadingRowFromBackground(true);
                doGetNews();
            }
        }
    };

    View.OnClickListener mFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UiHelper.showToast(mActivity, "FAB Click");
        }
    };

    SwipeRefreshLayout.OnRefreshListener mSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // Refresh items
            refreshNews();
        }
    };

    Callback<NewsRequestAdapter> mGetNewsCallback = new Callback<NewsRequestAdapter>() {

        @Override
        public void success(NewsRequestAdapter newsRequestAdapter, Response response) {
            //set mSwipeRefreshLayout visible BEFORE modifying
            //the recycler adapter to get a smooth animation
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mNewsRecyclerViewAdapter.addNewsArray(newsRequestAdapter.getResults());
            populateUi();
            if (newsRequestAdapter.getResults().length > 0
                && mNewsRecyclerViewAdapter.getCurrentPos() == mNewsRecyclerViewAdapter.getItemCount() )
                    mRecyclerView.smoothScrollToPosition(mNewsRecyclerViewAdapter.getCurrentPos() + 1);
            if(mNewsLoading) mNewsRecyclerViewAdapter.popLoadingRow();
            mNewsLoading = false;
            setLoadingRowFromBackground(false);
        }

        @Override
        public void failure(RetrofitError error) {
            UiHelper.showToast(mActivity, error.getMessage());
        }
    };

    // ** End of ANONYMOUS CLASSES **
}
