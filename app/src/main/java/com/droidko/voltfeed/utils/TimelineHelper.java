package com.droidko.voltfeed.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.Schema;
import com.droidko.voltfeed.events.EventDispatcher;
import com.droidko.voltfeed.model.Post;
import com.droidko.voltfeed.ui.timeline.TimelineIdeaPostViewHolder;
import com.droidko.voltfeed.ui.timeline.TimelineImagePostViewHolder;

import org.ocpsoft.pretty.time.PrettyTime;

public class TimelineHelper {

    public volatile static int sLastPosition = -1;

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
                                              RecyclerView.ViewHolder viewHolder,
                                              int position) {
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

                animateViewIfNew(ideaPostViewHolder.mCard, position);

                return true;

            //Case: Image post
            case Schema.POST_COL_TYPE_IMAGE:
                final TimelineImagePostViewHolder imagePostViewHolder =
                        ((TimelineImagePostViewHolder) viewHolder);
                imagePostViewHolder.mTitle.setText(post.getText());
                UiHelper.setFontRoboto(imagePostViewHolder.mTitle);
                imagePostViewHolder.mDate.setText(
                        new PrettyTime().format(post.getCreatedAt())
                );
                UiHelper.setFontVarela(imagePostViewHolder.mDate);
                if (!TextUtils.isEmpty(post.getPicture())) {
                    final Uri fullResUri =
                            Uri.parse(ImagesHelper.getFullScreenImage(post.getPicture()));
                    final Uri standarResUri =
                            Uri.parse(ImagesHelper.getTimelineImage(post.getPicture()));

                    ImagesHelper.loadImageInTimeline(imagePostViewHolder.mPicture, post);

                    imagePostViewHolder.mPicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EventDispatcher.dispatchTimelineImageClick(fullResUri.toString(),
                                    standarResUri.toString());
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

                animateViewIfNew(imagePostViewHolder.mCard, position);

                return true;

            //Case default: Not a valid post type
            default:
                return false;
        }
    }

    private void animateViewIfNew(View view, int newPosition) {
        if (newPosition > sLastPosition) {
            sLastPosition = newPosition;
            AnimationHelper.slideFromBelowWithFade(view.getRootView(),
                    Config.UI_TIMELINE_ANIMATION_DURATION);
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

}
