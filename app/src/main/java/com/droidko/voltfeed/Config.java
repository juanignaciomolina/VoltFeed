package com.droidko.voltfeed;

public class Config {

    //Terms of Service
    public static final String ToS_URL = "http://www.wolox.com.ar";

    //PARSE REST API
    public static final String PARSE_APP_HEADER = "X-Parse-Application-Id";
    public static final String PARSE_APP_ID = "kLRpYZ9OqIMFJ8zDyrqFlJhWEAabsO20bfZfBXgT";
    public static final String PARSE_REST_HEADER = "X-Parse-REST-API-Key";
    public static final String PARSE_REST_KEY = "c2Vi5MRQx5WxhppghBZy4KYIGjQ6U0CeLAY6UHXO";
    public static final String PARSE_TOKEN_HEADER = "X-Parse-Session-Token";

    public static final String PARSE_API_URL = "https://api.parse.com/1";
    public static final String PARSE_LOGIN = "/login";
    public static final String PARSE_USERS = "/users";
    public static final String PARSE_ME = PARSE_USERS + "/me";
    public static final String PARSE_NEWS = "/classes/news";

    //SharedPreferences
    public static final String LOGIN_PREFERENCES_KEY = "Login_preferences";
    public static final String LOGIN_EMAIL_KEY = "Email";
    public static final String LOGIN_PASSWORD_KEY = "Password";
    public static final String LOGIN_SESSION_KEY = "Session";

    //Logs
    public static final String LOG_DEBUG = "Wolox - Debug:";
    public static final String LOG_ERROR = "Wolox - Error: ";

    //UI
    public static final int UI_ELEVATION = 5;

    //NEWSFEED
    public static final int NEWSFEED_PAGE_SIZE = 8;
    public static final int NEWSFEED_FECTH_THRESHOLD = 2;

}
