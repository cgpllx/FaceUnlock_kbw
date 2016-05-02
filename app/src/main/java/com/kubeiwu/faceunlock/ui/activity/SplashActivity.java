package com.kubeiwu.faceunlock.ui.activity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.content.Intent;
import android.os.Bundle;

import com.kubeiwu.faceunlock.KonkaApplication;
import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.ui.fragment.ErrorDialogFragment;
import com.kubeiwu.faceunlock.util.FileUtil;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash);
		// getActionBar().hide();
//		getSupportActionBar().hide();
	
	}

	@Override
	protected void onStart() {
		super.onStart();
		Observable.create(new OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> arg0) {
				try {
					KonkaApplication app = (KonkaApplication) getApplication();
					FileUtil.copyResToSdcard(getApplicationContext(), app.getModelPath());
					arg0.onNext(true);
					arg0.onCompleted();
				} catch (Exception e) {
					e.printStackTrace();
					arg0.onError(e);
				}
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())//
				.delay(2, TimeUnit.SECONDS).subscribe(new Action1<Boolean>() {
					@Override
					public void call(Boolean arg0) {
						startMainActivity();
					}
				}, new Action1<Throwable>() {

					@Override
					public void call(Throwable arg0) {
						showErrorDialog();
					}
				});
	}

	protected void showErrorDialog() {
		new ErrorDialogFragment().show(getSupportFragmentManager(), "tag");
	}

	protected void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		// empty
	}
}
