package com.aqrlei.graduation.yueting.constant

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
object YueTingConstant {
    /*Chat Type*/
    val TYPE_MESSAGE_FLAG: Int = 5
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
            "path"
    )
    val MUSIC_TABLE_C_TYPE = arrayOf(
            "varchar unique"
    )
    /*sharedpreference*/
    val SF_NAME = "yueting"
}