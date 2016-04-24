package com.kubeiwu.faceunlock.receiver;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockScreenService extends Service {
	BroadcastReceiver mReceiver;
	KeyguardManager.KeyguardLock kl;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// ����ϵͳ����ҳ
		KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		kl = km.newKeyguardLock("IN");
		kl.disableKeyguard();

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		mReceiver = new LockScreenReceiver();
		registerReceiver(mReceiver, filter);
		System.out.println("服务启动了");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		kl.reenableKeyguard();
		unregisterReceiver(mReceiver);
	}

}
