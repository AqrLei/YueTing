package com.aqrairsigns.aqrleilib.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aqrairsigns.aqrleilib.R

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/12.
 */
class PdfReadFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.fragment_pdf_read, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}