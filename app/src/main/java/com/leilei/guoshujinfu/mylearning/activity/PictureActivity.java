package com.leilei.guoshujinfu.mylearning.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.model.resp.PictureRespBean;
import com.leilei.guoshujinfu.mylearning.presenter.PicturePresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;
import com.leilei.guoshujinfu.mylearning.util.ui.viewpager.NormalAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public class PictureActivity extends MvpActivity<PicturePresenter> {
    @BindView(R.id.vp_impage)
    ViewPager mVPImg;
    @BindView(R.id.bt_post)
    Button mPost;


    private List<PictureRespBean> mPictureRespBeanList;
    private NormalAdapter mNormalAdapter;
    private List<View> mViews;
    private int currentitem = 0;
    /*每隔1秒轮播图片*/
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1 ) {
                /*做好除数为零的预防*/
                currentitem = (currentitem + 1) % (mPictureRespBeanList.size() == 0 ?
                        (currentitem+1):mPictureRespBeanList.size());
                mVPImg.setCurrentItem(mVPImg.getCurrentItem()+1);
                mHandler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_picture;
    }

    @Override
    protected void initComponents(Bundle savedInstanceState) {

        super.initComponents(savedInstanceState);
        mPresenter.getImg("2");

        /*initViews();
          mVPImg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        mHandler.sendEmptyMessageDelayed(1,1000);


/*      mVPImg.post(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(mPictureRespBeanList.size() != 0) {
                        currentitem = (currentitem + 1) % mPictureRespBeanList.size();
                        mVPImg.setCurrentItem(currentitem);
                    }
                }

            }
        });*/

        //mPicture.setImageURI(Uri.parse("http://daily.file.guoshujinfu.com/guoshu-ump//banner/3759e253-dc77-47dc-8bb1-add63e6d5208.png"));

    }

    @Override
    protected PicturePresenter createPresenter() {
        return new PicturePresenter(this);
    }
    /*初始化图片轮播视图*/
    public void initViews(List<PictureRespBean> data ) {
        mPictureRespBeanList = new ArrayList<>();
        mPictureRespBeanList.addAll(data);
        ViewPager viewPager = new ViewPager(this,null);
        mViews = new ArrayList<>();
        for(int i = 0; i<mPictureRespBeanList.size(); i++) {
            addImgs(i);
        }
        /*创建Adapter,向Adapter中传入List<T>*/
        mNormalAdapter = new NormalAdapter(mViews);
        /*ViewPager设置Adapter*/
        mVPImg.setAdapter(mNormalAdapter);
    }

    /*
    * 为每个SimpleDraweeView设置ImageURI
    * @param pos
    * */
    private void addImgs(int pos){
        /*获取View*/
        View view = LayoutInflater.from(this).inflate(R.layout.picture_from_url,null);
        /*fresco 中SimpleDraweeView 的简单使用,获取SimpleDrweeView*/
        SimpleDraweeView simpleDraweeView =  view.findViewById(R.id.sdv_picture);
        /*通过
        * GenericDraweeHierarchyBuilder
        * GenericDraweeHierarchy
        * 自定义SimpleDraweeView的效果
        * */
        GenericDraweeHierarchyBuilder builder =new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                /*
                * 1. 设置占位图片
                * 2. 设置加载时图片
                * 3. 设置加载失败时图片
                * 4. 设置重试图片
                * */
                .setPlaceholderImage(getResources().getDrawable(R.mipmap.img_placeholder, null),
                        ScalingUtils.ScaleType.CENTER_INSIDE)
                .setProgressBarImage(getResources().getDrawable(R.mipmap.img_progress_bar, null),
                        ScalingUtils.ScaleType.CENTER_INSIDE)
                .setFailureImage(getResources().getDrawable(R.mipmap.img_load_failure, null),
                        ScalingUtils.ScaleType.CENTER_INSIDE)
                .setRetryImage(getResources().getDrawable(R.mipmap.img_retry, null),
                        ScalingUtils.ScaleType.FOCUS_CROP)
                /*
                * 在代码中设置了翻页效果会覆盖掉xml文件中的所有效果（即使代码中没有设置，xml中的也会失效）设置，
                * 需要在代码中显式设置
                * */
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER)
                .build();
        /*设置效果*/
        simpleDraweeView.setHierarchy(hierarchy);
        /*设置图片*/

        simpleDraweeView.setImageURI(Uri.parse(mPictureRespBeanList.get(pos).getPictureUrl()));

        mViews.add(view);
    }


    /*
    * 网络请求获取图片的URl
    * 转移到Presenter中处理
    * */

   /* public void getImg(){
        *//*创建请求参数*//*
        PictureReqBean pictureReqBean = new PictureReqBean("2");
        *//*Subscription subscription(RxJava, RxAndroid)*//*
        subscription = HttpReqHelper.getHttpHelper()
                *//*根据service接口*//*
                .creatService(PictureInfoService.class)
                *//*执行的方法*//*
                .getPicture(pictureReqBean)
                *//*调用之后回到Android的UI线程（RxAndroid)*//*
                .observeOn(AndroidSchedulers.mainThread())
                *//*调用之前启动新线程*//*
                .subscribeOn(Schedulers.io())
                *//*订阅（执行回调方法）*//*
                .subscribe(new Observer<Response<BaseRespBean<BannerBean>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Amoryan", "onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("Amoryan", "onError");
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(Response<BaseRespBean<BannerBean>> baseRespBeanResponse) {
                        Log.d("Amoryan", "onNext");
                        if(baseRespBeanResponse != null) {
                            List<PictureRespBean> data = new ArrayList<PictureRespBean>();
                            data.addAll(baseRespBeanResponse.body().getData().getBannerConfig());
                            if(data.size() > 0) {
                                mPictureRespBeanList.addAll(data);
                                initViews();
                            }
                        }
                    }
                });
    }*/
}
