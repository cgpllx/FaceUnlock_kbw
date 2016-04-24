package com.kubeiwu.faceunlock.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.simple.app.SetPatternActivity;

@SuppressWarnings("deprecation")
public class DisplayFragment1 extends BaseFragment implements OnClickListener, Callback {

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_registerdisplay;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// setContentView(R.layout.activity_test_camera);
		mTakePhoto = (Button) view.findViewById(R.id.take_photo);
		mTakePhoto.setOnClickListener(this); // 拍照按钮监听器

		startOrientationChangeListener(); // 启动设备方向监听器
		mSurfaceView = (SurfaceView) view.findViewById(R.id.my_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this); // 回调接口

		// 开启相机
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(1);
			// i=0 表示后置相机
		} else {
			mCamera = Camera.open();
		}
		mCamera.setFaceDetectionListener(faceDetectionListener);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mCamera.startFaceDetection();
			}
		}, 2000);
		// FaceDetectionListener
	}

	FaceDetectionListener faceDetectionListener = new FaceDetectionListener() {
		@Override
		public void onFaceDetection(Face[] faces, Camera camera) {
			System.out.println("补货到人脸了＝＝＝" + faces);

			if (faces != null && faces.length > 0) {
				for (Face face : faces) {
					System.out.println(face.rect.bottom);
					System.out.println(face.rect.left);
					System.out.println(face.rect.right);
					System.out.println(face.rect.top);
				}
			}
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private static final String TAG = "TestCameraActivity";
	public static final String KEY_FILENAME = "filename";
	private Button mTakePhoto;
	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private String mFileName;
	private OrientationEventListener mOrEventListener; // 设备方向监听器
	private Boolean mCurrentOrientation; // 当前设备方向 横屏false,竖屏true

	/* 图像数据处理还未完成时的回调函数 */
	private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
		@Override
		public void onShutter() {
			// 一般显示进度条

		}
	};
	List<byte[]> datas = new ArrayList<>();

	public int[] bypes2ints(byte[] datas) {
		int[] ints = new int[datas.length];
		for (int i = 0; i < datas.length; i++) {
			ints[i] = datas[i];
		}
		return ints;
	}

	/* 图像数据处理完成后的回调函数 */
	private Camera.PictureCallback mJpeg1 = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// 保存图片
			// datas.add(data);

			// if (datas.size() > 0) {
			// KonkaApplication app = (KonkaApplication)
			// getActivity().getApplication();
			// Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,
			// data.length);
			// Bitmap newBitmap = BitmapFactory.decodeByteArray(datas.get(0), 0,
			// datas.get(0).length);
			// int[] oldints =
			// com.konka.faceunlock.util.Files.bitmap2intArray(oldBitmap);
			// int[] newints =
			// com.konka.faceunlock.util.Files.bitmap2intArray(newBitmap);
			// boolean result = KonkaSo.FaceVerification(oldints, newints,
			// app.getModelPath(), oldBitmap.getWidth(), oldBitmap.getHeight());
			// Toast.makeText(getContext().getApplicationContext(), datas.size()
			// + "==" + result, 0).show();
			// System.out.println("result=" + result);
			// }
			Toast.makeText(getActivity().getApplicationContext(), datas.size() + "==", 0).show();
			datas.add(data);
			System.out.println("datas.size()" + datas.size());

			if (datas.size() >= 3) {
				// Intent intent = new Intent(getContext(),
				// SetPatternPreference.class);
				// startActivity(intent);
				startActivity(new Intent(getActivity(), SetPatternActivity.class));
				DisplayFragment1.this.getActivity().finish();
				return;
			}

			mCamera.setPreviewCallback(null);
			mCamera.stopSmoothZoom();
			mCamera.stopPreview();
			startCameraPreview();

			// Toast.makeText(getContext(), datas.size()+"", 0).show();
			//
			// mFileName = UUID.randomUUID().toString() + ".jpg";
			// Log.i(TAG, mFileName);
			// FileOutputStream out = null;
			// try {
			// out =
			// getContext().getApplicationContext().openFileOutput(mFileName,
			// Context.MODE_PRIVATE);
			// byte[] newData = null;
			// if (mCurrentOrientation) {
			// // 竖屏时，旋转图片再保存
			// Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0,
			// data.length);
			// Matrix matrix = new Matrix();
			// matrix.setRotate(90);
			// Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
			// oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// newBitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
			// newData = baos.toByteArray();
			// out.write(newData);
			// } else {
			// out.write(data);
			// }
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// } finally {
			// try {
			// if (out != null)
			// out.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			// Intent i = new Intent(TestCameraActivity.this,
			// ShowPicture.class);
			// i.putExtra(KEY_FILENAME, mFileName);
			// startActivity(i);
			// finish();
		}
	};

	private final void startOrientationChangeListener() {
		mOrEventListener = new OrientationEventListener(getActivity().getApplicationContext()) {
			@Override
			public void onOrientationChanged(int rotation) {
				if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315) || ((rotation >= 135) && (rotation <= 225))) {// portrait
					mCurrentOrientation = true;
					Log.i(TAG, "竖屏");
				} else if (((rotation > 45) && (rotation < 135)) || ((rotation > 225) && (rotation < 315))) {// landscape
					mCurrentOrientation = false;
					Log.i(TAG, "横屏");
				}
			}
		};
		mOrEventListener.enable();
	}

	@Override
	public void onStart() {
		super.onStart();
		// // 开启相机
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		// mCamera = Camera.open(1);
		// // i=0 表示后置相机
		// } else {
		// mCamera = Camera.open();
		// }
	}

	@Override
	public void onClick(View v) {
		// 点击拍照
		switch (v.getId()) {
		case R.id.take_photo:
			// mCamera.autoFocus(myAutoFocusCallback);
			// mCamera.takePicture(mShutter, null, mJpeg1);
			takePic();
			break;
		default:
			break;
		}

	}

	public void takePic() {
		mCamera.takePicture(mShutter, null, mJpeg1);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览

		Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象
		Size largestSize = getBestSupportedSize(parameters.getSupportedPreviewSizes());
		parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
		largestSize = getBestSupportedSize(parameters.getSupportedPictureSizes());// 设置捕捉图片尺寸
		parameters.setPictureSize(largestSize.width, largestSize.height);
		mCamera.setParameters(parameters);
		try {
			mCamera.startPreview();
		} catch (Exception e) {
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// SurfaceView创建时，建立Camera和SurfaceView的联系
		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// SurfaceView销毁时，取消Camera预览
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		// 释放相机
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}

	}

	private Size getBestSupportedSize(List<Size> sizes) {
		// 取能适用的最大的SIZE
		Size largestSize = sizes.get(0);
		int largestArea = sizes.get(0).height * sizes.get(0).width;
		for (Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				largestArea = area;
				largestSize = s;
			}
		}
		return largestSize;
	}

	/**
	 * 聚焦回调接口
	 */
	private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {

			camera.takePicture(mShutter, null, mJpeg1);
			camera.setPreviewCallback(null);
			camera.stopSmoothZoom();
			camera.stopPreview();
			startCameraPreview();
		}

	};

	public void startCameraPreview() {
		try {
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
