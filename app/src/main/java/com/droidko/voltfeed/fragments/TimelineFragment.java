package com.droidko.voltfeed.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.droidko.voltfeed.activities.MainActivity;
import com.droidko.voltfeed.events.innerEvents.OnNoConnection;
import com.droidko.voltfeed.events.innerEvents.OnPublishEvent;
import com.droidko.voltfeed.events.innerEvents.OnVoltsPostsUpdate;
import com.droidko.voltfeed.ui.QuickReturnAnimation;
import com.droidko.voltfeed.ui.adapters.TimelineRecyclerViewAdapter;
import com.droidko.voltfeed.utils.ApiHelper;
import com.droidko.voltfeed.utils.UiHelper;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

public class TimelineFragment extends Fragment {

    private MainActivity mActivity;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFab;
    private LinearLayout mNoPostsHolder;
    private LinearLayout mNoInternetHolder;
    private ProgressBar mProgressBar;
    private TextView mMotd;

    private TimelineRecyclerViewAdapter mTimelineRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean mNewsLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = (MainActivity) getActivity();

        initUi();
        setLoadingUi();
        doGetOlderPosts();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initUi() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.timeline_recycler_view_news);
        mFab = (FloatingActionButton) mActivity.findViewById(R.id.timeline_fab);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mActivity.findViewById(R.id.timeline_swipe_refresh_layout);
        mNoPostsHolder = (LinearLayout) mActivity.findViewById(R.id.timeline_no_news_holder);
        mNoInternetHolder = (LinearLayout) mActivity.findViewById(R.id.timeline_no_internet_holder);
        mProgressBar = (ProgressBar) mActivity.findViewById(R.id.timeline_loading_indicator);
        mMotd = (TextView) mActivity.findViewById(R.id.timeline_message_of_the_day);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.pallete_green_dark,
                R.color.pallete_green);

        mMotd.setText(UiHelper.getMessageOfTheDay());

        mSwipeRefreshLayout.setOnRefreshListener(mSwipeRefreshListener);

        mProgressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.pallete_red),
                android.graphics.PorterDuff.Mode.SRC_IN);

        mLinearLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        setTimelineRecyclerViewAdapter();
        mFab.setOnClickListener(mFabClickListener);

        QuickReturnAnimation quickReturnAnimation = new QuickReturnAnimation(
                mActivity.getQuickReturnWrapper(),
                mActivity.getToolbar(),
                mActivity.getTabs());
        mRecyclerView.addOnScrollListener(quickReturnAnimation);
        mFab.attachToRecyclerView(mRecyclerView);

    }

    private void setTimelineRecyclerViewAdapter() {
        mTimelineRecyclerViewAdapter = new TimelineRecyclerViewAdapter();
        mTimelineRecyclerViewAdapter.setPaginationListener(mViewHolderListener);
        mTimelineRecyclerViewAdapter
                .setLoaderDividerColor(getResources().getColor(R.color.white_4));

        mRecyclerView.setAdapter(mTimelineRecyclerViewAdapter);
        //TODO (1) customize animations extending RecyclerView.ItemAnimator class
    }

    private void populateUi() {
        mProgressBar.setVisibility(View.GONE);
        mMotd.setVisibility(View.GONE);
        mFab.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        if (mTimelineRecyclerViewAdapter != null
            && mTimelineRecyclerViewAdapter.getItemCount() > 0)
                displayNoPosts(false);
        else displayNoPosts(true);
    }

    private void displayNoPosts(boolean state) {
        if (state) mNoPostsHolder.setVisibility(View.VISIBLE);
        else mNoPostsHolder.setVisibility(View.GONE);
    }

    private void displayNoInternet(boolean state) {
        if (state) {
            mNoInternetHolder.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mMotd.setVisibility(View.GONE);
        } else mNoInternetHolder.setVisibility(View.GONE);
    }

    private void setLoadingUi() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        displayNoPosts(false);
        mProgressBar.setVisibility(View.VISIBLE);
        mMotd.setVisibility(View.VISIBLE);
    }

    private void setLoadingRowFromBackground(final boolean state, @Nullable final Integer position) {
        android.os.Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                if (state) {
                    if (position != null) mTimelineRecyclerViewAdapter.addLoadingRow(position);
                    else mTimelineRecyclerViewAdapter.addLoadingRow();
                }
                else {
                    if (position != null) mTimelineRecyclerViewAdapter.removeLoadingRow(position);
                    mTimelineRecyclerViewAdapter.removeLoadingRow();
                }
            }
        });
    }

    private void doGetOlderPosts() {
        mNewsLoading = true;
        Date latestDate = null;
        if (mTimelineRecyclerViewAdapter.getLastPost() != null) latestDate =
                mTimelineRecyclerViewAdapter.getLastPost().getCreatedAt();
        ApiHelper.getOlderTimelinePosts(
                latestDate,
                mGetOlderPostsCallback);
    }

    private void doGetNewerPosts() {
        mNewsLoading = true;
        ApiHelper.getNewerTimelinePosts(
                mTimelineRecyclerViewAdapter.getFirstPost().getCreatedAt(),
                mGetNewerPostsCallback);
    }

    private void onRefreshNewsComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // ** Start of EVENT BUS **

    public void onEvent(OnNoConnection event){
        if (this.isVisible()) {
            displayNoPosts(false);
            displayNoInternet(true);
        }
    }

    public void onEvent(OnPublishEvent event){
        doGetNewerPosts();
    }

    public void onEvent(OnVoltsPostsUpdate event) {
        /*TODO (2) As of RecyclerView 22.2.0 there seems to be a bug where notifing the adapater
        of a dataset change while the recycler is scrolling may cause a fatal error*/
        /*
        android.os.Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                mTimelineRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        */
    }

    // ** End of EVENT BUS **

    // ** ANONYMOUS CLASSES **

    TimelineRecyclerViewAdapter.PaginationListener mViewHolderListener = new TimelineRecyclerViewAdapter.PaginationListener() {
        @Override
        public void onPreviousPageRequired() {
            if (!mNewsLoading) {
                setLoadingRowFromBackground(true, 0);
                doGetNewerPosts();
            }
        }

        @Override
        public void onNextPageRequired() {
            if (!mNewsLoading) {
                setLoadingRowFromBackground(true, null);
                doGetOlderPosts();
            }
        }
    };

    View.OnClickListener mFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UiHelper.addFragment(
                    getActivity(),
                    R.id.fragment_overlay_container,
                    new NewPostFragment(),
                    NewPostFragment.FRAGMENT_TAG);
        }
    };

    SwipeRefreshLayout.OnRefreshListener mSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            doGetNewerPosts();
        }
    };

    FindCallback<ParseObject> mGetOlderPostsCallback = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> list, ParseException e) {
            if (e == null) {
                //set mSwipeRefreshLayout visible BEFORE modifying
                //the recycler adapter to get a smooth animation
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if (mSwipeRefreshLayout.isRefreshing()) setTimelineRecyclerViewAdapter();
                mTimelineRecyclerViewAdapter.addPostsListToRecyclerEnd(list);
                populateUi();
            } else {
                //Error: No internet
                UiHelper.showParseError(mActivity, e);
            }
            if(mNewsLoading) mTimelineRecyclerViewAdapter.removeLoadingRow();
            mNewsLoading = false;
            setLoadingRowFromBackground(false, null);
        }
    };

    FindCallback<ParseObject> mGetNewerPostsCallback = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> list, ParseException e) {
            if (e == null) {
                mTimelineRecyclerViewAdapter.addPostsListToRecyclerStart(list);
                populateUi();
            } else {
                //Error: No internet
                UiHelper.showParseError(mActivity, e);
            }
            mNewsLoading = false;
            setLoadingRowFromBackground(false, 0);
            onRefreshNewsComplete();
        }
    };

    // ** End of ANONYMOUS CLASSES **
}
