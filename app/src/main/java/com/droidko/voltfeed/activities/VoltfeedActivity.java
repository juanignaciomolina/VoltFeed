package com.droidko.voltfeed.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public abstract class VoltfeedActivity extends ActionBarActivity {

    //Vars
    protected Context mContext;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());

        mContext = this;

        init();
        setUi();
        initFragments();
        populate();
        setListeners();
    }

    protected abstract void initFragments();

    protected abstract int layout();

    protected abstract void setUi();

    protected abstract void setListeners();

    protected abstract void populate();

    protected abstract void init();
    
    public FragmentManager getVoltfeedFragmentManager() {
        if (mFragmentManager == null) mFragmentManager = super.getSupportFragmentManager();
        return mFragmentManager;
    }

    public Context getContext() {
        return mContext;
    }

    protected void replaceFragment(int resId, Fragment f) {
        getVoltfeedFragmentManager()
                .beginTransaction()
                .replace(resId, f)
                .commit();
    }

    protected void replaceFragment(int resId, Fragment f, String tag) {
        getVoltfeedFragmentManager()
                .beginTransaction()
                .replace(resId, f, tag)
                .commit();
    }

    public void addFragment(int resId, Fragment f) {
        getVoltfeedFragmentManager()
                .beginTransaction()
                .add(resId, f)
                .commit();
    }

    public void addFragment(int resId, Fragment f, String tag) {
        getVoltfeedFragmentManager()
                .beginTransaction()
                .add(resId, f, tag)
                .commit();
    }

    public void removeFragment(Fragment f) {
        getVoltfeedFragmentManager()
                .beginTransaction()
                .remove(f)
                .commit();
    }

}
