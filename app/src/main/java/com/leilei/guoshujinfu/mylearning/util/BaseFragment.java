package com.leilei.guoshujinfu.mylearning.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/3
 */

public abstract class BaseFragment<T extends BaseActivity> extends Fragment {
    protected  T mContainerActivity;
    protected View mView;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BaseActivity) {
            mContainerActivity = ((T)context);
            AppLog.logDebug("context", ""+ mContainerActivity.getClass().getSimpleName());
           if (mContainerActivity == null)
            AppLog.logDebug("context", "context is null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        initComponents();
        return mView;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mContainerActivity != null) {
            mContainerActivity = null;
        }
    }
    public T getContainerActivity() {
        return mContainerActivity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected abstract int getLayoutRes();
    protected void initComponents() {

    }
    public void finish () {
        mContainerActivity.finish();
    }



}
