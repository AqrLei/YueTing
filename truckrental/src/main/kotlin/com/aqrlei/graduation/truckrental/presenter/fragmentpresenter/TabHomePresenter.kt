package com.aqrlei.graduation.truckrental.presenter.fragmentpresenter

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.aqrlei.graduation.truckrental.R
import com.aqrlei.graduation.truckrental.baselib.mvp.MvpContract
import com.aqrlei.graduation.truckrental.ui.fragment.TabHomeFragment

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
class TabHomePresenter(mMvpView : TabHomeFragment):
        MvpContract.FragmentPresenter<TabHomeFragment>(mMvpView) {

    fun getTweenAnimation(context: Context): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.anim_test)
    }

    fun getFrameAnimation(v: View): AnimationDrawable {
        return v.background as AnimationDrawable
    }

    fun getAnimator(context: Context,v: View): Animator {
        val animator = AnimatorInflater.loadAnimator(context, R.animator.animator_test)
        animator.setTarget(v)
        return animator
    }

    fun getSpannableString(): SpannableStringBuilder {
        var wordBuilder = SpannableStringBuilder("")
        var spannableString1 = SpannableString("Hello World!\n")
        var spannableString = SpannableString("你好 世界！")
        var sizSpan = RelativeSizeSpan(0.75f)
        var colorSpan1 = ForegroundColorSpan(Color.parseColor("#FF7C943E"))
        var colorSpan2 = ForegroundColorSpan(Color.parseColor("#989223"))
        var styleSpan1 = StyleSpan(Typeface.BOLD)
        //var styleSpan2 = StyleSpan(Typeface.BOLD_ITALIC)
        var styleSpan3 = StyleSpan(Typeface.ITALIC)
        spannableString1.setSpan(colorSpan1, 0, spannableString1.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(sizSpan, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(colorSpan2, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(styleSpan1, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString1.setSpan(styleSpan3, 0, spannableString1.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        wordBuilder.append(spannableString1).append(spannableString)
        return wordBuilder
    }
}