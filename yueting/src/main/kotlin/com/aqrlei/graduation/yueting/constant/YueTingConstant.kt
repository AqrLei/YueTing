package com.aqrlei.graduation.yueting.constant

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
object YueTingConstant {

    /**
     * fragment tag
     * */
    const val TAB_FRAGMENT_HOME = "tag_fragment_home"
    const val TAB_FRAGMENT_READ_PDF = "tag_fragment_read_pdf"

    /**
     * HandlerMessageWhat
     * */
    const val CURRENT_DURATION = 1
    const val PLAY_STATE = 2
    const val PLAY_TYPE = 3
    /**
     * Book Chapter keyWord
     * */
    const val CHAPTER_KEY_WORD = "(^\\s*第)(.{1,9})[章节卷集部篇回](\\s*)(.*)(\n|\r|\r\n)"


    /*Activity RequestCode*/
    const val TXT_REQ = 10
    const val YUE_TING_REQ = 11
    /*Activity ResultCode*/
    const val CATALOG_REQ = 20
    const val FILE_REQ = 21
}