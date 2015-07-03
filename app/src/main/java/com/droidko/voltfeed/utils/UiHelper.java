package com.droidko.voltfeed.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.VoltfeedApp;
import com.droidko.voltfeed.activities.VoltfeedActivity;
import com.parse.ParseException;

public class UiHelper {

    private static final Typeface sFontVarela = Typeface.createFromAsset(
            VoltfeedApp.getContextInstance().getAssets(), "VarelaRound-Regular.otf");

    private static final Typeface sFontRoboto = Typeface.createFromAsset(
            VoltfeedApp.getContextInstance().getAssets(), "Roboto-Regular.ttf");

    public static Toolbar setToolbar(ActionBarActivity activity,
                                     int view_toolbar,
                                     int view_title,
                                     String title,
                                     int view_logo,
                                     int logo) {
        Toolbar local_toolbar = (Toolbar) activity.findViewById(view_toolbar);
        ImageView local_logo = (ImageView) activity.findViewById(view_logo);
        TextView local_title = (TextView) activity.findViewById(view_title);

        activity.setSupportActionBar(local_toolbar);
        activity.getSupportActionBar().setElevation(Config.UI_TOOLBAR_ELEVATION);
        //We place the tittle in view_title. Otherwise it would end up on the left side of the logo
        activity.getSupportActionBar().setTitle(null);
        local_logo.setImageResource(logo);
        local_title.setText(title);
        setFontVarela(local_title);

        return local_toolbar;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void startActivityClearStack(Context localContext,
                                               Class<? extends Activity> targetActivity) {
        localContext.startActivity(
                new Intent(localContext, targetActivity).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
        );
    }

    public static void showParseError(Context context, ParseException e) {
        switch (e.getCode()) {
            //Error 100: No internet connection
            case 100:
                showToast(context, context.getString(R.string.error_no_internet));
                break;

            //Error 101: Email and/or password incorrect
            case 101:
                showToast(context, context.getString(R.string.login_wrong_credentials));
                break;

            //Error 201: Invalid username (already taken)
            case 201:
                showToast(context, context.getString(R.string.signup_invalid_username));
                break;

            //Error 209: Session expired
            case 209:
                showToast(context, context.getString(R.string.error_session_expired));
                break;

            //Error ??: Default unknown error
            default:
                showToast(context, context.getString(R.string.error_unknown));
                Log.e(Config.LOG_ERROR, e.getCode() + ": " + e.getMessage());
        }
    }

    public static void setFontVarela(TextView textView) {
        textView.setTypeface(sFontVarela);
    }

    public static void setFontRoboto(TextView textView) {
        textView.setTypeface(sFontRoboto);
    }

    public static boolean canRunLollipopFx() {
        return (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                && Config.LOLLIPOP_FX_ENABLED;
    }

    @SuppressLint("NewApi")
    public static void renderElevation(int elevation, View view) {
        if (!canRunLollipopFx()) return;
        view.setElevation(elevation);
    }

    public static void renderElevation(int elevation, View... views) {
        for (View view : views) {
            renderElevation(elevation, view);
        }
    }

    public static void addFragment(VoltfeedActivity activity,
                                   int containerId,
                                   Fragment fragment,
                                   String tag) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.abc_fade_in,
                        R.anim.abc_fade_out,
                        R.anim.abc_fade_in,
                        R.anim.abc_fade_out)
                .add(containerId, fragment)
                .addToBackStack(tag)
                .commit();
    }

    public static void removeFragment(VoltfeedActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_out, R.anim.abc_fade_in)
                .remove(fragment)
                .commit();
    }

    protected void replaceFragment(VoltfeedActivity activity, int containerId, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

}