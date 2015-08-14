package com.droidko.voltfeed.utils;

import android.net.Uri;
import android.view.View;

import com.cloudinary.Transformation;
import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.VoltfeedApp;
import com.droidko.voltfeed.model.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class ImagesHelper {

    //SETTINGS
    private static final int QUALITY_HIGH = 90;
    private static final int QUALITY_MID = 80;
    private static final int QUALITY_LOW = 65;
    private static final int SIZE_THUMBNAIL = 100;
    private static final String CROP_LIMIT = "limit";
    private static final String CROP_THUMBNAIL = "thumb";
    private static final String PREFERED_FORMAT = ".webp";

    //** Start of CLOUDINARY **

    public static String getThumbnailImage(String cloudinaryImageId) {
        return VoltfeedApp.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(SIZE_THUMBNAIL)
                        .height(SIZE_THUMBNAIL)
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
    //** End of CLOUDINARY **

    //** Start of FRESCO **
    public static void loadThumbAvatarImage(SimpleDraweeView simpleDraweeView, User user) {
        simpleDraweeView.setVisibility(View.VISIBLE);

        String cloudinaryAvatarUrl = getThumbnailImage(user.getAvatar());
        Uri avatarUri = Uri.parse(cloudinaryAvatarUrl);

        //Main request for the image to load
        ImageRequest avatarImageRequest = ImageRequestBuilder.newBuilderWithSource(avatarUri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();

        //Image loader controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(avatarImageRequest)
                .setTapToRetryEnabled(false)
                .setOldController(simpleDraweeView.getController())
                .build();

        simpleDraweeView.setController(controller);
    }
    //** End of FRESCO **

}
