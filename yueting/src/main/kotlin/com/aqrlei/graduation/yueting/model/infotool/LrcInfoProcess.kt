package com.aqrlei.graduation.yueting.model.infotool

import com.aqrlei.graduation.yueting.model.LrcInfo
import java.io.*


/**
 * created by AqrLei at 22:49 on 星期六, 五月 05, 2018
 */
class LrcInfoProcess {
    val lrcList: ArrayList<LrcInfo>
            by lazy { ArrayList<LrcInfo>() }

    fun readLRC(path: String, suffix: String): String {
        val stringBuilder = StringBuilder()
        val f = File(path.replace(suffix, ".lrc"))
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
                    val lrcInfo = LrcInfo()
                    lrcInfo.lrcContent = lrcData[1]
                    lrcInfo.lrcTime = calLrcTime(lrcData[0])
                    lrcList.add(lrcInfo)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            stringBuilder.append("木有歌词文件，赶紧去下载！...")
        } catch (e: IOException) {
            e.printStackTrace()
            stringBuilder.append("木有读取到歌词哦！")
        }
        return stringBuilder.toString()
    }

    private fun calLrcTime(timeStr: String): Int {
        var temp = timeStr
        temp = temp.replace(":", ".")
        temp = temp.replace(".", "@")
        val timeData = temp.split("@")
        val minute = Integer.parseInt(timeData[0])
        val second = Integer.parseInt(timeData[1])
        val millisecond = Integer.parseInt(timeData[2])
        return (minute * 60 + second) * 1000 + millisecond * 10
    }

}