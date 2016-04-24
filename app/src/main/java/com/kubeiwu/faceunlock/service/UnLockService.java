package com.kubeiwu.faceunlock.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;

import com.kubeiwu.faceunlock.KonkaApplication;
import com.kubeiwu.faceunlock.core.OnUnLockListener;
import com.kubeiwu.faceunlock.pojo.PreviewFrameInfo;
import com.kubeiwu.faceunlock.util.Files;
import com.kubeiwu.faceunlock.util.KeyUtils;
import com.kubeiwu.faceunlock.util.Utils;
import com.konka.project.KonkaSo;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;

// can we use startFaceDetection on camera? probably not
@SuppressWarnings("deprecation")
public class UnLockService implements Camera.PreviewCallback {
	// private static final int N_MAX = 2;

	String modelpath;
	Context context;

	public UnLockService(Context context) {
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

	private OnUnLockListener mListener;

	public void setOnUnLockListener(OnUnLockListener listener) {
		mListener = listener;
	}

	public final ArrayList<PreviewFrameInfo> list = new ArrayList<PreviewFrameInfo>();
	int checkCount = 0;
	public static final int MAX_CHECKCOUNT = 3;
	public static final int LISTSIZECOUNT = 1;

	public void init() {
		checkCount = 0;
		list.clear();
	}

	// on main thread
	protected void processImage(final byte[] data, int width, int height) {
		if (list.size() >= LISTSIZECOUNT) {
			return;
		}
		// byte[] datas = new byte[data.length];
		// System.arraycopy(data, 0, datas, 0, datas.length);
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
							checkCount++;
							if (boolean1) {
								// TODO 进度更新
								if (mListener != null) {
									mListener.onLock(boolean1);
								}
							} else {
								if (checkCount >= MAX_CHECKCOUNT) {// 检查次数
									mListener.onLock(boolean1);
								} else {
									// 人脸不正确，继续添加
									list.clear();
								}
							}

						}
					});
		}

	}

	class Task implements Callable<Boolean> {
		private Bitmap bmp;

		public Task(Bitmap bmp) {
			super();
			this.bmp = bmp;
		}

		@Override
		public Boolean call() throws Exception {
			System.out.println("cgp====call=" + "");
			Matrix matrix = new Matrix();
			matrix.setRotate(-90);
			Bitmap newBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

			// faceDetector = new FaceDetector(newBitmap.getWidth(),
			// newBitmap.getHeight(), N_MAX);
			// faceDetector.
			// int nFace = faceDetector.findFaces(newBitmap.copy(Config.RGB_565,
			// true), face);
			// System.out.println("cgp=r人脸数" + nFace);
			// int nFace = 1;
			int[] results = KonkaSo.FaceDetect(Files.bitmap2intArray(newBitmap), modelpath, newBitmap.getWidth(), newBitmap.getHeight());
			int nFace =  results.length / 4;
			// System.out.println("cgp=results==" + results.length);
			if (nFace >= 1) {// KonkaSo解析太慢，过滤一下
				File[] key_file = KeyUtils.getKeyFiles(context);
//				File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "cgp.jpg");
//				if (!file.exists()) {
//					file.createNewFile();
//				}
//				FileUtil.BitmapToFile(newBitmap, file);
				System.out.println("keyfiles=" + key_file.length);
				UUID uuid_2 = getUUID(newBitmap);
				if (uuid_2 == null) {
					System.out.println("cgp====uuid_2=" + uuid_2);
					return false;
				}
				System.out.println("cgp====uuid_2=" + uuid_2);
				for (int i = 0; i < key_file.length; i++) {
					File key = key_file[i];
					Bitmap bit_key = BitmapFactory.decodeFile(key.getAbsolutePath());

					// float result_float =
					// KonkaSo.FaceVerification(Files.bitmap2intArray(bit_key),//
					// Files.bitmap2intArray(newBitmap), modelpath, //
					// newBitmap.getWidth(),//
					// newBitmap.getHeight());

					UUID uuid_1 = getUUID(bit_key);
					// System.out.println("cgp====uuid_1="+uuid_1);
					if (uuid_1 == null) {
						continue;
					}

					VerifyResult verifyResult = KonkaApplication.getFaceServiceClient().verify(uuid_1, uuid_2);

					if (verifyResult != null) {
						System.out.println("cgp====verifyResult=" + verifyResult.confidence);
						System.out.println("cgp====verifyResult=" + verifyResult.isIdentical);
						if (verifyResult.isIdentical) {
							return true;
						}
					}

					// boolean result = result_float > -4 && result_float <
					// 0.4f;
					// System.out.println("比较结果=" + result_float);
					// bit_key.recycle();
					// if (result) {
					// return result;
					// }
				}

			}
			return false;

		}
	}

	private UUID getUUID(Bitmap bitmap) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
			FaceServiceClient faceServiceClient = KonkaApplication.getFaceServiceClient();
			// Start detection.
			Face[] faces = faceServiceClient.detect(inputStream, true, false, null);
			if (faces != null && faces.length > 0) {
				UUID uuid_1 = faces[0].faceId;
				return uuid_1;
			}
		} catch (Exception e) {
			System.err.println("cgp===" + e);
		}
		return null;
	}

	// FaceDetector faceDetector = null;
	// FaceDetector.Face[] face;

	public boolean PreviewFrameInfoArrayToFile(PreviewFrameInfo[] previewFrameInfos) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<FutureTask<Boolean>> list = new ArrayList<FutureTask<Boolean>>();

		for (int i = 0; i < previewFrameInfos.length; i++) {
			PreviewFrameInfo info = previewFrameInfos[i];

			Bitmap bmp = Utils.previewFrameInfoToBitmap(info);
			Task task = new Task(bmp);
			FutureTask<Boolean> futureTask = new FutureTask<Boolean>(task);

			executor.submit(futureTask);

			list.add(futureTask);

		}
		executor.shutdown();
		for (FutureTask<Boolean> futureTask : list) {
			try {
				Boolean isPassed = futureTask.get();
				if (isPassed) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Observable<Boolean> getObservable(final PreviewFrameInfo[] previewFrameInfos) {
		Observable<Boolean> myObservable = Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> sub) {
				try {
					if (sub.isUnsubscribed()) {
						return;
					}
					long starttime = System.currentTimeMillis();
					boolean b = PreviewFrameInfoArrayToFile(previewFrameInfos);
					System.out.println("总共花费时间＝" + (System.currentTimeMillis() - starttime) / 1000 + "秒");
					sub.onNext(b);
					sub.onCompleted();
				} catch (Exception e) {
					sub.onError(new Throwable());
				}

			}
		});
		return myObservable;
	}

}
