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
import com.droidko.voltfeed.VoltFeedApp;
import com.droidko.voltfeed.activities.MainActivity;
import com.droidko.voltfeed.api.NewsService;
import com.droidko.voltfeed.api.PostsRequestAdapter;
import com.droidko.voltfeed.entities.TimelineRow;
import com.droidko.voltfeed.entities.User;
import com.droidko.voltfeed.ui.adapters.TimelineRecyclerViewAdapter;
import com.droidko.voltfeed.utils.UiHelper;
import com.melnykov.fab.FloatingActionButton;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimelineFragment extends Fragment {

    private Activity mActivity;
    private NewsService mNewsService;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFab;
    private LinearLayout mNoPostsHolder;
    private LinearLayout mNoInternetHolder;
    private ProgressBar mProgressBar;

    private TimelineRecyclerViewAdapter mTimelineRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private User mUser;
    private int mActualPage = 0;
    private boolean mNewsLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
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
        mNoPostsHolder = (LinearLayout) mActivity.findViewById(R.id.no_news_holder);
        mNoInternetHolder = (LinearLayout) mActivity.findViewById(R.id.no_internet_holder);
        mProgressBar = (ProgressBar) mActivity.findViewById(R.id.loading_indicator);

        mLinearLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mTimelineRecyclerViewAdapter = new TimelineRecyclerViewAdapter();
        mTimelineRecyclerViewAdapter.setOnViewHolderListener(mViewHolderListener);
        mTimelineRecyclerViewAdapter.setLoaderDividerColor(getResources().getColor(R.color.item_separator));
        mRecyclerView.setAdapter(mTimelineRecyclerViewAdapter);
        // todo customize animations extending RecyclerView.ItemAnimator class
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFab.attachToRecyclerView(mRecyclerView); //this is for the mFab animation
        mFab.setOnClickListener(mFabClickListener);

        mSwipeRefreshLayout.setOnRefreshListener(mSwipeRefreshListener);
    }

    private void initApiConnection() {
        mNewsService = VoltFeedApp.getRestAdapter().create(NewsService.class);
    }

    private void populateUi() {
        mProgressBar.setVisibility(View.GONE);
        mFab.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        if (mTimelineRecyclerViewAdapter != null
            && mTimelineRecyclerViewAdapter.getItemCount() > 0)
                displayNoNews(false);
        else displayNoNews(true);
    }

    private void displayNoNews(boolean state) {
        if (state) mNoPostsHolder.setVisibility(View.VISIBLE);
        else mNoPostsHolder.setVisibility(View.GONE);
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
                if (state) mTimelineRecyclerViewAdapter.addLoadingRow();
                else mTimelineRecyclerViewAdapter.removeLoadingRow();
            }
        });
    }

    private void doGetNews() {
        mNewsLoading = true;
        mNewsService.getNews(
                mActualPage * Config.FEED_PAGE_SIZE + 1,
                Config.FEED_PAGE_SIZE,
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

    TimelineRecyclerViewAdapter.OnViewHolderListener mViewHolderListener = new TimelineRecyclerViewAdapter.OnViewHolderListener() {
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

    Callback<PostsRequestAdapter> mGetNewsCallback = new Callback<PostsRequestAdapter>() {

        @Override
        public void success(PostsRequestAdapter postsRequestAdapter, Response response) {
            //set mSwipeRefreshLayout visible BEFORE modifying
            //the recycler adapter to get a smooth animation
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mTimelineRecyclerViewAdapter.addPostsToRecycler(postsRequestAdapter.getResults());
            populateUi();
            if (postsRequestAdapter.getResults().length > 0
                && mTimelineRecyclerViewAdapter.getCurrentPosition() == mTimelineRecyclerViewAdapter.getItemCount() )
                    mRecyclerView.smoothScrollToPosition(mTimelineRecyclerViewAdapter.getCurrentPosition() + 1);
            if(mNewsLoading) mTimelineRecyclerViewAdapter.removeLoadingRow();
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
