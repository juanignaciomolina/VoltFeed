package com.droidko.voltfeed.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.cloudinary.Transformation;
import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.VoltfeedApp;
import com.droidko.voltfeed.model.Post;
import com.droidko.voltfeed.model.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImagesHelper {

    //SETTINGS
    private static final int QUALITY_HIGH = 90;
    private static final int QUALITY_MID = 80;
    private static final int QUALITY_LOW = 65;
    private static final int SIZE_THUMBNAIL = 100;
    private static final String CROP_LIMIT = "limit";
    private static final String CROP_THUMBNAIL = "thumb";
    private static final String PREFERED_FORMAT = ".webp";
    private static final int BITMAP_MAX_SIZE = 4048;
    private static final String PICTURES_TAKEN_PREFIX = "Voltfeed";

    //** Base dimensions
    public static int getBaseHeight() {
        return Math.min(Config.UI_SCREEN_BASE_HEIGHT, BITMAP_MAX_SIZE);
    }

    public static int getBaseWidth() {
        return Math.min(Config.UI_SCREEN_BASE_WIDTH, BITMAP_MAX_SIZE);
    }

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
                        .width(Double.valueOf(getBaseWidth() * 0.9).intValue())
                        .height(Double.valueOf(getBaseHeight() * 0.6).intValue())
                        .crop(CROP_LIMIT)
                        .quality(QUALITY_MID))
                .generate(cloudinaryImageId + PREFERED_FORMAT);
    }

    public static String getFullScreenImage(String cloduinaryImageId) {
        return VoltfeedApp.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(getBaseWidth())
                        .height(getBaseHeight())
                        .crop(CROP_LIMIT)
                        .quality(QUALITY_HIGH))
                .generate(cloduinaryImageId + PREFERED_FORMAT);
    }

    //WARNING: This is a very heavy request that can consume considerable bandwith. Caution advised.
    public static String getZoomableScreenImage(String cloduinaryImageId) {
        return VoltfeedApp.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(getBaseWidth() * 2)
                        .height(getBaseHeight() * 2)
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
    public static void loadImageInTimeline(SimpleDraweeView draweeView, Post post) {
        draweeView.setVisibility(View.VISIBLE);

        String cloudinaryPictureUrl = getTimelineImage(post.getPicture());
        Uri pictureUri = Uri.parse(cloudinaryPictureUrl);

        //Main request for the image to load
        ImageRequest highResRequest = ImageRequestBuilder.newBuilderWithSource(pictureUri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();

        //Image loader controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(highResRequest)
                .setTapToRetryEnabled(true)
                .setOldController(draweeView.getController())
                .build();

        draweeView.setController(controller);
    }
    //** End of FRESCO **

    //** Start of UPLOAD IMAGE **
    public static class UploadImageTask extends AsyncTask<Uri, Void, Boolean> {

        public interface UploadFinishedCallback {
            void onUploadFinished(List<String> imagesIds);
        }

        UploadFinishedCallback mUploadFinishedCallback;
        List<String> mImagesIds = new LinkedList<String>();

        public UploadImageTask(UploadFinishedCallback uploadFinishedCallback) {
            this.mUploadFinishedCallback = uploadFinishedCallback;
        }

        @Override
        protected void onPostExecute(Boolean uploadResult) {
            super.onPostExecute(uploadResult);
            if (uploadResult) mUploadFinishedCallback.onUploadFinished(mImagesIds);
            else mUploadFinishedCallback.onUploadFinished(null);
        }

        @Override
        protected Boolean doInBackground(Uri... uris) {
            try {
                for (int i = 0; i < uris.length; i++) {
                    InputStream in = VoltfeedApp
                            .getContextInstance()
                            .getContentResolver()
                            .openInputStream(uris[i]);


                    //This method call uploads the image and returns a map with some upload
                    //params, including the id generated for this image
                    Map uploadParams = VoltfeedApp.getCloudinary().uploader().upload(in, null);
                    mImagesIds.add((String) uploadParams.get("public_id"));
                    int a = 0;
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    //** End of UPLOAD IMAGE **

    //** Start of STORE IMAGE **
    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = PICTURES_TAKEN_PREFIX + "_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    //This method adds a picture to the user's images gallery
    public static void galleryAddPic(Uri pictureUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(pictureUri);
        VoltfeedApp.getContextInstance().sendBroadcast(mediaScanIntent);
    }
    //** End of STORE IMAGE **

}
