package com.droidko.voltfeed.api;

import com.droidko.voltfeed.entities.News;

public class NewsRequestAdapter {

    private News[] results;

    public News[] getResults() {
        return results;
    }

    public void setResults(News[] results) {
        this.results = results;
    }
}
