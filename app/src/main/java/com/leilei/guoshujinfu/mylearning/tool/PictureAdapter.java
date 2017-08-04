package com.leilei.guoshujinfu.mylearning.tool;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.leilei.guoshujinfu.mylearning.util.AppLog;

import java.util.List;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/4
 */

public class PictureAdapter extends PagerAdapter {
    private List<View> view;

    public PictureAdapter(List<View> view) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"PictureAdapter");
        this.view = view;
    }

    @Override
    public int getCount() {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"getCount");
        return view == null? 0:view.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"isViewFromObject");
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"instantiateItem");
        View v = view.get(position);
        container.addView(v);
        return v;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"destroyItem");
        container.removeView(view.get(position));
    }

}
