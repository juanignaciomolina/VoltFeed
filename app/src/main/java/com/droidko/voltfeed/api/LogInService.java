package com.droidko.voltfeed.api;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.entities.User;

import retrofit.http.GET;
import retrofit.http.Query;

public interface LogInService {
    @GET(Config.PARSE_LOGIN)
    void logIn(@Query("username") String email, //In this app the username is the email
               @Query("password") String password,
               retrofit.Callback<User> userCallback);
}
