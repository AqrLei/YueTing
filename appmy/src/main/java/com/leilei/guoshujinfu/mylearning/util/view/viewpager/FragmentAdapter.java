package com.leilei.guoshujinfu.mylearning.util.view.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/5.
 */

public class FragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> mFragments;
    public FragmentAdapter(FragmentManager fm, List<T> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getClass().getSimpleName();
    }
}
