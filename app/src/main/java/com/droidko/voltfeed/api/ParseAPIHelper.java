package com.droidko.voltfeed.api;

import com.droidko.voltfeed.Config;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class ParseAPIHelper {

    private String mSessionToken;
    private boolean mContentTypeJSON;

    private static final String CONTENT_HEADER = "Content-Type";
    private static final String CONTENT_JSON = "application/json";

    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader(Config.PARSE_APP_HEADER, Config.PARSE_APP_ID);
            request.addHeader(Config.PARSE_REST_HEADER, Config.PARSE_REST_KEY);
            if (mSessionToken != null) request.addHeader(Config.PARSE_TOKEN_HEADER, mSessionToken);
            if (mContentTypeJSON) request.addHeader(CONTENT_HEADER, CONTENT_JSON);
        }
    };

    public ParseAPIHelper(String sessionToken) {
        this.mSessionToken = sessionToken;
    }

    public ParseAPIHelper() {
        this.mSessionToken = null;
    }

    public void setSessionToken(String sessionToken) {
        this.mSessionToken = sessionToken;
    }

    public String getSessionToken() {
        return mSessionToken;
    }

    public void setContentTypeToJson(boolean value) {
        this.mContentTypeJSON = value;
    }

    public RestAdapter getRestAdapter() {

        return new RestAdapter.Builder()
                .setEndpoint(Config.PARSE_API_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

}
