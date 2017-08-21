package com.leilei.guoshujinfu.mylearning.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.presenter.TestPresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;
import com.leilei.guoshujinfu.mylearning.util.view.listview.MyAdapter;

import java.util.Arrays;

import butterknife.BindView;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/8
 */

public class TestActivity extends MvpActivity<TestPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_view_anim)
    ImageView mViewAnimIv;
    @BindView(R.id.iv_pro_anim)
    ImageView mProAnimIV;
    @BindView(R.id.lv_list_test)
    ListView mTestLv;
    @BindView(R.id.iv_suspension)
    ImageView mSuspensionIv;
    @BindView(R.id.ll_suspension)
    LinearLayout mSuspensionLl;
    @BindView(R.id.iv_suspension_temp)
    ImageView mSuspensionIv_temp;

    private Animation anim;
    private ObjectAnimator animator;
    private View mHeaderView;
    private View mSuspensionView;
    private MyAdapter mAdapter;

    /*
    * View Animation 位置变化后 点击有效位置为原始位置
    * Property Animation 位置变化后 点击有效位置随之变化
    * */

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
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.listheader_item, null);
        mSuspensionView = LayoutInflater.from(this).inflate(R.layout.listheader_suspension, null);
        mSuspensionIv.setVisibility(View.GONE);
        String[] mData = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        mAdapter = new MyAdapter(Arrays.asList(mData), this, R.layout.list_item);

        mTestLv.addHeaderView(mHeaderView);
        mTestLv.addHeaderView(mSuspensionView);
        mTestLv.setAdapter(mAdapter);
        mTestLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    mSuspensionIv_temp.setVisibility(View.VISIBLE);
                } else {
                    mSuspensionIv_temp.setVisibility(View.GONE);
                }

            }
        });


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
                animator = ObjectAnimator.ofFloat(mProAnimIV, "translationX", 0, 200, 200, 200);
                animator.setDuration(1000);
                animator.start();

                break;
        }

    }
}
