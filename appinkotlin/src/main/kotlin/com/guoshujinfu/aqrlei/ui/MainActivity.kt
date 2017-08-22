package com.guoshujinfu.aqrlei.ui

import android.animation.Animator
import android.animation.AnimatorInflater
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.guoshujinfu.aqrlei.R
import com.guoshujinfu.aqrlei.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/17
 */
class MainActivity : BaseActivity() {
    /*
    * @param anim Tween Animation
    * @param animator Property Animation
    * @param animDrawable Frame Animation(Drawable Animation)
    * */
    private lateinit var anim: Animation
    private lateinit var animator: Animator
    private lateinit var animDrawable: AnimationDrawable

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun initComponents(savedInstanceState: Bundle?) {
        super.initComponents(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        tv_spannable_test.text = wordBuilder
        //tv_spannable_test.compoundDrawables[1].level = 14

        /*Property Animation*/
        animator = AnimatorInflater.loadAnimator(this, R.animator.animator_test)
        animator.setTarget(tv_spannable_test)
        /*Frame Animation (Drawable Animation)*/
        animDrawable = iv_anim_test.background as AnimationDrawable
        /*Tween Animation*/
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_test)
        tv_spannable_test.setOnClickListener({


            animator.start()
            tv_spannable_test.startAnimation(anim)
            animDrawable.start()//(iv_anim_test.background as AnimationDrawable).start()

        })

    }

    override fun beforeSetContentView() {
        super.beforeSetContentView()
    }

}