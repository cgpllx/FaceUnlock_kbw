package com.kubeiwu.faceunlock.ui.views;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressWarnings("deprecation")
public class Preview extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	Camera mCamera;
	Camera.PreviewCallback previewCallback;

	public Preview(Context context) {
		this(context, null);

	}

	public Preview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setKeepScreenOn(true);
		setFocusable(true);
	}

	public Preview(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		try {
			// TODO: use front facing if available
			System.out.println("cgp--mCamera=" + mCamera);
			mCamera = Camera.open(1);
			Size s=mCamera.getParameters().getPreviewSize();
			System.out.println("cgp=height="+s.height);
			System.out.println("cgp=width="+s.width);
			System.out.println();
		} catch (Exception e) {
			System.out.println("cgp--mCamera11111111111=" + mCamera);
			try {
				mCamera = Camera.open(0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		if (mCamera == null) {
			return;
		}
		mCamera.setDisplayOrientation(90);
		if (mCamera != null) {
			try {
				// setDisplayOrientation(mCamera, 90);
				mCamera.setPreviewDisplay(holder);
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
				// TODO: add more exception handling logic here
			}
		}
	}

	public void setOnPreviewCallback(Camera.PreviewCallback previewCallback) {
		this.previewCallback = previewCallback;
	}

	public void onPause() {
		// 释放相机
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);

			mCamera.release();
			mCamera = null;
		}
	}

	public void onResume() {
		try {
			mCamera = Camera.open(1);
		} catch (Exception e) {
			mCamera = Camera.open(0);
		}
		mCamera.setDisplayOrientation(90);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			System.out.println("size=" + size.width + "x" + size.height + "y");
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		int sizeslenth = sizes.size();
		if (sizeslenth > 0) {
			int z = sizeslenth >> 1;
			optimalSize = sizes.get(z);
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
//		// System.out.println();
		optimalSize.height = 720;
		optimalSize.width = 1280;
//		optimalSize.height = 960;
//		optimalSize.width = 1280;
		return optimalSize;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		if (mCamera == null) {
			return;
		}
		Parameters parameters = mCamera.getParameters();

		 List<Size> sizes = parameters.getSupportedPreviewSizes();
		 Size optimalSize = getOptimalPreviewSize(sizes, w, h);
		 parameters.setPreviewSize(optimalSize.width, optimalSize.height);

//		 System.out.println("cgp===width="+optimalSize.width);
//		 System.out.println("cgp===height="+optimalSize.height);
		// parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		// parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//
		// parameters.setZoom(3);
		// parameters.setPreviewFormat(pixel_format);
		 mCamera.setParameters(parameters);
		// mCamera.set
		if (previewCallback != null) {
			mCamera.setPreviewCallbackWithBuffer(previewCallback);
			Size size = parameters.getPreviewSize();
			byte[] data = new byte[size.width * size.height * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8];
//			System.out.println("cgp==ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()=="+ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()));
			mCamera.addCallbackBuffer(data);
		}
		mCamera.startPreview();
	}

	private int getOptimalZoomRatio(List<Integer> zoomRatios) {
		System.out.println("Zoom=" + zoomRatios.toArray());
		System.out.println("Zoom=" + zoomRatios);
		int zoomRatiossize = zoomRatios.size();
		if (zoomRatios.size() > 0) {
			int z = zoomRatiossize >> 1;
			return zoomRatios.get(z);
		}
		return zoomRatiossize;
	}

	// public void takePic(){
	// Camera.Parameters parameters = mCamera.getParameters();
	// if (previewCallback != null) {
	// mCamera.setPreviewCallbackWithBuffer(previewCallback);
	// Camera.Size size = parameters.getPreviewSize();
	// byte[] data = new byte[size.width*size.height*
	// ImageFormat.getBitsPerPixel(parameters.getPreviewFormat())/8];
	// mCamera.addCallbackBuffer(data);
	// }
	// }

}
