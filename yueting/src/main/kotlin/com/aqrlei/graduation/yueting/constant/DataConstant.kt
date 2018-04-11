package com.aqrlei.graduation.yueting.constant

/**
 * @author  aqrLei
 */
object DataConstant {
    /**
     * database's name
     */
    const val DB_NAME = "yueTing.db"
    /**
     * common column
     */
    const val COMMON_COLUMN_ID = "id"
    const val COMMON_COLUMN_CREATE_TIME = "createTime"

    const val COMMON_COLUMN_PATH = "path"
    /**
     * music's table
     */
    const val MUSIC_TABLE_NAME = "musicInfo"
    const val MUSIC_TABLE_C1_TYPE = "type"
    const val MUSIC_TABLE_C2_FILE_INFO = "fileInfo"
    const val MUSIC_TABLE_C0_DEF = "varchar unique not null"
    const val MUSIC_TABLE_C1_DEF = "integer DEFault 0"
    const val MUSIC_TABLE_C2_DEF = "text"
    /**
     * book's table
     */
    const val BOOK_TABLE_NAME = "bookInfo"
    const val BOOK_TABLE_C1_TYPE = "type"
    const val BOOK_TABLE_C2_INDEX_BEGIN = "indexBegin"
    const val BOOK_TABLE_C3_INDEX_END = "indexEnd"
    const val BOOK_TABLE_C4_FILE_INFO = "fileInfo"
    const val BOOK_TABLE_C0_DEF = "varchar unique not null"
    const val BOOK_TABLE_C1_DEF = "varchar"
    const val BOOK_TABLE_C2_DEF = "integer DEFault 0"
    const val BOOK_TABLE_C3_DEF = "integer DEFault 0"
    const val BOOK_TABLE_C4_DEF = "text"
    /**
     * mark's table
     */
    const val MARK_TABLE_NAME = "markInfo"
    const val MARK_TABLE_C1_MARK_POSITION = "markPosition"
    const val MARK_TABLE_C0_DEF = "varchar"
    const val MARK_TABLE_C1_DEF = "integer unique"
    /**
     * catalog's table
     */
    const val CATALOG_TABLE_NAME = "catalogInfo"
    const val CATALOG_TABLE_C1_CATALOG_NAME = "catalogName"
    const val CATALOG_TABLE_C2_CATALOG_POSITION = "catalogPosition"
    const val CATALOG_TABLE_C0_DEF = "varchar not null"
    const val CATALOG_TABLE_C1_DEF = "varchar"
    const val CATALOG_TABLE_C2_DEF = "integer unique"
}