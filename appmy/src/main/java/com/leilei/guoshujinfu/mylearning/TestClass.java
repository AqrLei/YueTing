package com.leilei.guoshujinfu.mylearning;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 15:24
 */

public class TestClass {
    String [][] test = new String[][] {{"m4a","audio"},{"mp3","audio"}};

    public String getTest() {
        String type = " ";
        for(int i= 0;i < test.length;i++) {
            if(type.equals(test[i][0])) {
                type = test[i][1];
            }

        }
        return "";
    }
}
