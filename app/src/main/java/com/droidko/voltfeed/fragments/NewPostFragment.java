package com.droidko.voltfeed.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.utils.AnimationHelper;
import com.droidko.voltfeed.utils.ApiHelper;
import com.droidko.voltfeed.utils.ImagesHelper;
import com.droidko.voltfeed.utils.UiHelper;
import com.droidko.voltfeed.utils.ValidationHelper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NewPostFragment extends Fragment {

    //Constants
    public static final String FRAGMENT_TAG = "NewPost";
    private static final int INTENT_CODE_IMAGE = 0;
    private static final int INTENT_CODE_IMAGE_CAMERA = 1;

    //Settings
    private static final Bitmap.CompressFormat CAMERA_IMAGE_FORMAT = Bitmap.CompressFormat.WEBP;
    private static final int CAMERA_IMAGE_QUALITY = 100; //100 = no compression

    //Views
    private View mContainerButtons;
    private View mContainerIdea;
    private EditText mEditTextPostText;
    private TextView mTextViewCharCounter;
    private View mButtonPost;
    private View mButtonImage;
    private View mButtonCamera;
    private View mBackground;
    private SimpleDraweeView mDraweeViewSelectedImage;
    private ProgressBar mProgressBarUploading;

    //Other variables
    private View.OnClickListener mSelectImageClickListener;
    private View.OnClickListener mTakePictureClickListener;
    private View.OnClickListener mPublishIdeaClickListener;
    private View.OnClickListener mPublishImageClickListener;
    private Uri mSelectedImageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContainerButtons = view.findViewById(R.id.fragment_new_post_container_buttons);
        mContainerIdea = view.findViewById(R.id.fragment_new_post_container_idea);
        mEditTextPostText = (EditText) view.findViewById(R.id.fragment_new_post_edit_text_post_text);
        mTextViewCharCounter =
                (TextView) view.findViewById(R.id.fragment_new_post_text_view_char_counter);
        mButtonPost = view.findViewById(R.id.fragment_new_post_button_publish);
        mButtonImage = view.findViewById(R.id.fragment_new_post_button_image);
        mButtonCamera = view.findViewById(R.id.fragment_new_post_button_camera);
        mBackground = view.findViewById(R.id.fragment_new_post_background);
        mDraweeViewSelectedImage = (SimpleDraweeView)
                view.findViewById(R.id.fragment_new_post_fresco_selected_image);
        mProgressBarUploading = (ProgressBar)
                view.findViewById(R.id.fragment_new_post_progressbar_uploading);

        UiHelper.setFontVarela(mTextViewCharCounter);

        setListeners();
        animateLayoutStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            switch(requestCode) {

                case INTENT_CODE_IMAGE:
                    mSelectedImageUri = data.getData();
                    mButtonPost.setBackground(getResources()
                            .getDrawable(R.drawable.selector_button_flat_new_image));
                    mContainerButtons.setVisibility(View.GONE);
                    mDraweeViewSelectedImage.setImageURI(mSelectedImageUri);
                    mDraweeViewSelectedImage.setVisibility(View.VISIBLE);
                    mButtonPost.setOnClickListener(mPublishImageClickListener);
                    break;

                case INTENT_CODE_IMAGE_CAMERA:
                    mButtonPost.setBackground(getResources()
                            .getDrawable(R.drawable.selector_button_flat_new_image));
                    mContainerButtons.setVisibility(View.GONE);
                    mDraweeViewSelectedImage.setImageURI(mSelectedImageUri);
                    mDraweeViewSelectedImage.setVisibility(View.VISIBLE);
                    mDraweeViewSelectedImage.setOnClickListener(null);
                    ImagesHelper.galleryAddPic(mSelectedImageUri);
                    mButtonPost.setOnClickListener(mPublishImageClickListener);

                    break;

                default:
                    //Unknown requestCode, do nothing...
                    return;
            }
        }
    }

    //** Start of LISTENERS **
    private void setListeners() {
        mSelectImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Ensure that there's a gallery activity to handle the intent
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    NewPostFragment.this.startActivityForResult(i, INTENT_CODE_IMAGE);
                }
            }
        };

        mTakePictureClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = ImagesHelper.createImageFile();
                    } catch (IOException ex) {
                        Log.e(Config.LOG_ERROR, ex.toString());
                        UiHelper.showToast(R.string.error_unknown);
                    }
                    if (photoFile != null) {
                        mSelectedImageUri = Uri.fromFile(photoFile);
                        i.putExtra(MediaStore.EXTRA_OUTPUT,
                                mSelectedImageUri);
                        startActivityForResult(i, INTENT_CODE_IMAGE_CAMERA);
                    }
                }
            }
        };

        mPublishIdeaClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidationHelper.validatePostIdeaLenght(mEditTextPostText.getText().toString())) return;

                ApiHelper.publishIdea(String.valueOf(mEditTextPostText.getText()));
                animateLayoutPublishSuccessfull();
                UiHelper.showToast(R.string.fragment_new_post_publish_successful);
                UiHelper.removeFragment(getActivity(), NewPostFragment.this);
            }
        };

        mPublishImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidationHelper.validatePostImageLenght(mEditTextPostText.getText().toString())) return;

                mButtonPost.setEnabled(false);
                mEditTextPostText.setEnabled(false);
                mProgressBarUploading.setVisibility(View.VISIBLE);
                ImagesHelper.UploadImageTask task = new ImagesHelper.UploadImageTask(
                        new ImagesHelper.UploadImageTask.UploadFinishedCallback() {
                    @Override
                    public void onUploadFinished(List<String> imagesIds) {
                        if (imagesIds != null) {
                            ApiHelper.publishImage(String.valueOf(mEditTextPostText.getText()),
                                    imagesIds.get(0));
                            UiHelper.showToast(R.string.fragment_new_post_image_uploaded);
                            if (getActivity() != null) {
                                animateLayoutPublishSuccessfull();
                                UiHelper.removeFragment(getActivity(), NewPostFragment.this);
                            }
                        } else {
                            Log.e(Config.LOG_ERROR, "Image upload error");
                            UiHelper.showToast(R.string.error_unknown);
                            if (getActivity() != null) {
                                mButtonPost.setEnabled(true);
                                mEditTextPostText.setEnabled(true);
                                mProgressBarUploading.setVisibility(View.GONE);
                            }
                        }
                    }
                });
                task.execute(mSelectedImageUri);
            }
        };

        mButtonPost.setOnClickListener(mPublishIdeaClickListener);
        mButtonImage.setOnClickListener(mSelectImageClickListener);
        mButtonCamera.setOnClickListener(mTakePictureClickListener);
        mDraweeViewSelectedImage.setOnClickListener(mSelectImageClickListener);

        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiHelper.removeFragment(getActivity(), NewPostFragment.this);
            }
        });

        mEditTextPostText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int charsAvailable = Config.POST_MAX_CHAR_LENGHT - editable.length();
                mTextViewCharCounter.setText(String.valueOf(charsAvailable));
                if (charsAvailable >= 0)
                    mTextViewCharCounter.setTextColor(getResources().getColor(R.color.font_dark));
                else
                    mTextViewCharCounter.setTextColor(getResources().getColor(R.color.pallete_red));
            }
        });
    }
    //** End of LISTENERS **

    //** Start of ANIMATIONS **
    private void animateLayoutPublishSuccessfull() {
        AnimationHelper.slideToTop(mContainerButtons, 350);
        AnimationHelper.slideToTop(mContainerIdea, 500);
    }

    private void animateLayoutStart() {
        AnimationHelper.slideFromBelow(mContainerButtons, 300);
        AnimationHelper.slideFromBelow(mContainerIdea, 350);
    }
    //** End of ANIMATIONS **
}
