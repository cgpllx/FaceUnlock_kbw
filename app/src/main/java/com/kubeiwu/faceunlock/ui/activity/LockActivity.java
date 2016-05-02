package com.kubeiwu.faceunlock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.kubeiwu.faceunlock.R;

import com.kubeiwu.faceunlock.ui.fragment.FaceViewFragment;
import com.kubeiwu.faceunlock.util.TimeUtils;

public class LockActivity extends BaseActivity  {
//	private Preview mPreview;
//	private UnLockService faceView;
	private TextView time;
	private TextView date;
//	private me.zhanghai.patternlock.PatternView patternView;
//	private View spotlight;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // 需要自己定义标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_unlock);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
//		getSupportActionBar().hide();
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);// 关键代码
		setContentView(R.layout.main);
		time = (TextView) findViewById(R.id.time);
		date = (TextView) findViewById(R.id.date);
//		patternView = (PatternView) findViewById(R.id.pl_pattern);
//		spotlight = findViewById(R.id.spotlight);

		time.setText(TimeUtils.getCurTime());
		date.setText(TimeUtils.getCurDayTime());
		getSupportFragmentManager().beginTransaction().replace(R.id.coutent, new FaceViewFragment(), "").commit();

//		mPreview = (Preview) findViewById(R.id.preview);
//		faceView = new UnLockService(this);
//		faceView.setOnUnLockListener(this);
//		mPreview.setOnPreviewCallback(faceView);

//		patternView.setInStealthMode(isStealthModeEnabled());
//		patternView.setOnPatternListener(this);

	}

//	@Override
//	public void onLock(boolean isunlock) {
//		if (isunlock) {
//			this.finish();
//			Toast.makeText(getApplicationContext(), "人脸解锁成功", Toast.LENGTH_SHORT).show();
//		} else {
//			Toast.makeText(getApplicationContext(), "人脸解锁失败，请使用图案解锁", Toast.LENGTH_SHORT).show();
//			// 启动手势解锁
//			patternView.setVisibility(View.VISIBLE);
//			mPreview.setVisibility(View.GONE);
//			spotlight.setVisibility(View.GONE);
//
//			// Intent intent = new Intent(this, ConfirmPatternActivity.class);
//			// startActivity(intent);
//		}
//	}

	@Override
	public void finish() {
		super.finish();

	}

	@Override
	protected void onStart() {
		super.onStart();
//		mPreview.onPause();
	}

	@Override
	public void onPause() {
		super.onPause();
		// mPreview.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();
//		 mPreview.onPause();
//		this.finish();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
//		mPreview.onResume();
	}

//	@Override
//	public void onAttachedToWindow() {
//		super.onAttachedToWindow();
//		mPreview.onResume();
//	}
//
//	@Override
//	public void onDetachedFromWindow() {
//		super.onDetachedFromWindow();
//		mPreview.onPause();
//	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		// mPreview.onPause();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
//		faceView.init();
		getSupportFragmentManager().beginTransaction().replace(R.id.coutent, new FaceViewFragment(), "dd").commit();
	}
//
//	@Override
//	public void onPatternStart() {
//
//		removeClearPatternRunnable();
//
//		// Set display mode to correct to ensure that pattern can be in stealth
//		// mode.
//		patternView.setDisplayMode(PatternView.DisplayMode.Correct);
//	}

//	@Override
//	public void onPatternCellAdded(List<PatternView.Cell> pattern) {
//	}
//
//	@Override
//	public void onPatternDetected(List<PatternView.Cell> pattern) {
//		if (isPatternCorrect(pattern)) {
//			onConfirmed();
//		} else {
//			// messageText.setText(R.string.pl_wrong_pattern);
//			patternView.setDisplayMode(PatternView.DisplayMode.Wrong);
//			postClearPatternRunnable();
//			// ViewAccessibilityCompat.announceForAccessibility(messageText,
//			// messageText.getText());
//			onWrongPattern();
//		}
//	}
//
//	protected void onWrongPattern() {
//		// ++numFailedAttempts;
//	}

//	@Override
//	public void onPatternCleared() {
//		removeClearPatternRunnable();
//	}

//	protected void onConfirmed() {
//		setResult(RESULT_OK);
//		finish();
//	}
//
//	protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
//		return PatternLockUtils.isPatternCorrect(pattern, this);
//	}
//
//	protected boolean isStealthModeEnabled() {
//		return false;
//	}
//
//	protected void removeClearPatternRunnable() {
//		patternView.removeCallbacks(clearPatternRunnable);
//	}
//
//	protected void postClearPatternRunnable() {
//		removeClearPatternRunnable();
//		patternView.postDelayed(clearPatternRunnable, CLEAR_PATTERN_DELAY_MILLI);
//	}

//	private final Runnable clearPatternRunnable = new Runnable() {
//		public void run() {
//			// clearPattern() resets display mode to DisplayMode.Correct.
//			patternView.clearPattern();
//		}
//	};
//	private static final int CLEAR_PATTERN_DELAY_MILLI = 2000;

}
