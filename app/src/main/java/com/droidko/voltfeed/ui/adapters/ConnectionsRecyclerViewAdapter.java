package com.droidko.voltfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.model.User;
import com.droidko.voltfeed.ui.connections.ConnectionsUserViewHolder;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerAdapter;
import com.droidko.voltfeed.utils.ApiHelper;
import com.droidko.voltfeed.utils.ImagesHelper;
import com.droidko.voltfeed.utils.UiHelper;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsRecyclerViewAdapter extends RecyclerAdapter<User> {

    private PaginationListener mPaginationListener;

    private User mFirstUser;
    private User mLastUser;

    public interface PaginationListener {
        void onPreviousPageRequired();
        void onNextPageRequired();
    }

    public void setPaginationListener(PaginationListener mPaginationListener) {
        this.mPaginationListener = mPaginationListener;
    }

    public User getFirstUser() {
        return mFirstUser;
    }

    public User getLastUser() {
        return mLastUser;
    }

    // Get item view type (invoked by the customized RecyclerAdapter)
    @Override
    public int recyclerGetItemViewType(int position) {
        return 0;
    }

    // Create new views (invoked by the RecyclerAdapter)
    @Override
    public RecyclerView.ViewHolder recyclerOnCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connections_row_user, null);

        return new ConnectionsUserViewHolder(itemLayoutView);

    }

    // Replace the contents of a view (invoked by the RecyclerAdapter)
    @Override
    public void recyclerOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ConnectionsUserViewHolder connectionsUserViewHolder =
                (ConnectionsUserViewHolder) viewHolder;
        final User user = getItems().get(position);

        ImagesHelper.loadThumbAvatarImage(connectionsUserViewHolder.mAvatar, user);
        connectionsUserViewHolder.mUsername.setText(user.getUsername());
        connectionsUserViewHolder.mFollowersCounter.setText(String.valueOf(user.getFollowersCount()));
        connectionsUserViewHolder.mVoltsCounter.setText(String.valueOf(user.getVoltsCount()));

        connectionsUserViewHolder.mFollowButton.setSelected(ApiHelper.isUserFollow(user));

        connectionsUserViewHolder.mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                ApiHelper.followUser(user, view.isSelected());
            }
        });

        UiHelper.setFontVarela(connectionsUserViewHolder.mUsername);
        UiHelper.setFontVarela(connectionsUserViewHolder.mFollowers);
        UiHelper.setFontVarela(connectionsUserViewHolder.mVolts);

        if (mPaginationListener != null) {
            //Aproaching end of dataset -> require next page
            if (position == getItemCount() - 1 - Config.FEED_FECTH_THRESHOLD)
                mPaginationListener.onNextPageRequired();
            //Aproaching start of dataset -> requiere previous page
            else if (position == Config.FEED_FECTH_THRESHOLD)
                mPaginationListener.onPreviousPageRequired();
        }
    }

    public void addUserToRecyclerStart(User user) {
        addItemToPos(0, user);
        mFirstUser = user;
        if (mLastUser == null) mLastUser = user;
    }

    public void addUsersListToRecyclerStart(List<ParseUser> list) {
        for (ParseUser parseUser : list) {
            addUserToRecyclerStart(new User(parseUser));
        }
    }

    public void addUsersListToRecyclerEnd(List<ParseUser> list) {
        ArrayList<User> userArrayList = new ArrayList<User>();
        for (ParseUser parseUser : list) {
            User user = new User(parseUser);
            userArrayList.add(user);
            if (mFirstUser == null) mFirstUser = user;
            mLastUser = user;
        }
        addAllItems(userArrayList);
    }

}
