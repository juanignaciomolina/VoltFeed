package com.droidko.voltfeed.api;

import com.droidko.voltfeed.Config;

import retrofit.http.GET;
import retrofit.http.Query;

public interface NewsService {
    @GET(Config.PARSE_NEWS)
    void getNews(@Query("skip") int fromPos,
               @Query("limit") int NumbOfNews,
               retrofit.Callback<PostsRequestAdapter> newsRequestAdapterCallback);
}
