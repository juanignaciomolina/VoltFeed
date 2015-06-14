package com.droidko.voltfeed.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.entities.Post;
import com.droidko.voltfeed.ui.timeline.TimelineIdeaPostViewHolder;
import com.droidko.voltfeed.ui.timeline.TimelineImagePostViewHolder;

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
                        .inflate(R.layout.timeline_post_idea, null);
                return new TimelineIdeaPostViewHolder(itemLayoutView);

            //Case 0: Image post
            case 1:
                itemLayoutView = LayoutInflater.from(context)
                        .inflate(R.layout.timeline_post_image, null);
                return new TimelineImagePostViewHolder(itemLayoutView);

            //Case default: Not a valid post type
            default:
                return null;
        }

    }

    public static boolean populateTimelineViewHolder(final Post post,
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
                populateLikeView(ideaPostViewHolder.mLike);

                ideaPostViewHolder.mLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(Config.LOG_DEBUG, "On Like Click: " + post.getId());
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
                    Uri uri;
                    uri = Uri.parse(post.getPicture());
                    UiHelper.setProgessiveFrescoImage(imagePostViewHolder.mPicture,
                            uri,
                            null,
                            true);
                }
                populateLikeView(imagePostViewHolder.mLike);

                imagePostViewHolder.mLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(Config.LOG_DEBUG, "On Like Click: " + post.getId());
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

    private static void populateLikeView(ImageView view) {
        //todo desharcodear esto
        if (true) view.setImageResource(R.drawable.likeon);
        else view.setImageResource(R.drawable.likeoff);
    }

}
