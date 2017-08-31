package com.leilei.guoshujinfu.mylearning.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.util.view.SemiRoundBar;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/30
 */

public class TestActivity extends AppCompatActivity {
    SemiRoundBar mSemiRoundBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_two);
        mSemiRoundBar = (SemiRoundBar) findViewById(R.id.srb_test);
        mSemiRoundBar.setProgress(100F);
        mSemiRoundBar.setMaxProgress(500F);
        mSemiRoundBar.setOpenAnimation(true);
    }
}
