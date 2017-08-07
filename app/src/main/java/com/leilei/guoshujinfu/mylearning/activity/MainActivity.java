package com.leilei.guoshujinfu.mylearning.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.model.req.PictureReqBean;
import com.leilei.guoshujinfu.mylearning.model.resp.PictureRespBean;
import com.leilei.guoshujinfu.mylearning.tool.presenter.MainPresenter;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends MvpActivity {
    @BindView(R.id.knife_test1)
    Button test1;
    @BindView(R.id.knife_test2)
    Button test2;
    @BindView(R.id.knife_test3)
    Button test3;
    @BindView(R.id.tab_home)
    TabLayout mTabLayoutm;
    @BindView(R.id.tv_resp)
    TextView respContents;

    private TabLayout.Tab mNTab;
    private TabLayout.Tab mMTab;
    private TabLayout.Tab mATab;

    private OkHttpClient client;

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);
        mNTab = mTabLayoutm.newTab().setText("test1");
        mMTab = mTabLayoutm.newTab().setText("test2");
        mATab = mTabLayoutm.newTab().setText("test3");
        mTabLayoutm.addTab(mNTab);
        mTabLayoutm.addTab(mMTab);
        mTabLayoutm.addTab(mATab);
        initOkHttp();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @OnClick({R.id.knife_test2, R.id.knife_test3, R.id.knife_test1})
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
    }
    private void initOkHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = new Request.Builder()
                                .addHeader("Content-Type","application/json")
                                .build();
                        return chain.proceed(request);
                    }
                })*/
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }
}
