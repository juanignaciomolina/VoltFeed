package com.droidko.voltfeed;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cloudinary.Cloudinary;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.parse.Parse;

import java.util.HashMap;
import java.util.Map;

public class VoltfeedApp extends Application {

    private static Context sContext;
    private static SharedPreferences sLogInPreferences;
    private static SharedPreferences.Editor sLogInPreferencesEditor;
    private static Cloudinary sCloudinary;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        initParse(this);
        initFresco(this);
        initCloudinary();
        initPreferences(this);
    }

    //** Initializers **

    private void initParse(Context context) {
        //Parse.enableLocalDatastore(context); Cannot be enable while using Parse Cache
        Parse.initialize(context, Config.PARSE_APP_ID, Config.PARSE_CLIENT_KEY);
    }

    private void initFresco(Context context) {
        Fresco.initialize(context);
    }

    private void initCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", Config.CLOUDINARY_NAME);
        config.put("api_key", Config.CLOUDINARY_API_KEY);
        config.put("api_secret", Config.CLOUDINARY_API_SECRET);
        sCloudinary = new Cloudinary(config);
    }

    private void initPreferences(Context context) {
        sLogInPreferences = context.getSharedPreferences(
                Config.LOGIN_PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        sLogInPreferencesEditor = sLogInPreferences.edit();
    }

    //** Accessors **


    public static Cloudinary getCloudinary() {
        return sCloudinary;
    }

    public static Context getContextInstance() {
        return sContext;
    }

    public static SharedPreferences getLogInPreferences() {
        return sLogInPreferences;
    }

    public static SharedPreferences.Editor editLogInPreferences() {
        return sLogInPreferencesEditor;
    }

}