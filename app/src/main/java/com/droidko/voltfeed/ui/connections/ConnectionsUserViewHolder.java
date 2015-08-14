package com.droidko.voltfeed.ui.connections;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class ConnectionsUserViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView mAvatar;
    public TextView mUsername;
    public TextView mFollowers;
    public TextView mFollowersCounter;
    public TextView mVolts;
    public TextView mVoltsCounter;
    public ImageView mFollowButton;

    public ConnectionsUserViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        this.mAvatar =
                (SimpleDraweeView) itemLayoutView.findViewById(R.id.fragment_connection_row_avatar);
        this.mUsername =
                (TextView) itemLayoutView.findViewById(R.id.fragment_connection_row_username);
        this.mFollowers =
                (TextView) itemLayoutView.findViewById(R.id.fragment_connection_row_followers);
        this.mFollowersCounter =
                (TextView) itemLayoutView.findViewById(R.id.fragment_connection_row_followers_count);
        this.mVolts =
                (TextView) itemLayoutView.findViewById(R.id.fragment_connection_row_volts);
        this.mVoltsCounter =
                (TextView) itemLayoutView.findViewById(R.id.fragment_connection_row_volts_count);
        this.mFollowButton =
                (ImageView) itemLayoutView.findViewById(R.id.fragment_connection_row_follow);
    }

}
