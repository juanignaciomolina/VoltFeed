package com.droidko.voltfeed.api;

import com.droidko.voltfeed.entities.Post;

public class PostsRequestAdapter {

    private Post[] results;

    public Post[] getResults() {
        return results;
    }

    public void setResults(Post[] results) {
        this.results = results;
    }
}
