package com.droidko.voltfeed.entities;

public class Volt {

    private boolean mState;

    public Volt (boolean state) {
        mState = state;
    }

    public boolean getState() {
        return mState;
    }

    public void setState(boolean state) {
        mState = state;
    }

}
