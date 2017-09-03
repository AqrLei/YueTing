package com.leilei.guoshujinfu.mylearning.util.view;

import android.widget.BaseAdapter;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/2.
 */

public abstract class BaseSwipeListAdapter extends BaseAdapter {
    public boolean getSwipEnableByPosition(int position){
        return true;
    }

}
