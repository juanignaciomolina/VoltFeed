package com.droidko.voltfeed.events.innerEvents;

import java.util.HashSet;

public class OnFollowingUsersUpdate {

    private HashSet<String> followingUsersSet;

    public OnFollowingUsersUpdate(HashSet<String> followingUsersSet) {
        this.followingUsersSet = followingUsersSet;
    }

    public HashSet<String> getFollowingUsers() {
        return followingUsersSet;
    }
}
