package com.aqrairsigns.aqrleilib.util

import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/21.
 */
enum class StringChangeUtil {
    SPANNABLE;

    private val builder = SpannableStringBuilder("")
    fun clear(): StringChangeUtil {
        builder.clear()
        return this
    }

    fun relativeSizeChange(size: Float, text: String): StringChangeUtil {
        val spannableString = SpannableString(text)
        val sizeSpan = RelativeSizeSpan(size)
        spannableString.setSpan(sizeSpan, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        builder.append(spannableString)
        return this
    }

    fun foregroundColorChange(color: String, text: String): StringChangeUtil {
        val spannableString = SpannableString(text)

        val colorSpan = ForegroundColorSpan(Color.parseColor(color))
        spannableString.setSpan(colorSpan, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        builder.append(spannableString)
        return this
    }

    fun complete() = builder
}