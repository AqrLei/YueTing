package com.leilei.guoshujinfu.mylearning.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.util.AppToast;
import com.leilei.guoshujinfu.mylearning.util.view.SemiRoundBar;
import com.leilei.guoshujinfu.mylearning.util.view.TestDialog;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/30
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener{
    SemiRoundBar mSemiRoundBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_two);
        mSemiRoundBar = (SemiRoundBar) findViewById(R.id.srb_test);
        mSemiRoundBar.setProgress(100F);
        mSemiRoundBar.setMaxProgress(500F);
        mSemiRoundBar.setOpenAnimation(true);
        mSemiRoundBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDialog dialog = new TestDialog(TestActivity.this, R.layout.test_dialog);
                dialog.setTitle("是个测试");
                dialog.setMsg("是个测试\n就是个测试\n真得是个测试");
                dialog.setNegativeButton("测试专用0",null);
                dialog.setPostiveButton("测试专用1",TestActivity.this);
                dialog.show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        AppToast.toastShow(TestActivity.this,"测试用途");
    }
}
