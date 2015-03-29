package com.chaos.starke.core;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int layoutId = onCreateViewId();
        if (layoutId != 0) {
            this.setContentView(layoutId);
            ButterKnife.inject(this);
        }

        final ActionBar actionBar = getSupportActionBar();
        this.setUpActionBar(actionBar);
    }

    protected void setUpActionBar(final ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public abstract int onCreateViewId();
}
