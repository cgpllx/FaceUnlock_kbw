/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.kubeiwu.faceunlock.simple.app;

import android.os.Bundle;

import com.kubeiwu.faceunlock.R;

import com.kubeiwu.faceunlock.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//    	ActionBar actionBar = getSupportActionBar();
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.main_activity);
    }
}
