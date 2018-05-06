package com.aqrlei.graduation.yueting.model.infotool

import android.util.Log
import com.aqrairsigns.aqrleilib.util.FileUtil
import com.aqrlei.graduation.yueting.model.LrcInfo
import java.io.*


/**
 * created by AqrLei at 22:49 on 星期六, 五月 05, 2018
 */
object LrcInfoProcess {
    private val lrcList: ArrayList<LrcInfo>
            by lazy { ArrayList<LrcInfo>() }

    fun readLRC(path: String): List<LrcInfo> {
        val suffix = FileUtil.getFileSuffix(path)
        val f = File(path.replace(suffix, "lrc"))
        Log.d("lrc", path.replace(suffix, "lrc"))
        try {
            val fis = FileInputStream(f)
            val isr = InputStreamReader(fis, "utf-8")
            val br = BufferedReader(isr)
            var s: String? = null
            while (s.let {
                        s = br.readLine() ?: null
                        s != null
                    }) {
                s = s!!.replace("[", "")
                s = s!!.replace("]", "@")
                val lrcData = s!!.split("@")
                if (lrcData.size > 1) {
                    for (i in 0 until lrcData.size - 1) {
                        val lrcInfo = LrcInfo()
                        lrcInfo.lrcContent = lrcData[lrcData.size - 1]
                        calLrcTime(lrcData[i])?.let {
                            lrcInfo.lrcTime = it
                            lrcList.add(lrcInfo)
                        } ?: continue
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lrcList.toList().sorted()
    }

    private fun calLrcTime(timeStr: String): Int? {
        var temp = timeStr
        temp = temp.replace(":", ".")
        temp = temp.replace(".", "@")
        val timeData = temp.split("@")
        return try {
            val minute = Integer.parseInt(timeData[0])
            val second = Integer.parseInt(timeData[1])
            val millisecond = Integer.parseInt(timeData[2])
            (minute * 60 + second) * 1000 + millisecond * 10
        } catch (e: NumberFormatException) {
            null
        }


    }

}