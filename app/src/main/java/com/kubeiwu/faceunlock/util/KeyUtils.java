package com.kubeiwu.faceunlock.util;

import java.io.File;

import com.kubeiwu.faceunlock.Constants;

import android.content.Context;

public class KeyUtils {
	public static File[] getKeyFiles(Context context) {
		File file = FileUtil.getDiskCacheDir(context, Constants.FACEKEY);
		if (file != null && file.isDirectory()) {
			return file.listFiles();
		}
		return null;
	}
}
