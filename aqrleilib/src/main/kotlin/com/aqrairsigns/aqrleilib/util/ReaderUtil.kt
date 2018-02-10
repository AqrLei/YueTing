package com.aqrairsigns.aqrleilib.util

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/21.
 */
enum class ReaderUtil {
    READER;

    private var strBuffer = StringBuffer()
    fun setString(str: String) {
        strBuffer.delete(0, strBuffer.length)
        strBuffer.append(str)
    }

    fun readLine(): String {
        if (strBuffer.isEmpty()) {
            return ""
        }
        val i0 = strBuffer.indexOf("\r\n")
        val i1 = strBuffer.indexOf("\n")
        val i2 = strBuffer.indexOf("\r")
        val str: String
        if (i0 < i1 && i0 == i2) {
            str = strBuffer.substring(0, strBuffer.indexOf("\r\n") + 2)
            strBuffer.delete(0, str.length)
        } else {
            str = if (i1 < i2) {
                strBuffer.substring(0, strBuffer.indexOf("\n") + 1)
            } else {
                strBuffer.substring(0, strBuffer.indexOf("\r") + 1)
            }
            strBuffer.delete(0, str.length)
        }
        return str
    }
}