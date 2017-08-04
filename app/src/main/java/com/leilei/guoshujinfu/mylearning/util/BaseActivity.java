package com.leilei.guoshujinfu.mylearning.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/3.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public BaseActivity() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(getLayoutRes());
        initComponents(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remove(this);
    }

    protected abstract int getLayoutRes();


    protected void initComponents(Bundle savedInstanceState){
        ButterKnife.bind(this);
        ActivityCollector.add(this);
        AppLog.logDebug(AppLog.LOG_TAG_ACTIVITY,"当前的activity是：" + this.getClass().getSimpleName());

    }




}
