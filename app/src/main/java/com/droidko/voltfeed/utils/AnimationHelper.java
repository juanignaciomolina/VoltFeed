package com.droidko.voltfeed.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.droidko.voltfeed.R;
import com.droidko.voltfeed.VoltfeedApp;

public class AnimationHelper {

    public static void slideFromBelow(View view, int duration) {
        Animation animation = AnimationUtils.loadAnimation(
                VoltfeedApp.getContextInstance(),
                R.anim.slide_from_below);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void slideFromBelowWithFade(View view, int duration) {
        Animation animation = AnimationUtils.loadAnimation(
                VoltfeedApp.getContextInstance(),
                R.anim.slide_from_below_fade);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void slideToTop(View view, int duration) {
        Animation animation = AnimationUtils.loadAnimation(
                VoltfeedApp.getContextInstance(),
                R.anim.slide_to_top);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void popButton(View view) {
        Animation animation = AnimationUtils.loadAnimation(
                VoltfeedApp.getContextInstance(),
                R.anim.button_pop);
        view.startAnimation(animation);
    }

}
