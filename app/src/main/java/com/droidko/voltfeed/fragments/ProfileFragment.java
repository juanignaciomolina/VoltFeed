package com.droidko.voltfeed.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.droidko.voltfeed.Schema;
import com.droidko.voltfeed.events.innerEvents.OnPublishEvent;
import com.droidko.voltfeed.events.innerEvents.OnVoltEvent;
import com.droidko.voltfeed.utils.ImagesHelper;
import com.droidko.voltfeed.utils.UiHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.parse.ParseUser;

import de.greenrobot.event.EventBus;

public class ProfileFragment extends Fragment {

    private TextView mTextViewUsername;
    private TextView mTextViewFollowers;
    private TextView mTextViewFollowersTitle;
    private TextView mTextViewVolts;
    private TextView mTextViewVoltsTitle;
    private TextView mTextViewPosts;
    private TextView mTextViewPostsTitle;
    private SimpleDraweeView mDraweeViewAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextViewUsername =
                (TextView) view.findViewById(R.id.fragment_profile_text_view_username);
        mTextViewFollowers =
                (TextView) view.findViewById(R.id.fragment_profile_text_view_followers);
        mTextViewFollowersTitle =
                (TextView) view.findViewById(R.id.fragment_profile_text_view_followers_title);
        mTextViewVolts =
                (TextView) view.findViewById(R.id.fragment_profile_text_view_volts);
        mTextViewVoltsTitle =
                (TextView) view.findViewById(R.id.fragment_profile_text_view_volts_title);
        mTextViewPosts =
                (TextView) view.findViewById(R.id.fragment_profile_text_view_posts);
        mTextViewPostsTitle =
                (TextView) view.findViewById(R.id.fragment_profile_text_view_posts_title);
        mDraweeViewAvatar =
                (SimpleDraweeView) view.findViewById(R.id.fragment_profile_image_view_avatar);

        UiHelper.setFontVarela(mTextViewFollowersTitle);
        UiHelper.setFontVarela(mTextViewVoltsTitle);
        UiHelper.setFontVarela(mTextViewPostsTitle);

        populateUi();
        loadAvatarImage();
    }

    private void populateUi() {
        mTextViewUsername.setText(ParseUser.getCurrentUser().getString(Schema.USER_COL_USERNAME));
        UiHelper.setFontVarela(mTextViewUsername);
        mTextViewFollowers.setText(
                String.valueOf(
                        ParseUser.getCurrentUser().getInt(Schema.USER_COL_FOLLOWERS_USERS_COUNT)));
        mTextViewVolts.setText(
                String.valueOf(
                        ParseUser.getCurrentUser().getInt(Schema.USER_COL_VOLTS_POSTS_COUNT)));
        mTextViewPosts.setText(
                String.valueOf(
                        ParseUser.getCurrentUser().getInt(Schema.USER_COL_POSTS_CREATED_COUNT)));
    }

    private void loadAvatarImage() {
        mDraweeViewAvatar.setVisibility(View.VISIBLE);

        String cloudinaryAvatarUrl = ImagesHelper
                .getFullScreenImage(ParseUser.getCurrentUser().getString(Schema.USER_COL_PICTURE));
        Uri avatarUri = Uri.parse(cloudinaryAvatarUrl);

        //Main request for the image to load
        ImageRequest avatarImageRequest = ImageRequestBuilder.newBuilderWithSource(avatarUri)
                .setLocalThumbnailPreviewsEnabled(true)
                .setProgressiveRenderingEnabled(true)
                .build();

        //Image loader controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(avatarImageRequest)
                .setTapToRetryEnabled(true)
                .setOldController(mDraweeViewAvatar.getController())
                .build();

        mDraweeViewAvatar.setController(controller);
    }

    //** EVENT BUS **
    public void onEvent(OnPublishEvent event){
        populateUi();
    }

    public void onEvent(OnVoltEvent event){
        populateUi();
    }
    //** End of EVENT BUS **
}
