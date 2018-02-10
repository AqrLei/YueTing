package com.aqrlei.graduation.yueting.constant

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
object YueTingConstant {

    /*fragment tag*/
    val TAB_FRAGMENT_TAGS = arrayOf(
            "tag_fragment_home",
            "tag_fragment_read",
            "tag_fragment_music"
    )
    val TAG_FRAGMENT_HOME: Int = 0
    val TAG_FRAGMENT_READ: Int = 1
    val TAG_FRAGMENT_MUSIC: Int = 2
    /*database*/
    val DB_NAME = "yueting.db"
    val MUSIC_TABLE_NAME = "musicInfo"
    val MUSIC_TABLE_C = arrayOf(
            "path",
            "type",
            "fileInfo"
    )
    val MUSIC_TABLE_C_TYPE = arrayOf(
            "varchar unique not null",
            "integer default 0",
            "text"
    )
    val BOOK_TABLE_NAME = "bookInfo"
    val BOOK_TABLE_C = arrayOf(
            "path",
            "type",
            "fileInfo"
    )
    val BOOK_TABLE_C_TYPE = arrayOf(
            "varchar unique not null",
            "integer ",
            "text"
    )
    val MARK_TABLE_NAME = "markInfo"
    val MARK_TABLE_C = arrayOf(
            "path",
            "markPosition"
    )
    val MARK_TABLE_C_TYPE = arrayOf(
            "varchar",
            "integer unique"
    )
    val CATALOG_TABLE_NAME = "catalogInfo"
    val CATALOG_TABLE_C = arrayOf(
            "path",
            "catalogName",
            "catalogPosition"
    )
    val CATALOG_TABLE_C_TYPE = arrayOf(
            "varchar not null",
            "varchar",
            "integer unique"
    )
    /*sharedpreference*/
    val SF_NAME = "yueting"

    /*BroadcastReceiverAction*/
    val ACTION_BROADCAST = arrayOf(
            "play",
            "next",
            "previous",
            "finish",
            "single",
            "list",
            "random"
    )
    val ACTION_PLAY = 0
    val ACTION_NEXT = 1
    val ACTION_PREVIOUS = 2
    val ACTION_FINISH = 3
    val ACTION_SINGLE = 4
    val ACTION_LIST = 5
    val ACTION_RANDOM = 6
    val ACTION_REQ_CODE = arrayOf(
            1, 2, 3, 4, 5, 6, 7
    )
    /*HandlerMessageWhat*/
    val CURRENT_DURATION = 1
    val PLAY_STATE = 2
    val PLAY_TYPE = 3
    /*Book Chapter keyWord*/
    val CHAPTER_KEY_WORD = arrayOf(
            "第",
            "章",
            "节",
            "回"
    )
}