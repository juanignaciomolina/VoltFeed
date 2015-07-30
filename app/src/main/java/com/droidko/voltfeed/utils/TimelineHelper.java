package com.droidko.voltfeed.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.droidko.voltfeed.Schema;
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

            //Case: Idea post
            case Schema.POST_COL_TYPE_IDEA:
                itemLayoutView = LayoutInflater.from(context)
                        .inflate(R.layout.fragment_timeline_post_idea, null);
                return new TimelineIdeaPostViewHolder(itemLayoutView);

            //Case: Image post
            case Schema.POST_COL_TYPE_IMAGE:
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

            //Case: Idea post
            case Schema.POST_COL_TYPE_IDEA:
                final TimelineIdeaPostViewHolder ideaPostViewHolder =
                        ((TimelineIdeaPostViewHolder) viewHolder);
                ideaPostViewHolder.mContent.setText(post.getText());
                UiHelper.setFontRoboto(ideaPostViewHolder.mContent);
                ideaPostViewHolder.mDate.setText(
                        new PrettyTime().format(post.getCreatedAt())
                );
                UiHelper.setFontVarela(ideaPostViewHolder.mDate);
                populateVoltViews(post,
                        ideaPostViewHolder.mVoltButton,
                        ideaPostViewHolder.mVoltsCounter);

                ideaPostViewHolder.mVoltButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiHelper.voltPost(post, !view.isSelected());
                        populateVoltViews(post,
                                ideaPostViewHolder.mVoltButton,
                                ideaPostViewHolder.mVoltsCounter);
                        AnimationHelper.popButton(view);
                    }
                });

                ideaPostViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Do something on row click
                    }
                });

                return true;

            //Case: Image post
            case Schema.POST_COL_TYPE_IMAGE:
                final TimelineImagePostViewHolder imagePostViewHolder =
                        ((TimelineImagePostViewHolder) viewHolder);
                imagePostViewHolder.mTitle.setText(post.getTitle());
                UiHelper.setFontRoboto(imagePostViewHolder.mTitle);
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
                populateVoltViews(post,
                        imagePostViewHolder.mVoltButton,
                        imagePostViewHolder.mVoltsCounter);

                imagePostViewHolder.mVoltButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiHelper.voltPost(post, !view.isSelected());
                        populateVoltViews(post,
                                imagePostViewHolder.mVoltButton,
                                imagePostViewHolder.mVoltsCounter);
                        AnimationHelper.popButton(view);
                    }
                });

                imagePostViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Do something on row click
                    }
                });

                return true;

            //Case default: Not a valid post type
            default:
                return false;
        }
    }

    private static void populateVoltViews(Post post,
                                          View button,
                                          TextView counter) {
        if (ApiHelper.isPostVolted(post)) {
            ((TextView)button).setText(R.string.volted);
            button.setSelected(true);

        } else {
            ((TextView)button).setText(R.string.volt);
            button.setSelected(false);
        }
        counter.setText(String.valueOf(post.getVolts()));
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

        draweeView.setController(controller);
    }

}
