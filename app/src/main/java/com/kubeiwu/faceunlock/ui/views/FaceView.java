package com.kubeiwu.faceunlock.ui.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.FaceDetector;

import com.kubeiwu.faceunlock.Constants;
import com.kubeiwu.faceunlock.KonkaApplication;
import com.kubeiwu.faceunlock.core.ProgressListener;
import com.kubeiwu.faceunlock.pojo.PreviewFrameInfo;
import com.kubeiwu.faceunlock.util.FileUtil;
import com.kubeiwu.faceunlock.util.Files;
import com.kubeiwu.faceunlock.util.Utils;
import com.konka.project.KonkaSo;

// can we use startFaceDetection on camera? probably not
@SuppressWarnings("deprecation")
public class FaceView implements Camera.PreviewCallback {
	// private static final int N_MAX = 2;

	String modelpath;
	Context context;

	public FaceView(Context context) {
		this.context = context;
		KonkaApplication app = (KonkaApplication) context.getApplicationContext();
		modelpath = app.getModelPath();

		// face = new FaceDetector.Face[N_MAX];
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

	private ProgressListener mListener;

	public void setOnProgressListener(ProgressListener listener) {
		mListener = listener;
	}

	public final ArrayList<PreviewFrameInfo> list = new ArrayList<PreviewFrameInfo>();
	private int count = 1;
	public static final int KEYCOUNT = 2;
	private static final int LISTSIZECOUNT = 5;

	// on main thread
	protected void processImage(final byte[] data, int width, int height) {
		if (list.size() >= LISTSIZECOUNT) {
			return;
		}
		// System.out.println("data=" + data.length);
//		byte[] datas = new byte[data.length];
//		System.arraycopy(data, 0, datas, 0, datas.length);
		PreviewFrameInfo previewFrameInfo = new PreviewFrameInfo(data, width, height);

		list.add(previewFrameInfo);

		if (list.size() == LISTSIZECOUNT) {
			PreviewFrameInfo[] a = new PreviewFrameInfo[LISTSIZECOUNT];
			// PreviewFrameInfoArrayToFile(list.toArray(a));

			getObservable(list.toArray(a))//
					.subscribeOn(Schedulers.io())//
					.observeOn(AndroidSchedulers.mainThread())//
					.subscribe(new Action1<Boolean>() {
						@Override
						public void call(Boolean boolean1) {
							if (boolean1.booleanValue()) {
								// TODO 进度更新
								
								count++;
								if (mListener != null) {
									float pro = 1.0f * count / KEYCOUNT;
									mListener.onProgress(pro);
								}
							}  
							// System.out.println("count==" + count);
							if (count < KEYCOUNT) {
								list.clear();
							}
						}
					});
		}

	}

	class Task implements Callable<Boolean> {
		private final Bitmap bmp;

		public Task(Bitmap bmp) {
			super();
			this.bmp = bmp;
		}

		@Override
		public Boolean call() throws Exception {

			Matrix matrix = new Matrix();
			matrix.setRotate(-90);
			Bitmap newBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			bmp.recycle();

			// final Bitmap newBitmap = bmp;
			// faceDetector = new FaceDetector(newBitmap.getWidth(),
			// newBitmap.getHeight(), N_MAX);
			// int nFace = faceDetector.findFaces(newBitmap.copy(Config.RGB_565,
			// true), face);
			long startTime = System.currentTimeMillis();
			int[] results = KonkaSo.FaceDetect(Files.bitmap2intArray(newBitmap), modelpath, newBitmap.getWidth(), newBitmap.getHeight());
			int nFace = results.length / 4;
			System.out.println("nFace" + nFace);
			if (nFace == 0) {
				return false;
			}
			System.out.println("开始完成" + newBitmap.hashCode() + "时间＝" + (System.currentTimeMillis() - startTime) + "毫秒"+"count="+count);
			File file = FileUtil.getDiskCacheDir(context, Constants.FACEKEY);
			File imagefile = new File(file, "key" + count);
			FileUtil.BitmapToFile(newBitmap, imagefile);
			newBitmap.recycle();
			return true;
		}

	}

	FaceDetector faceDetector = null;

	// FaceDetector.Face[] face;

	public boolean PreviewFrameInfoArrayToFile(PreviewFrameInfo[] previewFrameInfos) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<FutureTask<Boolean>> list = new ArrayList<FutureTask<Boolean>>();
		// for (int i = 0; i < previewFrameInfos.length; i++) {
		// PreviewFrameInfo info = previewFrameInfos[i];
		//
		// Bitmap bmp = Utils.previewFrameInfoToBitmap(info);
		// Task task = new Task(bmp);
		// try {
		// Boolean bitmap = task.call();
		// if (bitmap.booleanValue()) {
		// // 保存。。
		// return true;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		for (int i = 0; i < previewFrameInfos.length; i++) {
			PreviewFrameInfo info = previewFrameInfos[i];

			Bitmap bmp = Utils.previewFrameInfoToBitmap(info);
			Task task = new Task(bmp);
			FutureTask<Boolean> futureTask = new FutureTask<Boolean>(task);
			list.add(futureTask);
			executor.submit(futureTask);
		}

		for (FutureTask<Boolean> futureTask : list) {
			Boolean bitmap;
			try {
				bitmap = futureTask.get();
				if (bitmap.booleanValue()) { // 保存。。
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// for (int i = 0; i < previewFrameInfos.length; i++) {
		//
		//
		//
		// PreviewFrameInfo info = previewFrameInfos[i];
		//
		// Bitmap bmp = Utils.previewFrameInfoToBitmap(info); Task task = new
		// Task(bmp); FutureTask<Boolean> futureTask = new
		// FutureTask<Boolean>(task);
		//
		// executor.submit(futureTask);
		// try {
		// Boolean bitmap = futureTask.get();
		// if (bitmap.booleanValue()) { // 保存。。
		// return true;
		// }
		// }
		// catch
		// }
		// (Exception e) { e.printStackTrace(); } list.add(futureTask);
		//
		// } executor.shutdown(); starttime = System.currentTimeMillis(); for
		// (FutureTask<Boolean> futureTask : list) { try {
		//
		// starttimesub = System.currentTimeMillis(); Boolean bitmap =
		// futureTask.get(); long endtime = System.currentTimeMillis(); ; if
		// (bitmap.booleanValue()) { // 保存。。 return true; }
		// System.out.println("每个＝＝" + (endtime - starttimesub) / 1000 + "秒");
		//
		// } catch (Exception e) { e.printStackTrace(); } } long endtime =
		// System.currentTimeMillis(); ; System.out.println((endtime -
		// starttime) / 1000 + "秒"); // Observable<T> // this.list.clear();
		//
		// }
		return false;
	}

	long starttimesub = 0;
	long starttime = 0;

	public Observable<Boolean> getObservable(final PreviewFrameInfo[] a) {
		Observable<Boolean> myObservable = Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> sub) {

				boolean b = PreviewFrameInfoArrayToFile(a);
				sub.onNext(b);
				sub.onCompleted();
			}
		});
		return myObservable;
	}

}
