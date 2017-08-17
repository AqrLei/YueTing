package com.leilei.guoshujinfu.mylearning.activity;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.config.MainConfig;
import com.leilei.guoshujinfu.mylearning.fragment.TabChatFragment;
import com.leilei.guoshujinfu.mylearning.fragment.TabHomeFragment;
import com.leilei.guoshujinfu.mylearning.fragment.TabPictureFragment;
import com.leilei.guoshujinfu.mylearning.presenter.MainPresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends MvpActivity<MainPresenter> implements RadioGroup.OnCheckedChangeListener{
    @BindView(R.id.rg_main_tab)
    RadioGroup mTabRG;
    /*android.app下的fragment会有各种问题
    * 用android.support.v4.app包下
    * 与此相同的还有toolbar
    *
    * */
    private List<Fragment> mFragments;
    private FragmentManager mFragmentManager;
    private TabHomeFragment mTabHomeFragment;
    private TabChatFragment mTabChatFragment;
    private TabPictureFragment mTabPictureFragment;
    private int currentTab;
    private LinearLayout mLinearLayout;

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        mTabRG.setOnCheckedChangeListener(this);
        mFragmentManager = getSupportFragmentManager();
        initFragments(savedInstanceState);
        changeFragment(MainConfig.TAB_HOME_POSITION);
       /* mNTab = mTabLayoutm.newTab().setText("test1");
        mMTab = mTabLayoutm.newTab().setText("test2");
        mATab = mTabLayoutm.newTab().setText("test3");
        mTabLayoutm.addTab(mNTab);
        mTabLayoutm.addTab(mMTab);
        mTabLayoutm.addTab(mATab);*/
       /* initOkHttp();*/
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

  /*  @OnClick({R.id.knife_test2, R.id.knife_test3, R.id.knife_test1})
    public void sayHi(Button button) {
        if(button.getId() == R.id.knife_test1) {

            String json = new Gson().toJson(new PictureReqBean("2"));
            String json1 = "{\"name\":\"leilei\"}";
            PictureRespBean bean = new Gson().fromJson(json1, PictureRespBean.class);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),json);
            Request request = new Request.Builder()
                    .url("http://daily.api.guoshujinfu.com/v4/client/config/")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String string = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            respContents.setText(string);
                        }
                    });

                }
            });

        }

        //button.setText("Hello, 你好");
    }*/
    /*private void initOkHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                *//*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = new Request.Builder()
                                .addHeader("Content-Type","application/json")
                                .build();
                        return chain.proceed(request);
                    }
                })*//*
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }*/
    private void initFragments(Bundle savedInstanceState) {
       mFragments = new ArrayList<>();
        if( savedInstanceState != null) {
            mTabHomeFragment = (TabHomeFragment) mFragmentManager.findFragmentByTag(
                    MainConfig.TAB_HOME_TAGS[MainConfig.TAB_HOME_POSITION]);
            mTabHomeFragment = mTabHomeFragment == null? TabHomeFragment.newInstance():
                    mTabHomeFragment;
            mTabChatFragment = (TabChatFragment) mFragmentManager.findFragmentByTag(
                    MainConfig.TAB_HOME_TAGS[MainConfig.TAB_CHAT_POSITION]);
            mTabChatFragment = mTabChatFragment == null? TabChatFragment.newInstance():
                    mTabChatFragment;
            mTabPictureFragment = (TabPictureFragment) mFragmentManager.findFragmentByTag(
                    MainConfig.TAB_HOME_TAGS[MainConfig.TAB_PICTURE_POSITION]);
            mTabPictureFragment = mTabPictureFragment == null? TabPictureFragment.newInstance():
                    mTabPictureFragment;

        }
        else {
            mTabHomeFragment = TabHomeFragment.newInstance();
            mTabChatFragment = TabChatFragment.newInstance();
            mTabPictureFragment = TabPictureFragment.newInstance();
        }
        mFragments.add(mTabHomeFragment);
        mFragments.add(mTabChatFragment);
        mFragments.add(mTabPictureFragment);

    }
    private void changeFragment(int position) {
        currentTab = position;
        for(int i = 0; i < mFragments.size(); i++) {
            Fragment currentFragment = mFragments.get(i);
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if(i == currentTab) {
                if(!currentFragment.isAdded()) {
                    ft.add(R.id.fl_fragment,currentFragment,MainConfig.TAB_HOME_TAGS[currentTab]);
                }
                ft.show(currentFragment);
            }
            else{
                ft.hide(currentFragment);
            }
            ft.commit();
        }


    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
        for(int i = 0; i<radioGroup.getChildCount(); i++) {
            if(radioGroup.getChildAt(i).getId() == checkId) {
                changeFragment(i);
            }
        }



    }
}
