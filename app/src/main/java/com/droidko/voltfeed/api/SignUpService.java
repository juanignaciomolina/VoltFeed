package com.droidko.voltfeed.api;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.entities.User;

import retrofit.http.Body;
import retrofit.http.POST;

public interface SignUpService {
    @POST(Config.PARSE_USERS)
    void signUp(@Body User user, retrofit.Callback<User> userCallback);
}
