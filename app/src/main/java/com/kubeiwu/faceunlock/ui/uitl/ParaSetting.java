package com.kubeiwu.faceunlock.ui.uitl;

import com.kubeiwu.commontool.view.setting.annomotion.IsPara;
import com.kubeiwu.commontool.view.util.Para;
import com.kubeiwu.commontool.view.util.SettingsUtil;

import android.content.Context;

public interface ParaSetting {
	@IsPara
	Para<Boolean> KAIGUAN = new Para<Boolean>("key1", true);
 
	public class ParaUtil {
		public static void initPrar(Context context) {
			SettingsUtil.initPrar(context, ParaSetting.class);
		}
	}
}
