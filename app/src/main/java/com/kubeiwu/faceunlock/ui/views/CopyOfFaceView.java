package com.kubeiwu.faceunlock.ui.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Subscriber;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Environment;
import android.view.View;

import com.kubeiwu.faceunlock.KonkaApplication;
import com.kubeiwu.faceunlock.util.Files;
import com.konka.project.KonkaSo;

// can we use startFaceDetection on camera? probably not
public class CopyOfFaceView extends View implements Camera.PreviewCallback {
	public static final int CONSISTENCY_SUBSAMPLING_FACTOR = 8;
	public static final int RECOGNITION_SUBSAMPLING_FACTOR = 4;

	public String displayedText = "点击屏幕以拍摄头像 - 此端在上.";

	public CopyOfFaceView(Context context) {
		super(context);

	}

	@Override
	public void onPreviewFrame(final byte[] data, final Camera camera) {
		try {
			Camera.Size size = camera.getParameters().getPreviewSize();
			processImage(data, size.width, size.height);
			camera.addCallbackBuffer(data);
		} catch (RuntimeException e) {
			// The camera has probably just been released, ignore.
			System.err.println(e.toString());
		}
	}

	private void createSubsampledImage(byte[] data, int width, int height, int f) {

	}

	int i = 0;
	String mFileName = null;
	AtomicBoolean checkFaceIng = new AtomicBoolean(false);

	// on main thread
	protected void processImage(final byte[] data, int width, int height) {
		// Size size = myCamera.getParameters().getPreviewSize(); //获取预览大小
		// final int w = size.width; //宽度
		// final int h = size.height;

		System.out.println(i + "");
		i++;

		final YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
		ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
		if (!image.compressToJpeg(new Rect(0, 0, width, height), 100, os)) {
			return;
		}
		byte[] tmp = os.toByteArray();
		System.out.println("tmp==" + tmp.length);
		Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
		try {
			os.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println("bmp==" + bmp);
		FileOutputStream out = null;
		Boolean boolean1 = checkFace(bmp, width, height);
		System.out.println("boolean1=" + boolean1);
		if (i < 3) {
			try {

				mFileName = i + "cgp.jpeg";
				try {
					File dirfile = Environment.getExternalStorageDirectory();
					File file = new File(dirfile, mFileName);
					System.out.println(file.getAbsolutePath());
					out = new FileOutputStream(file);
					// out =
					// getContext().getApplicationContext().openFileOutput(mFileName,
					// Context.MODE_PRIVATE);

					// Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0,
					// oldBitmap.getWidth(), oldBitmap.getHeight(), matrix,
					// true);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

					out.write(baos.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null)
							out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// doSomethingNeeded(bmp); //自己定义的实时分析预览帧视频的算法
		// postInvalidate();
	}

	FaceDetector faceDetector;
	FaceDetector.Face[] face;
	public static final int N_MAX = 1;

	public Boolean checkFace(Bitmap srcFace, int width, int height) {
		// faceDetector = new FaceDetector(width, height, N_MAX);
		// face = new FaceDetector.Face[N_MAX];
		// int nFace = faceDetector.findFaces(srcFace, face);
		KonkaApplication app = (KonkaApplication) getContext().getApplicationContext();
		String modelpath = app.getModelPath();
		 int[] results = KonkaSo.FaceDetect(Files.bitmap2intArray(srcFace),
		 modelpath, width, height);
		 System.out.println("boolean===" + results.length);
		// if (nFace > 0) {
		// return true;
		// }
		return false;
	}

	public Observable ddd(final Bitmap bitmap) {
		Observable<List<String>> myObservable = Observable.create(new Observable.OnSubscribe<List<String>>() {
			@Override
			public void call(Subscriber<? super List<String>> sub) {

				KonkaApplication app = (KonkaApplication) getContext().getApplicationContext();
				String modelpath = app.getModelPath();
				int[] results = KonkaSo.FaceDetect(Files.bitmap2intArray(bitmap), modelpath, bitmap.getWidth(), bitmap.getHeight());
				if (sub.isUnsubscribed()) {
					return;
				}
				FileOutputStream out;
				if (results.length == 4) {
					File dirfile = Environment.getExternalStorageDirectory();
					File file = new File(dirfile, mFileName);
					System.out.println(file.getAbsolutePath());
//					out = new FileOutputStream(file);
		 
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

//					out.write(baos.toByteArray());
					
					
					sub.onNext(null);
					sub.onCompleted();
				}else{
					sub.onError(new Throwable());
				}
			}
		});
		return myObservable;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(30);

		float textWidth = paint.measureText(displayedText);
		canvas.drawText(displayedText, (getWidth() - textWidth) / 2, 30, paint);

		paint.setStrokeWidth(2);
		paint.setColor(Color.RED);

		// if (faces != null) {
		// paint.setStrokeWidth(2);
		// paint.setStyle(Paint.Style.STROKE);
		// float scaleX = (float)getWidth()/grayImage.width();
		// float scaleY = (float)getHeight()/grayImage.height();
		// int total = faces.total();
		// for (int i = 0; i < total; i++) {//should only be 1
		// CvRect r = new CvRect(cvGetSeqElem(faces, i));
		// int x = r.x(), y = r.y(), w = r.width(), h = r.height();
		// //Commented out code works if using back facing camera
		// //canvas.drawRect(x*scaleX, y*scaleY, (x+w)*scaleX, (y+h)*scaleY,
		// paint);
		// canvas.drawRect(getWidth()-x*scaleX, y*scaleY,
		// getWidth()-(x+w)*scaleX, (y+h)*scaleY, paint);
		// }
		// }

	}
}
