package com.droidko.voltfeed.utils;

import com.droidko.voltfeed.Config;

public class ValidationHelper {

    public static boolean validatePostIdeaLenght(String postText) {
        return (Config.POST_MAX_CHAR_LENGHT >= postText.length() &&
        !postText.isEmpty());
    }

    public static boolean validatePostImageLenght(String postText) {
        return (Config.POST_MAX_CHAR_LENGHT >= postText.length());
    }

}
