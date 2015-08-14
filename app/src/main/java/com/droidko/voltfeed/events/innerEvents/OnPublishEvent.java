package com.droidko.voltfeed.events.innerEvents;

import com.droidko.voltfeed.model.Post;

public class OnPublishEvent {
    private Post mPost;

    public OnPublishEvent(Post post) {
        mPost = post;
    }

    public Post getPost() {
        return mPost;
    }
}
