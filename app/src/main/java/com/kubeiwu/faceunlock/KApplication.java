package com.kubeiwu.faceunlock;

import java.io.File;

import android.app.Application;
import android.content.Intent;

import com.kubeiwu.faceunlock.receiver.LockScreenService;
import com.kubeiwu.faceunlock.ui.uitl.ParaSetting;
import com.kubeiwu.faceunlock.util.FileUtil;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

public class KApplication extends Application {


	public static final String UNIQUENAME = "konka";

	@Override
	public void onCreate() {
		super.onCreate();
		init();
		 sFaceServiceClient = new FaceServiceRestClient(getString(R.string.subscription_key));

	}


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
