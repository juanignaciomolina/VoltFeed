package com.droidko.voltfeed.utils;

import com.cloudinary.Transformation;
import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.VoltfeedApp;

public class ImagesHelper {

    //SETTINGS
    private static final int QUALITY_HIGH = 90;
    private static final int QUALITY_MID = 80;
    private static final int QUALITY_LOW = 65;
    private static final String CROP_LIMIT = "limit";
    private static final String CROP_THUMBNAIL = "thumb";
    private static final String PREFERED_FORMAT = ".webp";

    public static String getThumbnailImage(String cloudinaryImageId) {
        return VoltfeedApp.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(Config.UI_SCREEN_BASE_WIDTH / 8)
                        .height(Config.UI_SCREEN_BASE_HEIGHT / 8)
                        .crop(CROP_THUMBNAIL)
                        .quality(QUALITY_LOW))
                .generate(cloudinaryImageId + PREFERED_FORMAT);
    }


    public static String getTimelineImage(String cloudinaryImageId) {
        return VoltfeedApp.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(Config.UI_SCREEN_BASE_WIDTH / 2)
                        .height(Config.UI_SCREEN_BASE_HEIGHT / 2)
                        .crop(CROP_LIMIT)
                        .quality(QUALITY_MID))
                .generate(cloudinaryImageId + PREFERED_FORMAT);
    }

    public static String getFullScreenImage(String cloduinaryImageId) {
        return VoltfeedApp.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(Config.UI_SCREEN_BASE_WIDTH)
                        .height(Config.UI_SCREEN_BASE_HEIGHT)
                        .crop(CROP_LIMIT)
                        .quality(QUALITY_HIGH))
                .generate(cloduinaryImageId + PREFERED_FORMAT);
    }

}
