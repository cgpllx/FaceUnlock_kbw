package com.kubeiwu.faceunlock;

import java.io.File;

import android.app.Application;
import android.content.Intent;

import com.kubeiwu.faceunlock.receiver.LockScreenService;
import com.kubeiwu.faceunlock.ui.uitl.ParaSetting;
import com.kubeiwu.faceunlock.util.FileUtil;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

public class KonkaApplication extends Application {
//	public static final String MODEL_FILE = "/storage/sdcard0/konka";
//	// 人脸解锁
//	public static final String MODEL_FACE_VERIFICATION1 = MODEL_FILE + "/subsml.xml";
//	// 人脸区域检测
//	public static final String MODEL_FACE_DETECT1 = MODEL_FILE + "/haarcascade_frontalface_alt.xml";

	public static final String UNIQUENAME = "konka";

	@Override
	public void onCreate() {
		super.onCreate();
		init();
		 sFaceServiceClient = new FaceServiceRestClient(getString(R.string.subscription_key));

	}
//    @Override
//    public void onCreate() {
//        super.onCreate();
//       
//    }

    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }

    private static FaceServiceClient sFaceServiceClient;

	private void init() {
		ParaSetting.ParaUtil.initPrar(getApplicationContext());
		String filepath = getModelPath();
		System.out.println("开始启动服务");
		Intent intent = new Intent(this, LockScreenService.class);
		startService(intent);
//		FileUtil.copyResToSdcard(getApplicationContext(), filepath);
	}



	public String modelpath;

	// 获取模型文件路径
	public String getModelPath() {
		if (modelpath == null) {
			File file = FileUtil.getDiskCacheDir(getApplicationContext(), UNIQUENAME);
			if (!file.exists()) {
				file.mkdirs();
			}
			modelpath = file.getAbsolutePath();
		}
		return modelpath;
	}
}
