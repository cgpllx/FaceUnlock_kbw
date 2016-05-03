package com.kubeiwu.faceunlock.receiver;

import java.io.File;

import com.kubeiwu.faceunlock.Constants;
import com.kubeiwu.faceunlock.simple.util.PatternLockUtils;
import com.kubeiwu.faceunlock.ui.activity.LockActivity;
import com.kubeiwu.faceunlock.ui.uitl.ParaSetting;
import com.kubeiwu.faceunlock.util.KeyUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class LockScreenReceiver extends BroadcastReceiver {

	private static final String TAG = "LockScreenReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("搜到广播了");
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			Log.e(TAG, "ACTION_SCREEN_OFF");

		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.e(TAG, "ACTION_SCREEN_ON");
			boolean faceregister_succee = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(Constants.FACEREGISTER_SUCCEE, false);
			boolean patteregister_succee = PatternLockUtils.hasPattern(context.getApplicationContext());
			File[] key_file = KeyUtils.getKeyFiles(context.getApplicationContext());
			if (ParaSetting.KAIGUAN.value) {
				if (!faceregister_succee) {
					Toast.makeText(context, "人脸没有注册成功", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!patteregister_succee) {
					Toast.makeText(context, "图案解锁没有注册成功", Toast.LENGTH_SHORT).show();
					return;
				}
				if (key_file == null || key_file.length < 1) {
					Toast.makeText(context, "人脸注册图片为0", Toast.LENGTH_SHORT).show();
					return;
				}
					Intent intent1 = new Intent(context, LockActivity.class);
					intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent1);
			}else{
				Toast.makeText(context, "人脸开关没有打开", Toast.LENGTH_SHORT).show();
			}

		} else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.e(TAG, "ACTION_BOOT_COMPLETED");
		}

	}
}
