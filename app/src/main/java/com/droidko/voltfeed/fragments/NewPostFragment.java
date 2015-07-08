package com.droidko.voltfeed.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.utils.AnimationHelper;
import com.droidko.voltfeed.utils.PublishHelper;
import com.droidko.voltfeed.utils.UiHelper;
import com.droidko.voltfeed.utils.ValidationHelper;

public class NewPostFragment extends Fragment {

    public static final String FRAGMENT_TAG = "NewPost";

    public View mContainerButtons;
    public View mContainerIdea;
    public EditText mEditTextIdea;
    public TextView mTextViewCharCounter;
    public View mButtonIdea;
    public View mButtonImage;
    public View mButtonCamera;
    public View mBackground;

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
        mEditTextIdea = (EditText) view.findViewById(R.id.fragment_new_post_edit_text_idea);
        mTextViewCharCounter =
                (TextView) view.findViewById(R.id.fragment_new_post_text_view_char_counter);
        mButtonIdea = view.findViewById(R.id.fragment_new_post_button_idea);
        mButtonImage = view.findViewById(R.id.fragment_new_post_button_image);
        mButtonCamera = view.findViewById(R.id.fragment_new_post_button_camera);
        mBackground = view.findViewById(R.id.fragment_new_post_background);

        UiHelper.setFontVarela(mTextViewCharCounter);

        setListeners();
        animateLayoutStart();
    }

    private void setListeners() {
        mButtonIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEditTextIdea.getText().toString())) return;
                if (!ValidationHelper.validateIdea(mEditTextIdea.getText().toString())) return;
                PublishHelper.publishIdea(String.valueOf(mEditTextIdea.getText()));
                animateLayoutPublishIdea();
                UiHelper.showToast(getActivity(), "Idea posted");
                UiHelper.removeFragment(getActivity(), NewPostFragment.this);
            }
        });
        mButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do something...
            }
        });
        mButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do something...
            }
        });
        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiHelper.removeFragment(getActivity(), NewPostFragment.this);
            }
        });
        mEditTextIdea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int charsAvailable = Config.POST_IDEA_MAX_CHAR_LENGHT - editable.length();
                mTextViewCharCounter.setText(String.valueOf(charsAvailable));
                if (charsAvailable >= 0)
                    mTextViewCharCounter.setTextColor(getResources().getColor(R.color.font_dark));
                else
                    mTextViewCharCounter.setTextColor(getResources().getColor(R.color.red));
            }
        });
    }

    private void animateLayoutPublishIdea() {
        AnimationHelper.slideToTop(mContainerButtons, 350);
        AnimationHelper.slideToTop(mContainerIdea, 500);
    }

    private void animateLayoutStart() {
        AnimationHelper.slideFromBelow(mContainerButtons, 300);
        AnimationHelper.slideFromBelow(mContainerIdea, 350);
    }
}
