package com.droidko.voltfeed.utils;

import android.text.TextUtils;

public class InputCheckHelper {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}