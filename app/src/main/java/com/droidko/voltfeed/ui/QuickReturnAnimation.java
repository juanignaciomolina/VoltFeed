package com.droidko.voltfeed.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.droidko.voltfeed.ui.widget.SlidingTabLayout;

public class QuickReturnAnimation extends RecyclerView.OnScrollListener {

    private LinearLayout mToolbarWrapper;
    private Toolbar mToolbar;
    private SlidingTabLayout mSlidingTabLayout;

    public QuickReturnAnimation(LinearLayout toolbarWrapper,
                                Toolbar toolbar,
                                SlidingTabLayout slidingTabLayout) {
        this.mToolbarWrapper = toolbarWrapper;
        this.mToolbar = toolbar;
        this.mSlidingTabLayout = slidingTabLayout;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (Math.abs(mToolbarWrapper.getTranslationY()) > mToolbar.getHeight()) {
                hideToolbar();
            } else {
                showToolbar();
            }
        }
    }

    private void showToolbar() {
        mToolbarWrapper
                .animate()
                .translationY(0)
                .start();
    }
    private void hideToolbar() {
        mToolbarWrapper
                .animate()
                .translationY(-mSlidingTabLayout.getBottom())
                .start();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        scrollColoredViewParallax(dy);
        if (dy > 0) {
            hideToolbarBy(dy);
        } else {
            showToolbarBy(dy);
        }
    }

    private void scrollColoredViewParallax(int dy) {
        //Use this to create a Parallax effect with an image
        //parallaxImage.setTranslationY(parallaxImage.getTranslationY() — dy / 3);
    }
    private void hideToolbarBy(int dy) {
        if (cannotHideMoreToolbar(dy)) {
            mToolbarWrapper.setTranslationY(-mSlidingTabLayout.getBottom());
        } else {
            mToolbarWrapper.setTranslationY(mToolbarWrapper.getTranslationY() - dy);
        }
    }

    private boolean cannotHideMoreToolbar(int dy) {
        return Math.abs(mToolbarWrapper.getTranslationY() - dy) > mSlidingTabLayout.getBottom();
    }
    private void showToolbarBy(int dy) {
        if (cannotShowMoreToolbar(dy)) {
            mToolbarWrapper.setTranslationY(0);
        } else {
            mToolbarWrapper.setTranslationY(mToolbarWrapper.getTranslationY() - dy);
        }
    }
    private boolean cannotShowMoreToolbar(int dy) {
        return (mToolbarWrapper.getTranslationY() - dy) > 0 ;
    }

}
