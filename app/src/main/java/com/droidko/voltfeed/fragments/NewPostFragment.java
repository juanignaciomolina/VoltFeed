package com.droidko.voltfeed.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidko.voltfeed.R;

public class NewPostFragment extends Fragment {

    public static final String FRAGMENT_TAG = "NewPost";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

}
