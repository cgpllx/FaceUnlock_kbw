/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.kubeiwu.faceunlock.simple.app;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import com.kubeiwu.faceunlock.simple.util.PatternLockUtils;
import com.kubeiwu.faceunlock.simple.util.PreferenceContract;
import com.kubeiwu.faceunlock.simple.util.PreferenceUtils;

import me.zhanghai.patternlock.PatternView;

public class ConfirmPatternActivity extends me.zhanghai.patternlock.ConfirmPatternActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AppUtils.setupActionBar(this);
    }

    @Override
    protected boolean isStealthModeEnabled() {
        return !PreferenceUtils.getBoolean(PreferenceContract.KEY_PATTERN_VISIBLE,
                PreferenceContract.DEFAULT_PATTERN_VISIBLE, this);
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        return PatternLockUtils.isPatternCorrect(pattern, this);
    }

    @Override
    protected void onForgotPassword() {

        startActivity(new Intent(this, ResetPatternActivity.class));

        // Finish with RESULT_FORGOT_PASSWORD.
        super.onForgotPassword();
    }
}
