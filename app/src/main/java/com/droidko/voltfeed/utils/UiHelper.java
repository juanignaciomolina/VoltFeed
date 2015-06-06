package com.droidko.voltfeed.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidko.voltfeed.Config;

public class UiHelper {

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
        activity.getSupportActionBar().setElevation(Config.UI_ELEVATION);
        //We place the tittle in view_title. Otherwise it would end up on the left side of the logo
        activity.getSupportActionBar().setTitle(null);
        local_logo.setImageResource(logo);
        local_title.setText(title);

        return local_toolbar;
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

}