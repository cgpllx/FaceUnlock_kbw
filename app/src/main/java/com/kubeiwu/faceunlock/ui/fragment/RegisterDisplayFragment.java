package com.kubeiwu.faceunlock.ui.fragment;

import android.hardware.Camera;
import com.kubeiwu.faceunlock.R;

@SuppressWarnings("deprecation")
public class RegisterDisplayFragment extends BaseFragment implements Camera.PreviewCallback {

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_registerdisplay;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

	}

}
