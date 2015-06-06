package com.droidko.voltfeed.api;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.entities.User;

import retrofit.http.GET;

public interface LogInSessionService {
    @GET(Config.PARSE_ME)
    void sessionLogIn(retrofit.Callback<User> userCallback);
}
