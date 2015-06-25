package com.droidko.voltfeed;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.droidko.voltfeed.activities.MainActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.parse.Parse;

public class VoltfeedApp extends Application {

    private static Context sContext;
    private static SharedPreferences sLogInPreferences;
    private static SharedPreferences.Editor sLogInPreferencesEditor;
    private static MainActivity sMainActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        initParse(this);
        initFresco(this);
        initPreferences(this);
    }

    //** Initializers **

    private void initParse(Context context) {
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, Config.PARSE_APP_ID, Config.PARSE_CLIENT_KEY);
    }

    private void initFresco(Context context) {
        Fresco.initialize(context);
    }

    private void initPreferences(Context context) {
        sLogInPreferences = context.getSharedPreferences(
                Config.LOGIN_PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        sLogInPreferencesEditor = sLogInPreferences.edit();
    }

    //** Accessors **

    public static Context getContextInstance() {
        return sContext;
    }

    public static MainActivity getMainActivity() { return sMainActivity; }

    public static SharedPreferences getLogInPreferences() {
        return sLogInPreferences;
    }

    public static SharedPreferences.Editor editLogInPreferences() {
        return sLogInPreferencesEditor;
    }

}