package com.droidko.voltfeed.entities;

import com.droidko.voltfeed.ui.widget.RecyclerView.RecyclerItem;

public class TimelineRow extends RecyclerItem {

    private Post mPost;

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        mPost = post;
    }
}
