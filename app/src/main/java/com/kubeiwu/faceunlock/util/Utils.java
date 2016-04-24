package com.kubeiwu.faceunlock.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import com.kubeiwu.faceunlock.pojo.PreviewFrameInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

public class Utils {
	public static Bitmap byteToBitmap(byte[] imgByte) {
		InputStream input = null;
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		input = new ByteArrayInputStream(imgByte);
		SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(BitmapFactory.decodeStream(input, null, options));
		bitmap = softRef.get();
		if (imgByte != null) {
			imgByte = null;
		}

		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap previewFrameInfoToBitmap(PreviewFrameInfo info) {
		final YuvImage image = new YuvImage(info.getData(), ImageFormat.NV21, info.getWidth(), info.getHeight(), null);
		ByteArrayOutputStream os = new ByteArrayOutputStream(info.getData().length);
		if (!image.compressToJpeg(new Rect(0, 0, info.getWidth(), info.getHeight()), 100, os)) {
			try {
				os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		byte[] tmp = os.toByteArray();
		Bitmap bmp = Utils.byteToBitmap(tmp);
		try {
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return bmp;

	}
}
