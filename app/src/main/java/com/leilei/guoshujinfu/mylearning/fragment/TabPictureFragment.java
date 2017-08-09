package com.leilei.guoshujinfu.mylearning.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.activity.MainActivity;
import com.leilei.guoshujinfu.mylearning.config.HttpReqConfig;
import com.leilei.guoshujinfu.mylearning.model.resp.PictureRespBean;
import com.leilei.guoshujinfu.mylearning.presenter.TabPicturePresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpFragment;
import com.leilei.guoshujinfu.mylearning.util.ui.viewpager.NormalAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class TabPictureFragment extends MvpFragment <TabPicturePresenter, MainActivity> {
    @BindView(R.id.vp_impage)
    ViewPager mVPImg;

    private List<PictureRespBean> mPictureRespBeans;
    private NormalAdapter<View> mNormalAdapter;
    private List<View> mImages;
    /*private int currentItem = 0;*/
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
               /* currentItem = (currentItem + 1) %(mPictureRespBeans.size() == 0 ?
                        (currentItem+1) : mPictureRespBeans.size());*/
                mVPImg.setCurrentItem(mVPImg.getCurrentItem()+1);
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void initComponents() {
        super.initComponents();
        mPresenter.getImg(HttpReqConfig.RQ_IMG_TYPE);
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }
    public void initViews(List<PictureRespBean> data) {
        mPictureRespBeans = new ArrayList<>();
        mPictureRespBeans.addAll(data);
        mImages = new ArrayList<>();
        for(int i = 0; i < mPictureRespBeans.size(); i++) {
            addImage(i);
        }
        mNormalAdapter = new NormalAdapter<>(mImages);
        mVPImg.setAdapter(mNormalAdapter);

    }
    private void addImage(int pos) {
        View view = LayoutInflater.from(this.getContainerActivity()).inflate(R.layout.picture_from_url, null);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_picture);
        GenericDraweeHierarchyBuilder builder =new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(getResources().getDrawable(R.mipmap.img_placeholder, null),
                        ScalingUtils.ScaleType.CENTER_INSIDE)
                .setProgressBarImage(getResources().getDrawable(R.mipmap.img_progress_bar, null),
                        ScalingUtils.ScaleType.CENTER_INSIDE)
                .setFailureImage(getResources().getDrawable(R.mipmap.img_load_failure, null),
                        ScalingUtils.ScaleType.CENTER_INSIDE)
                .setRetryImage(getResources().getDrawable(R.mipmap.img_retry, null),
                        ScalingUtils.ScaleType.FOCUS_CROP)
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER)
                .build();
        simpleDraweeView.setHierarchy(hierarchy);
        simpleDraweeView.setImageURI(Uri.parse(mPictureRespBeans.get(pos).getPictureUrl()));
        mImages.add(view);

    }

    public static TabPictureFragment newInstance() {
        Bundle args = new Bundle();
        TabPictureFragment fragment = new TabPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected TabPicturePresenter createPresenter() {
        return new TabPicturePresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_picture;
    }
}
