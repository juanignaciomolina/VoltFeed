package com.droidko.voltfeed.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidko.voltfeed.R;
import com.droidko.voltfeed.utils.UiHelper;

public class ConnectionsFragment extends Fragment {

    private TextView mTextViewUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_connections, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextViewUsername = (TextView) view.findViewById(R.id.fragment_profile_text_view_username);

        UiHelper.setFontVarela(mTextViewUsername);
    }
}
