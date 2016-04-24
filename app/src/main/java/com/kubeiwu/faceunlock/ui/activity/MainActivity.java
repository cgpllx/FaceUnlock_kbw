package com.kubeiwu.faceunlock.ui.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.receiver.LockScreenService;
import com.kubeiwu.faceunlock.simple.app.SetPatternActivity;
import com.kubeiwu.faceunlock.ui.uitl.ParaSetting;
import com.kubeiwu.commontool.view.setting.GroupView;
import com.kubeiwu.commontool.view.setting.KSettingView;
import com.kubeiwu.commontool.view.setting.RowView;
import com.kubeiwu.commontool.view.setting.viewimpl.CheckBoxRowView;
import com.kubeiwu.commontool.view.setting.viewimpl.DefaultRowView;
import com.kubeiwu.commontool.view.util.DisplayOptions;
import com.kubeiwu.commontool.view.util.Listener.OnGroupViewItemClickListener;

public class MainActivity extends BaseActivity implements OnGroupViewItemClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(initView(selectorPara));
		Intent intent = new Intent(this, LockScreenService.class);
		startService(intent);

		disableSystemLock();

	}

	/**
	 * 屏蔽系统锁屏
	 */
	private void disableSystemLock() {
		KeyguardManager mKeyguardManager = null;
		KeyguardManager.KeyguardLock mKeyguardLock = null;
		mKeyguardManager = (KeyguardManager) this.getSystemService(this.KEYGUARD_SERVICE);
		mKeyguardLock = mKeyguardManager.newKeyguardLock("");
		mKeyguardLock.disableKeyguard();
	}
	protected View initView(DisplayOptions selectorPara) {
		KSettingView containerView = new KSettingView(this);

		containerView.setDisplayOptions(selectorPara);
		GroupView groupView1 = containerView.addGroupViewItem(1);
		groupView1.setOnItemClickListener(this);

//		if (faceregister_succee && patteregister_succee && key_file != null && key_file.length > 1) {
//			groupView1.addRowViewItem(DefaultRowView.class, 2, "重新注册人脸");
//			groupView1.addRowViewItem(DefaultRowView.class, 3, "重新设置解锁图案");
//		} else {
		groupView1.addRowViewItem(DefaultRowView.class, 1, "注册人脸");
		groupView1.addRowViewItem(DefaultRowView.class, 3, "设置解锁图案");
//		}
		groupView1.addRowViewItem(CheckBoxRowView.class, 4, "人脸解锁开关",R.drawable.setting_view_item_selector,ParaSetting.KAIGUAN);
		containerView.commit();
		return containerView;
	}

	DisplayOptions selectorPara = new DisplayOptions.Builder()//
			.setNormalLineColorId(android.R.color.white)//
			.setDividerResId(R.drawable.line).setRowleftpadding(21)//
			.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE).build();

	@Override
	public void onItemClick(GroupView arg0, RowView arg1) {
		switch (arg1.getItemId()) {
		case 1:
			srartRegisterDisplayActivity();
			break;
		case 2:
			srartRegisterDisplayActivity();
			break;
		case 3:
			srartLockScreenService();
			break;
		case 4:
//			srartLockScreenService();
			break;
		case 5:

			break;

		}
	}

	private void srartRegisterDisplayActivity() {
		Intent intent = new Intent(this, RegisterFaceTip1.class);
		startActivity(intent);
	}

	private void srartLockScreenService() {
		Intent intent = new Intent(getApplicationContext(), SetPatternActivity.class);
		startActivity(intent);
	}

}
