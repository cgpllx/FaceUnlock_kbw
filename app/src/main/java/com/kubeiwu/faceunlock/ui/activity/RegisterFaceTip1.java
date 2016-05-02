package com.kubeiwu.faceunlock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.kubeiwu.faceunlock.R;

public class RegisterFaceTip1 extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registerfacetip1);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		findViewById(R.id.footerRightButton).setOnClickListener(this);
		findViewById(R.id.footerLeftButton).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.footerRightButton:
			startRegisterFaceActivity();
			break;
		case R.id.footerLeftButton:
			this.finish();
			break;
		}
	}

	private void startRegisterFaceActivity() {
		Intent intent = new Intent(this, RegisterFaceTip2.class);
		startActivity(intent);
		this.finish();
	}
}
