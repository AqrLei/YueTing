package com.leilei.guoshujinfu.mylearning;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leilei.guoshujinfu.mylearning.model.req.PictureReqBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BannerBean;
import com.leilei.guoshujinfu.mylearning.model.resp.BaseRespBean;
import com.leilei.guoshujinfu.mylearning.model.resp.PictureRespBean;
import com.leilei.guoshujinfu.mylearning.net.HttpReqHelper;
import com.leilei.guoshujinfu.mylearning.net.service.PictureInfoService;
import com.leilei.guoshujinfu.mylearning.tool.PictureAdapter;
import com.leilei.guoshujinfu.mylearning.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public class PictureActivity extends BaseActivity {
    @BindView(R.id.vp_impage)
    ViewPager mVPImg;
    @BindView(R.id.bt_post)
    Button mPost;

    private List<PictureRespBean> mPictureRespBeanList = new ArrayList<>();
    private PictureAdapter mPictureAdapter;
    private List<View> mViews;
    private int currentitem = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                currentitem = (currentitem + 1) % mPictureRespBeanList.size();
                mVPImg.setCurrentItem(currentitem);
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
        getImg();
        /*initViews();*/
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
        });
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
    private void initViews() {
        mViews = new ArrayList<>();
        for(int i = 0; i<mPictureRespBeanList.size(); i++) {
            addImgs(i);

        }
        mPictureAdapter = new PictureAdapter(mViews);
        mVPImg.setAdapter(mPictureAdapter);
    }
    private void addImgs(int pos){
        View view = LayoutInflater.from(this).inflate(R.layout.picture_from_url,null);
        SimpleDraweeView simpleDraweeView =  view.findViewById(R.id.sdv_picture);
        simpleDraweeView.setImageURI(Uri.parse(mPictureRespBeanList.get(pos).getPictureUrl()));
        mViews.add(view);
    }

    @OnClick(R.id.bt_post)
    public void onClick(){
        getImg();

    }
    public void getImg(){
        PictureReqBean pictureReqBean = new PictureReqBean("2");
        Subscription subscription = HttpReqHelper.getHttpHelper()
                .creatService(PictureInfoService.class)
                .getPicture(pictureReqBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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
        if(mPictureRespBeanList.size() > 0) {
           /* mPicture.setImageURI(Uri.parse(mPictureRespBeanList.get(1).getPictureUrl()));*/
        }

    }

}
