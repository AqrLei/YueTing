package com.leilei.guoshujinfu.mylearning.util.ui.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/5.
 */

public class StateFragmentAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
    private List<T> mFragments;

    public StateFragmentAdapter(FragmentManager fm, List<T> mFragments) {
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
}
