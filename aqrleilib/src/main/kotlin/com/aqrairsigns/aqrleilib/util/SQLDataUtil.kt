package com.aqrairsigns.aqrleilib.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/9/16.
 */
object SQLDataUtil {
    fun getData(data: ByteArray): Any? {
        val result: Any?
        val arrayInputStream = ByteArrayInputStream(data)
        var inputStream: ObjectInputStream? = null

        try {
            inputStream = ObjectInputStream(arrayInputStream)
            result = inputStream.readObject()
        } finally {
            arrayInputStream.close()
            inputStream?.close()
        }
        return result
    }

    fun saveData(data: Any): ByteArray? {
        val result: ByteArray?
        val arrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(arrayOutputStream)

        try {
            objectOutputStream.writeObject(data)
            objectOutputStream.flush()
            result = arrayOutputStream.toByteArray()
        } finally {
            arrayOutputStream.close()
            objectOutputStream.close()
        }
        return result
    }
}