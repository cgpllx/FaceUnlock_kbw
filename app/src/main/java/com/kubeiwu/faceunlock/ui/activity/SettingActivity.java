package com.kubeiwu.faceunlock.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.kubeiwu.commontool.view.setting.GroupView;
import com.kubeiwu.commontool.view.setting.KSettingView;
import com.kubeiwu.commontool.view.setting.RowView;
import com.kubeiwu.commontool.view.setting.viewimpl.CheckBoxRowView;
import com.kubeiwu.commontool.view.setting.viewimpl.DefaultRowView;
import com.kubeiwu.commontool.view.util.DisplayOptions;
import com.kubeiwu.commontool.view.util.Listener;
import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.ui.uitl.ParaSetting;


public class SettingActivity extends BaseActivity implements Listener.OnGroupViewItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        KSettingView kSettingView= (KSettingView) findViewById(R.id.ksettingview);
        initKSetting(kSettingView);

    }
    DisplayOptions selectorPara = new DisplayOptions.Builder()//
            .setNormalLineColorId(android.R.color.white)//
            .setDividerResId(R.drawable.line).setRowleftpadding(21)//
            .setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE).build();
    private void initKSetting(KSettingView containerView) {

        containerView.setDisplayOptions(selectorPara);
        GroupView groupView1 = containerView.addGroupViewItem(1);
        groupView1.setOnItemClickListener(this);

//        groupView1.addRowViewItem(DefaultRowView.class, 1, "注册人脸");
//        groupView1.addRowViewItem(DefaultRowView.class, 3, "设置解锁图案");
        groupView1.addRowViewItem(CheckBoxRowView.class, 4, "人脸解锁开关", R.drawable.setting_view_item_selector, ParaSetting.KAIGUAN);
        containerView.commit();

    }

    @Override
    public void onItemClick(GroupView groupView, RowView rowView) {

    }
}
