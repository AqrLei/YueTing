package com.aqrairsigns.aqrleilib.test

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 10:09
 */

class EnumSingleton {
    companion object {
        val instance: EnumSingleton
            get() = Singleton.INSTANCE.instance
    }


    private enum class Singleton {
        INSTANCE;

        val instance: EnumSingleton = EnumSingleton()
    }


}
