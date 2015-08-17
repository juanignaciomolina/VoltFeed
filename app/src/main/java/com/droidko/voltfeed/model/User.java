package com.droidko.voltfeed.model;

import com.droidko.voltfeed.Schema;
import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerItem;
import com.parse.ParseUser;

import java.util.Date;

public class User extends RecyclerItem {

    //Attributes
    private String mId;
    private String mUsername;
    private String mAvatar;
    private int mVoltsCount;
    private int mPostsCount;
    private int mFollowingCount;
    private int mFollowersCount;
    private Date mCreatedAt;

    public User(ParseUser parseUser) {
        setId(parseUser.getObjectId());
        setUsername(parseUser.getString(Schema.USER_COL_USERNAME));
        setAvatar(parseUser.getString(Schema.USER_COL_PICTURE));
        setVoltsCount(parseUser.getInt(Schema.USER_COL_VOLTS_POSTS_COUNT));
        setPostsCount(parseUser.getInt(Schema.USER_COL_POSTS_CREATED_COUNT));
        setFollowingCount(parseUser.getInt(Schema.USER_COL_FOLLOWING_USERS_COUNT));
        setFollowersCount(parseUser.getInt(Schema.USER_COL_FOLLOWERS_USERS_COUNT));
        setCreatedAt(parseUser.getCreatedAt());
    }

    public void setId(String id) {
        mId = id;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public void setVoltsCount(int voltsCount) {
        mVoltsCount = voltsCount;
    }

    public void setPostsCount(int postsCount) {
        mPostsCount = postsCount;
    }

    public void setFollowingCount(int followingCount) {
        mFollowingCount = followingCount;
    }

    public void setFollowersCount(int followersCount) {
        mFollowersCount = followersCount;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public int getVoltsCount() {
        return mVoltsCount;
    }

    public int getFollowersCount() {
        return mFollowersCount;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getId() {
        return mId;
    }
}
