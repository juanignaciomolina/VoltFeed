package com.droidko.voltfeed.utils;

import com.droidko.voltfeed.Config;

public class ValidationHelper {

    public static boolean validateIdea(String ideaText) {
        return (Config.POST_IDEA_MAX_CHAR_LENGHT - ideaText.length()) >= 0;
    }

}
