package com.droidko.voltfeed.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.droidko.voltfeed.fragments.ConnectionsFragment;
import com.droidko.voltfeed.fragments.ProfileFragment;
import com.droidko.voltfeed.fragments.TimelineFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private CharSequence mTitles[];
    private int mNumbOfTabs;
    private int mImgResources[];

    // Adapter's constructor with the necessary attributes
    public ViewPagerAdapter(FragmentManager fm,
                            CharSequence mTitles[],
                            int mNumbOfTabs,
                            int mImgResources[]) {
        super(fm);

        this.mTitles = mTitles;
        this.mNumbOfTabs = mNumbOfTabs;
        this.mImgResources = mImgResources;
    }

    //Return the fragment that corresponds to the select tab
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ConnectionsFragment connectionsFragment = new ConnectionsFragment();
                return connectionsFragment;
            case 1:
                TimelineFragment timelineFragment = new TimelineFragment();
                return timelineFragment;
            case 2:
            default:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
        }

    }

    //Return the title or image according to the select tab
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    //This method isn't an @Override because it's a customization, it's not inherited
    public int getPageImage(int position) {
        return mImgResources[position];
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}