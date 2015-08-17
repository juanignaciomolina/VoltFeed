package com.droidko.voltfeed.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidko.voltfeed.R;
import com.droidko.voltfeed.activities.MainActivity;
import com.droidko.voltfeed.ui.QuickReturnAnimation;
import com.droidko.voltfeed.ui.adapters.ConnectionsRecyclerViewAdapter;
import com.droidko.voltfeed.utils.ApiHelper;
import com.droidko.voltfeed.utils.UiHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class ConnectionsFragment extends Fragment {

    private MainActivity mActivity;

    private RecyclerView mRecyclerView;

    private ConnectionsRecyclerViewAdapter mConnectionsRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean mUsersLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connections, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = (MainActivity) getActivity();

        initUi();
        doGetOlderUsers();
    }

    private void initUi() {
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.connections_recycler_view_users);

        mLinearLayoutManager = new LinearLayoutManager(mActivity.getApplicationContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        setConnectionsRecyclerViewAdapter();

        QuickReturnAnimation quickReturnAnimation = new QuickReturnAnimation(
                mActivity.getQuickReturnWrapper(),
                mActivity.getToolbar(),
                mActivity.getTabs());
        mRecyclerView.addOnScrollListener(quickReturnAnimation);
    }

    private void setConnectionsRecyclerViewAdapter() {
        mConnectionsRecyclerViewAdapter = new ConnectionsRecyclerViewAdapter();
        mConnectionsRecyclerViewAdapter.setPaginationListener(mViewHolderListener);
        mConnectionsRecyclerViewAdapter
                .setLoaderDividerColor(getResources().getColor(R.color.white_4));

        mRecyclerView.setAdapter(mConnectionsRecyclerViewAdapter);
    }

    private void setLoadingRowFromBackground(final boolean state, @Nullable final Integer position) {
        android.os.Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                if (state) {
                    if (position != null) mConnectionsRecyclerViewAdapter.addLoadingRow(position);
                    else mConnectionsRecyclerViewAdapter.addLoadingRow();
                }
                else {
                    if (position != null) mConnectionsRecyclerViewAdapter.removeLoadingRow(position);
                    mConnectionsRecyclerViewAdapter.removeLoadingRow();
                }
            }
        });
    }

    private void doGetOlderUsers() {
        mUsersLoading = true;
        Date latestDate = null;
        if (mConnectionsRecyclerViewAdapter.getLastUser() != null) latestDate =
                mConnectionsRecyclerViewAdapter.getLastUser().getCreatedAt();
        ApiHelper.getOlderFollowingUsers(
                latestDate,
                mGetOlderUsersCallback);
    }

    private void doGetNewerUsers() {
        mUsersLoading = true;
        ApiHelper.getNewerFollowingUsers(
                mConnectionsRecyclerViewAdapter.getFirstUser().getCreatedAt(),
                mGetNewerUsersCallback);
    }


    // ** Start of  ANONYMOUS CLASSES **

    ConnectionsRecyclerViewAdapter.PaginationListener mViewHolderListener = new ConnectionsRecyclerViewAdapter.PaginationListener() {
        @Override
        public void onPreviousPageRequired() {
            if (!mUsersLoading) {
                setLoadingRowFromBackground(true, 0);
                doGetNewerUsers();
            }
        }

        @Override
        public void onNextPageRequired() {
            if (!mUsersLoading) {
                setLoadingRowFromBackground(true, null);
                doGetOlderUsers();
            }
        }
    };

    FindCallback<ParseUser> mGetOlderUsersCallback = new FindCallback<ParseUser>() {
        @Override
        public void done(List<ParseUser> list, ParseException e) {
            if (e == null) {
                //set mSwipeRefreshLayout visible BEFORE modifying
                //the recycler adapter to get a smooth animation
                mConnectionsRecyclerViewAdapter.addUsersListToRecyclerEnd(list);
            } else {
                //Error: No internet
                UiHelper.showParseError(mActivity, e);
            }
            if(mUsersLoading) mConnectionsRecyclerViewAdapter.removeLoadingRow();
            mUsersLoading = false;
            setLoadingRowFromBackground(false, null);
        }
    };

    FindCallback<ParseUser> mGetNewerUsersCallback = new FindCallback<ParseUser>() {
        @Override
        public void done(List<ParseUser> list, ParseException e) {
            if (e == null) {
                mConnectionsRecyclerViewAdapter.addUsersListToRecyclerEnd(list);
            } else {
                //Error: No internet
                UiHelper.showParseError(mActivity, e);
            }
            mUsersLoading = false;
            setLoadingRowFromBackground(false, 0);
        }
    };

    // ** End of ANONYMOUS CLASSES **
}
