package com.kubeiwu.faceunlock.ui.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.receiver.LockScreenService;
import com.kubeiwu.faceunlock.simple.app.SetPatternActivity;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this, LockScreenService.class);
        startService(intent);

        disableSystemLock();
        findViewById(R.id.settheunlockpattern).setOnClickListener(this);
        findViewById(R.id.registerFace).setOnClickListener(this);
    }

    /**
     * 屏蔽系统锁屏
     */
    private void disableSystemLock() {
        KeyguardManager mKeyguardManager = null;
        KeyguardManager.KeyguardLock mKeyguardLock = null;
        mKeyguardManager = (KeyguardManager) this.getSystemService(this.KEYGUARD_SERVICE);
        mKeyguardLock = mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
    }


    private void srartRegisterDisplayActivity() {
        Intent intent = new Intent(this, RegisterFaceTip1.class);
        startActivity(intent);
    }

    private void srartLockScreenService() {
        Intent intent = new Intent(getApplicationContext(), SetPatternActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settheunlockpattern:
                srartLockScreenService();
                break;
            case R.id.registerFace:
                srartRegisterDisplayActivity();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startSettingActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingActivity() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
