package com.droidko.voltfeed.entities;

import com.droidko.voltfeed.Schema;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by juan on 31/03/15.
 */
public class Post {

    //Values
    private String mId;
    private int mType;
    private String mPicture;
    private String mText;
    private String mTitle;
    private String mUserId;
    private int mVolts;
    private Date mCreatedAt;
    private Date mUpdatedAt;

    public Post(ParseObject parseObject) {
        setId(parseObject.getObjectId());
        setType(parseObject.getInt(Schema.POST_COL_TYPE));
        setPicture(parseObject.getString(Schema.POST_COL_PICTURE));
        setText(parseObject.getString(Schema.POST_COL_TEXT));
        setTitle(parseObject.getString(Schema.POST_COL_TITLE));
        setUserId(parseObject.getString(Schema.POST_COL_USER_ID));
        setVolts(parseObject.getInt(Schema.POST_COL_VOLTS));
        setCreatedAt(parseObject.getCreatedAt());
        setUpdatedAt(parseObject.getUpdatedAt());
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        this.mPicture = picture;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public int getVolts() {
        return mVolts;
    }

    public void setVolts(int volts) {
        mVolts = volts;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.mUpdatedAt = updatedAt;
    }

}
