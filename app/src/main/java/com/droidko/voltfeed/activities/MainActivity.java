package com.droidko.voltfeed.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.events.innerEvents.OnTimelineImageClickEvent;
import com.droidko.voltfeed.ui.adapters.ViewPagerAdapter;
import com.droidko.voltfeed.ui.widget.SlidingTabs.SlidingTabLayout;
import com.droidko.voltfeed.utils.ApiHelper;
import com.droidko.voltfeed.utils.UiHelper;
import com.parse.ParseUser;

import de.greenrobot.event.EventBus;

public class MainActivity extends VoltfeedActivity {

    private Toolbar mToolbar;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private SlidingTabLayout mTabs;
    private LinearLayout mQuickReturnWrapper;
    private int mNumbOfTabs = 3;
    private CharSequence mTitles[] = new CharSequence[mNumbOfTabs];
    private int mImageResources[] = new int[mNumbOfTabs];

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public SlidingTabLayout getTabs() {
        return mTabs;
    }

    public LinearLayout getQuickReturnWrapper() {
        return mQuickReturnWrapper;
    }

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
        mQuickReturnWrapper = (LinearLayout) findViewById(R.id.quickreturn_wrapper);

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
        if (UiHelper.canRunLollipopFx()) {
            findViewById(R.id.support_elevation).setVisibility(View.GONE);
            UiHelper.renderElevation(Config.UI_TOOLBAR_ELEVATION, mToolbar);
            UiHelper.renderElevation(Config.UI_TABS_ELEVATION, mTabs);
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
        ApiHelper.initVoltedPosts();

        mTitles[0] = getString(R.string.fragment_connections_name);
        mTitles[1] = getString(R.string.fragment_timeline_name);
        mTitles[2] = getString(R.string.fragment_profile_name);
        mImageResources[0] = R.drawable.selector_tab_img_connections;
        mImageResources[1] = R.drawable.selector_tab_img_timeline;
        mImageResources[2] = R.drawable.selector_tab_img_profile;
    }

    private void initTabs() {
        //We instance a ViewPagerAdapater and provide it with a fragmentManager,
        // tittles and images for the tabs and the total amount of tabs
        mAdapter =  new ViewPagerAdapter(getVoltfeedFragmentManager(),mTitles, mNumbOfTabs, mImageResources);
        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());

        //Provide a custom view for the tabs (tab.xml)
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabView(R.layout.tab, R.id.tab_tv, R.id.tab_img);

        //Custom color for the tabs scroll
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabs_scroll);
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // ** EVENT BUS **

    public void onEvent(OnTimelineImageClickEvent event) {
        Intent i = new Intent(this, FullscreenImageActivity.class);
        i.putExtra(FullscreenImageActivity.EXT_FULL_RES_URI, event.getFullResUri());
        i.putExtra(FullscreenImageActivity.EXT_LOW_RES_URI, event.getLowResUri());
        startActivity(i);
    }

    // ** End of EVENT BUS **

}
