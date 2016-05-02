package com.kubeiwu.faceunlock.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kubeiwu.faceunlock.R;

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
