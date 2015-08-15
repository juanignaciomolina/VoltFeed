package com.droidko.voltfeed;

public class Config {

    //Terms of Service
    public static final String ToS_URL = "http://www.google.com.ar";

    //PARSE REST API
    public static final String PARSE_APP_ID = "IC10flmq1iP4EyhiCWeHyaWPK6BVLnWJqPutVBRS";
    public static final String PARSE_CLIENT_KEY = "MmYFbfEDfHpMns3BXIkMLDL47YHQvGBH0DfGULFU";

    //CLOUDINARY API
    public static final String CLOUDINARY_NAME = "voltfeed";
    public static final String CLOUDINARY_API_KEY = "986199667734139";
    public static final String CLOUDINARY_API_SECRET = "Hc4DdDabuQcVbo-dgS2dZSp619s";

    //SharedPreferences
    public static final String LOGIN_PREFERENCES_KEY = "Login_preferences";
    public static final String LOGIN_EMAIL_KEY = "Email";
    public static final String LOGIN_PASSWORD_KEY = "Password";

    //LOGS
    public static final String LOG_DEBUG = "VF - Debug:";
    public static final String LOG_ERROR = "VF - Error: ";

    //UI
    public static final boolean UI_LOLLIPOP_FX_ENABLED = true;
    public static final int UI_TOOLBAR_ELEVATION = 10;
    public static final int UI_TABS_ELEVATION = 8;
    public static final int UI_MOTD_COUNT = 5;
    public static final int UI_TIMELINE_ANIMATION_DURATION = 400;
    public static final int UI_SCREEN_BASE_WIDTH = 1080;
    public static final int UI_SCREEN_BASE_HEIGHT = 1920;

    //PERFORMANCE
    public static final int PERFORMANCE_API_PAGE_SIZE = 8;
    public static final int PERFORMANCE_CACHE_MAX_AGE = 120000;

    //FEED
    public static final int FEED_FECTH_THRESHOLD = 2;

    //USER
    public static final int USER_PASSWORD_MIN_LENGHT = 8;

    //POST
    public static final int POST_MAX_CHAR_LENGHT = 140;
}
