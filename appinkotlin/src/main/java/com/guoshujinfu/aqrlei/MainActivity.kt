package com.guoshujinfu.aqrlei

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @Author: AqrLei
 *@Name MyLearning
 *@Description:
 *@Date: 2017/8/17
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var wordBuilder = SpannableStringBuilder("")
        var spannableString1 = SpannableString("Hello World!\n")
        var spannableString = SpannableString("你好 世界！")
        var sizSpan = RelativeSizeSpan(0.75f)
        var colorSpan1 = ForegroundColorSpan(Color.parseColor("#FF7C943E"))
        var colorSpan2 = ForegroundColorSpan(Color.parseColor("#989223"))
        var styleSpan1 = StyleSpan(Typeface.BOLD)
        var styleSpan2 = StyleSpan(Typeface.BOLD_ITALIC)
        var styleSpan3 = StyleSpan(Typeface.ITALIC)
        spannableString1.setSpan(colorSpan1, 0, spannableString1.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(sizSpan, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(colorSpan2, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(styleSpan1, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString1.setSpan(styleSpan3, 0, spannableString1.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        wordBuilder.append(spannableString1).append(spannableString)
        tv_spannable_test.text = wordBuilder

    }

}