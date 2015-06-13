package com.droidko.voltfeed.activities;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.ui.adapters.ViewPagerAdapter;
import com.droidko.voltfeed.ui.widget.SlidingTabLayout;
import com.droidko.voltfeed.utils.UiHelper;
import com.parse.ParseUser;

public class MainActivity extends VoltfeedActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private SlidingTabLayout mTabs;
    private int mNumbOfTabs = 2;
    private CharSequence mTitles[] = new CharSequence[mNumbOfTabs];
    private int mImageResources[] = new int[mNumbOfTabs];

    @Override
    protected void initFragments() {

    }

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUi() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);

        mToolbar = UiHelper.setToolbar(
                this,
                R.id.main_toolbar,
                R.id.toolbar_title,
                getString(R.string.general_app_name),
                R.id.toolbar_logo,
                R.drawable.ic_topbar);

        //If the OS version is LOLLIPOP or higher we use the elevation attribute,
        //otherwise we use a fake elevation with a degrade image. We have to do this
        //because the SlidingTabLayouts aren't compatible with elevation pre LOLLIPOP
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.support_elevation).setVisibility(View.GONE);
            mToolbar.setElevation(Config.UI_TOOLBAR_ELEVATION);
            mTabs.setElevation(Config.UI_TABS_ELEVATION);
        } else {
            getSupportActionBar().setElevation(0);
            findViewById(R.id.support_elevation).setVisibility(View.VISIBLE);
        }

        initTabs();
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void populate() {
    }

    @Override
    protected void init() {
        checkUserLoggedIn(); //IMPORTANT! Should be the first thing in the Activity lifecycle

        mTitles[0] = getString(R.string.fragment_timeline_name);
        mTitles[1] = getString(R.string.fragment_profile_name);
        mImageResources[0] = R.drawable.tab_timeline_img_selector;
        mImageResources[1] = R.drawable.tab_profile_img_selector;
    }

    private void initTabs() {
        //We instance a ViewPagerAdapater and provide it with a fragmentManager,
        // tittles and images for the tabs and the total amount of tabs
        mAdapter =  new ViewPagerAdapter(getVoltfeedFragmentManager(),mTitles, mNumbOfTabs, mImageResources);
        mPager.setAdapter(mAdapter);

        //Provide a custom view for the tabs (tab.xml)
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabView(R.layout.tab, R.id.tab_tv, R.id.tab_img);

        //Custom color for the tabs scroll
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        mTabs.setViewPager(mPager);
    }

    private void checkUserLoggedIn() {
        if (ParseUser.getCurrentUser() == null) {
            UiHelper.startActivityClearStack(getContext(), LogInActivity.class);
            finish();
        }
    }

    // ** EVENT BUS **

    public class LogInEvent { }

    public class NoInternetEvent { }

    // ** End of EVENT BUS **

}
