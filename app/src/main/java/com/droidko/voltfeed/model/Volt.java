package com.droidko.voltfeed.model;

public class Volt {

    private String mVoltedObjectId;
    private boolean mState;

    public Volt(String voltedObjectId, boolean state) {
        mVoltedObjectId = voltedObjectId;
        mState = state;
    }

    public boolean getState() {
        return mState;
    }

    public String getVoltedObjectId() {
        return mVoltedObjectId;
    }
}
