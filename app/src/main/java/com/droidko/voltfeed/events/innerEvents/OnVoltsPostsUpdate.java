package com.droidko.voltfeed.events.innerEvents;

import java.util.HashSet;

public class OnVoltsPostsUpdate {

    private HashSet<String> voltedPostsSet;

    public OnVoltsPostsUpdate(HashSet<String> voltedPostsSet) {
        this.voltedPostsSet = voltedPostsSet;
    }

    public HashSet<String> getVoltedPostsSet() {
        return voltedPostsSet;
    }
}
