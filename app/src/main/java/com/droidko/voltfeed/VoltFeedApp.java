package com.droidko.voltfeed;

import android.app.Application;

import com.droidko.voltfeed.api.ParseAPIHelper;

import retrofit.RestAdapter;

public class VoltFeedApp extends Application {

    private static ParseAPIHelper sParseAPIHelper;
    private static RestAdapter sRestAdapter;

    //The ParseAPIHelper instance is created on-demand if it hasn't been instantiated before
    public static ParseAPIHelper getParseApiHelper() {
        if (sParseAPIHelper == null) {
            setParseApiHelper(new ParseAPIHelper());
            sParseAPIHelper.setContentTypeToJson(true);
        }
        return sParseAPIHelper;
    }

    public static void setParseApiHelper(ParseAPIHelper sParseApiHelper) {
        VoltFeedApp.sParseAPIHelper = sParseApiHelper;
    }

    //The RestAdapter instance is created on-demand if it hasn't been instantiated before
    public static RestAdapter getRestAdapter() {
        if (sRestAdapter == null) {
            setRestAdapter(getParseApiHelper().getRestAdapter());
        }
        return sRestAdapter;
    }

    public static void setRestAdapter(RestAdapter sRestAdapter) {
        VoltFeedApp.sRestAdapter = sRestAdapter;
    }

}