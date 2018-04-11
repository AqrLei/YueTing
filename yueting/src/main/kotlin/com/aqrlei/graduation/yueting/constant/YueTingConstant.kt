package com.aqrlei.graduation.yueting.constant

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
object YueTingConstant {
    /**
     * service key word
     */
    const val SERVICE_MUSIC_ITEM_POSITION = "music_item_position"
    const val SERVICE_MUSIC_MESSENGER = "music_messenger"
    const val SERVICE_NOTIFICATION_ID = 1
    const val SERVICE_PENDING_INTENT_ID = 0
    const val SERVICE_PLAY_STATUS = "play_status"
    const val SERVICE_PLAY_STATUS_B = "play_status_bundle"
    const val SERVICE_PLAY_AUDIO_SESSION = "play_audio_session"
    /**
     * adapter type
     */
    const val ADAPTER_TYPE_BOOK = 0
    const val ADAPTER_TYPE_MUSIC = 1
    /**
     * permission code
     */
    const val RQ_PERMISSION_CODE = 1
    /**
     * fragment tag
     * */
    const val TAB_FRAGMENT_HOME = "tag_fragment_home"
    const val TAB_FRAGMENT_READ_PDF = "tag_fragment_read_pdf"

    /**
     * handler message what
     * */
    const val CURRENT_DURATION = 1
    const val PLAY_STATE = 2
    const val PLAY_TYPE = 3
    /**
     * book chapter keyword
     * */
    const val CHAPTER_KEY_WORD = "(^\\s*第)(.{1,9})[章节卷集部篇回](\\s*)(.*)(\n|\r|\r\n)"

    /**
     * activity request code
     * */
    const val TXT_CATALOG_REQ = 10
    const val YUE_TING_FILE_REQ = 11
    const val PDF_CATALOG_REQ = 12
    /**
     * activity result code
     * */
    const val CATALOG_RES = 20
    const val YUE_TING_FILE_RES = 21
    /**
     * intent key
     */
    const val FILE_BOOK_CHANGE = "book_change"
    const val FILE_MUSIC_CHANGE = "music_change"
    const val READ_BOOK_INFO = "book_info"
    const val READ_BOOK_POSITION = "b_position"

    /**
     * play type code
     * */
    const val PLAY_SINGLE = 0
    const val PLAY_LIST = 1
    const val PLAY_RANDOM = 2
    /**
     * other
     */
    const val ENCODING = "UTF-16LE"

}