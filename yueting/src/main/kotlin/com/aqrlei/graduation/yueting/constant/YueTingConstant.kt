package com.aqrlei.graduation.yueting.constant

/**
 * @Author: AqrLei
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
    const val ADAPTER_TYPE_TITLE_BOOK = 2
    const val ADAPTER_TYPE_TITLE_MUSIC = 3
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
    const val MANAGE_REQ = 13
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
    const val PLAY_STATUS_PAUSE = 0
    const val PLAY_STATUS_PLAY = 1
    const val PLAY_TYPE_REPEAT = 0
    const val PLAY_TYPE_RANDOM = 1
    const val PLAY_TYPE_REPEAT_ONE = 2
    const val PLAY_EXPAND = 0
    const val PLAY_EXPAND_CLOSE = 1
    const val PLAY_SUFFIX_MP3 = "mp3"
    const val PLAY_SUFFIX_APE = "ape"
    const val PLAY_SUFFIX_FLAC = "flac"
    /**
     * Title
     */
    const val FRAGMENT_TITLE_TYPE = "type"
    const val FRAGMENT_TITLE_VALUE = "value"
    const val FRAGMENT_TITLE_TYPE_MUSIC = "music"
    const val FRAGMENT_TITLE_TYPE_BOOK = "book"
    /**
     * other
     */
    const val ENCODING = "UTF-16LE"
    const val TITLE_TYPE_MUSIC = 1
    const val TITLE_TYPE_BOOK = 0
    const val READ_SUFFIX_TXT = "txt"
    const val READ_SUFFIX_PDF = "pdf"
    const val FILE_TYPE_FOLDER = 1
    const val FILE_TYPE_MUSIC = 2
    const val FILE_TYPE_BOOK = 0
    const val INFO_UNKNOWN = "未知"
    const val READ_BACKGROUND_COLOR_DEFAULT = "#C7EECE"
    const val READ_BIG_FONT = 30F
    const val READ_SMALL_FONT = 15F
    const val READ_MIDDLE_FONT = 22F
    const val WHICH_JUMP_TO_FILE = "jump_key"
    const val YUE_TING_LIST_FILE = 0
    const val YUE_TING_FILE = 1

    const val MANAGE_TYPE_KEY = "key"
    const val MANAGE_TYPE_LIST = "list"
    const val MANAGE_TYPE_ITEM = "item"
    const val MANAGE_DATA = "data"


}