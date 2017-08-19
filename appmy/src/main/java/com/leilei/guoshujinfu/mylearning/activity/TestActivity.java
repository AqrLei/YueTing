package com.leilei.guoshujinfu.mylearning.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.presenter.TestPresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;

import butterknife.BindView;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/8
 */

public class TestActivity extends MvpActivity <TestPresenter> implements View.OnClickListener{
    @BindView(R.id.iv_view_anim)
    ImageView mViewAnimIv;
    @BindView(R.id.iv_pro_anim)
    ImageView mProAnimIV;

    private Animation anim;
    private ObjectAnimator animator;

    @Override
    protected TestPresenter createPresenter() {
        return new TestPresenter(this);
    }

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        anim.setFillAfter(true);
        mViewAnimIv.setOnClickListener(this);
        mProAnimIV.setOnClickListener(this);


    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_view_anim:
                mViewAnimIv.startAnimation(anim);
                break;
            case R.id.iv_pro_anim:
                animator = ObjectAnimator.ofFloat(mProAnimIV, "translationX",0, 200, 200, 200);
                animator.setDuration(1000);
                animator.start();

                break;
        }

    }
}
