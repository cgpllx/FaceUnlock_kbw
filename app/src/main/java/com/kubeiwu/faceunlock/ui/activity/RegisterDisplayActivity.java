package com.kubeiwu.faceunlock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.kubeiwu.faceunlock.Constants;
import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.core.OnSuccessListener;
import com.kubeiwu.faceunlock.core.ProgressListener;
import com.kubeiwu.faceunlock.simple.app.SetPatternActivity;
import com.kubeiwu.faceunlock.ui.views.FaceView;
import com.kubeiwu.faceunlock.ui.views.Draw;
import com.kubeiwu.faceunlock.ui.views.Preview;

public class RegisterDisplayActivity extends BaseActivity implements ProgressListener, OnClickListener, OnSuccessListener {
	FrameLayout layout;
	Draw draw;
	View faceregisterdone;
	Preview mPreview;
	Button footerLeftButton;
	Button footerRightButton;

	// <com.konka.faceunlock.ui.views.MyView
	// android:layout_width="match_parent"
	// android:layout_height="match_parent" />
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
//		getSupportActionBar().hide();
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
		// new RegisterDisplayFragment(), "rig").commit();
		// layout = new FrameLayout(this);
		setContentView(R.layout.activity_registerdisplay);
		draw = (Draw) findViewById(R.id.draw);
		draw.setOnSuccessListener(this);
		mPreview = (Preview) findViewById(R.id.preview);
		faceregisterdone = findViewById(R.id.faceregisterdone);

		FaceView faceView = new FaceView(this);
		mPreview.setOnPreviewCallback(faceView);
		faceView.setOnProgressListener(this);

		footerLeftButton = (Button) findViewById(R.id.footerLeftButton);
		footerRightButton = (Button) findViewById(R.id.footerRightButton);
		footerRightButton.setEnabled(false);
		footerRightButton.setOnClickListener(this);
		footerLeftButton.setOnClickListener(this);

	
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	public void onPause() {
		super.onPause();
		mPreview.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
		mPreview.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onProgress(float pro) {
		if (draw != null) {
			draw.setProgress(pro);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.footerRightButton:
			Intent intent = new Intent(getApplicationContext(), SetPatternActivity.class);
			startActivity(intent);
			this.finish();

			break;
		case R.id.footerLeftButton:
			this.finish();
			break;

		}
	}

	@Override
	public void success() {
		Toast.makeText(getApplicationContext(), "人脸注册成功", 1).show();
		faceregisterdone.setVisibility(View.VISIBLE);
		footerRightButton.setEnabled(true);
		PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(Constants.FACEREGISTER_SUCCEE, true).commit();
	
		
	}

}
