package com.droidko.voltfeed.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.parse.ParseException;

public class UiHelper {

    private static Typeface sFontVarela = Typeface.createFromAsset(
            VoltfeedApp.getInstance().getAssets(), "VarelaRound-Regular.otf");

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

    public static void setProgessiveFrescoImage(SimpleDraweeView draweeView,
                                                Uri highResUri,
                                                Uri lowResUri,
                                                boolean tapToRetry) {
        draweeView.setVisibility(View.VISIBLE);
        ImageRequest highResRequest = ImageRequestBuilder.newBuilderWithSource(highResUri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(lowResUri))
                .setImageRequest(highResRequest)
                .setTapToRetryEnabled(tapToRetry)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
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

}