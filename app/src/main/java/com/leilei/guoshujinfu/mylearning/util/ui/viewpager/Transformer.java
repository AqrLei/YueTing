package com.leilei.guoshujinfu.mylearning.util.ui.viewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/8/5.
 */


public class Transformer implements ViewPager.PageTransformer {
    /*
    * 往左滑动时：view1，view2，view3的position都是不断变小的。
    * 1 -> 0 ->-1 -> 负无穷大
    * 往右滑动时：view1，view2，view3的position都是不断变大的。
    * -1 -> 0 -> 1 -> 正无穷大
    * 故可以在[1,-1]设置view的平移，缩放，透明，旋转
    * */
    @Override
    public void transformPage(View page, float position) {

    }
}
