/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.kubeiwu.faceunlock.simple.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.simple.util.PatternLockUtils;
import com.kubeiwu.faceunlock.simple.util.ToastUtils;

public class ResetPatternActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AppUtils.setupActionBar(this);

        setContentView(R.layout.reset_pattern_activity);

        findViewById(R.id.reset_pattern_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatternLockUtils.clearPattern(ResetPatternActivity.this);
                ToastUtils.show(R.string.pattern_reset, ResetPatternActivity.this);
                finish();
            }
        });

        findViewById(R.id.reset_pattern_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
