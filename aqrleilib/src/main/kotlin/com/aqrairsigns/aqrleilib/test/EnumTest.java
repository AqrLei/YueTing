package com.aqrairsigns.aqrleilib.test;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/12 Time: 10:09
 */

public class EnumTest {
    private EnumTest() {
    }

    public static EnumTest getInstance() {
        return Singleton.INSTANCE.getInstance();
    }


    private enum Singleton {
        INSTANCE;
        private EnumTest singleton;

        Singleton() {
            singleton = new EnumTest();
        }


        public EnumTest getInstance() {
            return singleton;
        }
    }


}
