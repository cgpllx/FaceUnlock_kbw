package com.kubeiwu.faceunlock.ui.fragment;

import java.util.List;

import me.zhanghai.patternlock.PatternView;
import me.zhanghai.patternlock.PatternView.OnPatternListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.core.OnUnLockListener;
import com.kubeiwu.faceunlock.service.UnLockService;
import com.kubeiwu.faceunlock.simple.util.PatternLockUtils;
import com.kubeiwu.faceunlock.ui.views.Preview;

public class FaceViewFragment extends Fragment implements OnUnLockListener, OnPatternListener{
	private Preview mPreview;
	private UnLockService faceView;
	private PatternView patternView;
	private View spotlight;
	private View draw;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_faceview, container, false);
		return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}
	private void initView(View view) {
		patternView = (PatternView) view.findViewById(R.id.pl_pattern);
		spotlight = view.findViewById(R.id.spotlight);
		draw = view.findViewById(R.id.draw);

		mPreview = (Preview) view.findViewById(R.id.preview);
		faceView = new UnLockService(getActivity().getApplicationContext());
		faceView.setOnUnLockListener(this);
		mPreview.setOnPreviewCallback(faceView);

		patternView.setInStealthMode(isStealthModeEnabled());
		patternView.setOnPatternListener(this);
	}

	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		
	}
	@Override
	public void onLock(boolean isunlock) {
		if (isunlock) {
			if(getActivity()!=null){
				Toast.makeText(getActivity().getApplicationContext(), "人脸解锁成功", Toast.LENGTH_SHORT).show();
				mPreview.onPause();
				getActivity().finish();
			}
		} else {
			if(getActivity()!=null){
				Toast.makeText(getActivity().getApplicationContext(), "人脸解锁失败，请使用图案解锁", Toast.LENGTH_SHORT).show();
			}
			// 启动手势解锁
			patternView.setVisibility(View.VISIBLE);
			mPreview.setVisibility(View.GONE);
			spotlight.setVisibility(View.GONE);
			draw.setVisibility(View.GONE);
			// Intent intent = new Intent(this, ConfirmPatternActivity.class);
			// startActivity(intent);
		}
	}
 

	@Override
	public void onPatternStart() {

		removeClearPatternRunnable();

		// Set display mode to correct to ensure that pattern can be in stealth
		// mode.
		patternView.setDisplayMode(PatternView.DisplayMode.Correct);
	}

	@Override
	public void onPatternCellAdded(List<PatternView.Cell> pattern) {
	}

	@Override
	public void onPatternDetected(List<PatternView.Cell> pattern) {
		if (isPatternCorrect(pattern)) {
			onConfirmed();
		} else {
			// messageText.setText(R.string.pl_wrong_pattern);
			patternView.setDisplayMode(PatternView.DisplayMode.Wrong);
			postClearPatternRunnable();
			// ViewAccessibilityCompat.announceForAccessibility(messageText,
			// messageText.getText());
			onWrongPattern();
		}
	}

	protected void onWrongPattern() {
		// ++numFailedAttempts;
	}

	@Override
	public void onPatternCleared() {
		removeClearPatternRunnable();
	}

	protected void onConfirmed() {
//		getActivity().setResult(Activity.RESULT_OK);
		if(getActivity()!=null){
			mPreview.onPause();
			getActivity().finish();
		}
	}

	protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
		return PatternLockUtils.isPatternCorrect(pattern, getActivity().getApplicationContext());
	}

	protected boolean isStealthModeEnabled() {
		return false;
	}

	protected void removeClearPatternRunnable() {
		patternView.removeCallbacks(clearPatternRunnable);
	}

	protected void postClearPatternRunnable() {
		removeClearPatternRunnable();
		patternView.postDelayed(clearPatternRunnable, CLEAR_PATTERN_DELAY_MILLI);
	}

	private final Runnable clearPatternRunnable = new Runnable() {
		public void run() {
			// clearPattern() resets display mode to DisplayMode.Correct.
			patternView.clearPattern();
		}
	};
	private static final int CLEAR_PATTERN_DELAY_MILLI = 2000;

}
