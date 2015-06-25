package com.droidko.voltfeed.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.entities.Post;
import com.droidko.voltfeed.events.EventDispatcher;
import com.droidko.voltfeed.ui.timeline.TimelineIdeaPostViewHolder;
import com.droidko.voltfeed.ui.timeline.TimelineImagePostViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.ocpsoft.pretty.time.PrettyTime;

public class TimelineHelper {

    public static RecyclerView.ViewHolder
    getTimelineViewHolder(int type,
                          Context context) {
        View itemLayoutView;
        switch (type) {

            //Case 0: Idea post
            case 0:
                itemLayoutView = LayoutInflater.from(context)
                        .inflate(R.layout.fragment_timeline_post_idea, null);
                return new TimelineIdeaPostViewHolder(itemLayoutView);

            //Case 0: Image post
            case 1:
                itemLayoutView = LayoutInflater.from(context)
                        .inflate(R.layout.fragment_timeline_post_image, null);
                return new TimelineImagePostViewHolder(itemLayoutView);

            //Case default: Not a valid post type
            default:
                return null;
        }

    }

    public boolean populateTimelineViewHolder(final Post post,
                                                     RecyclerView.ViewHolder viewHolder) {
        switch (post.getType()) {

            //Case 0: Idea post
            case 0:
                TimelineIdeaPostViewHolder ideaPostViewHolder =
                        ((TimelineIdeaPostViewHolder) viewHolder);
                ideaPostViewHolder.mContent.setText(post.getText());
                UiHelper.setFontVarela(ideaPostViewHolder.mContent);
                ideaPostViewHolder.mDate.setText(
                        new PrettyTime().format(post.getCreatedAt())
                );
                UiHelper.setFontVarela(ideaPostViewHolder.mDate);
                populateLikeView(ideaPostViewHolder.mVoltButton);

                ideaPostViewHolder.mVoltButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(Config.LOG_DEBUG, "On Volt Click: " + post.getId());
                    }
                });

                ideaPostViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(Config.LOG_DEBUG, "On Row Click: " + post.getId());
                    }
                });

                return true;

            //Case 1: Image post
            case 1:
                TimelineImagePostViewHolder imagePostViewHolder =
                        ((TimelineImagePostViewHolder) viewHolder);
                imagePostViewHolder.mTitle.setText(post.getTitle());
                UiHelper.setFontVarela(imagePostViewHolder.mTitle);
                imagePostViewHolder.mDate.setText(
                        new PrettyTime().format(post.getCreatedAt())
                );
                UiHelper.setFontVarela(imagePostViewHolder.mDate);
                if (!TextUtils.isEmpty(post.getPicture())) {
                    final Uri fullResUri;
                    fullResUri = Uri.parse(post.getPicture());

                    loadImageInTimeline(imagePostViewHolder.mPicture,
                            fullResUri,
                            null);

                    imagePostViewHolder.mPicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EventDispatcher.dispatchTimelineImageClick(fullResUri.toString(), null);
                        }
                    });
                }
                populateLikeView(imagePostViewHolder.mVoltButton);

                imagePostViewHolder.mVoltButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(Config.LOG_DEBUG, "On Volt Click: " + post.getId());
                    }
                });

                imagePostViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(Config.LOG_DEBUG, "On Row Click: " + post.getId());
                    }
                });

                return true;

            //Case default: Not a valid post type
            default:
                return false;
        }
    }

    private static void populateLikeView(View view) {
        //TODO mostrar el color del boton segun si el usuario volteo el post o no
    }

    public static void loadImageInTimeline(SimpleDraweeView draweeView,
                                           Uri highResUri,
                                           Uri lowResUri) {
        draweeView.setVisibility(View.VISIBLE);

        //Main request for the image to load
        ImageRequest highResRequest = ImageRequestBuilder.newBuilderWithSource(highResUri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();

        //Image loader controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(lowResUri))
                .setImageRequest(highResRequest)
                .setTapToRetryEnabled(true)
                .setOldController(draweeView.getController())
                .build();

        //raweeView.setHierarchy(hierarchy);
        draweeView.setController(controller);
    }

}
