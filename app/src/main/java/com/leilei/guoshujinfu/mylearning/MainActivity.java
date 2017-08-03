package com.leilei.guoshujinfu.mylearning;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.Button;

import com.leilei.guoshujinfu.mylearning.util.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    @BindView(R.id.knife_test1)
    Button test1;
    @BindView(R.id.knife_test2)
    Button test2;
    @BindView(R.id.knife_test3)
    Button test3;
    @BindView(R.id.tab_home)
    TabLayout mTabLayoutm;

    private TabLayout.Tab mNTab;
    private TabLayout.Tab mMTab;
    private TabLayout.Tab mATab;

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        mNTab = mTabLayoutm.newTab().setText("test1");
        mMTab = mTabLayoutm.newTab().setText("test2");
        mATab = mTabLayoutm.newTab().setText("test3");
        mTabLayoutm.addTab(mNTab);
        mTabLayoutm.addTab(mMTab);
        mTabLayoutm.addTab(mATab);
    }

    @OnClick({R.id.knife_test2, R.id.knife_test3})
    public void sayHi(Button button) {

        button.setText("Hello, 你好");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }
}
