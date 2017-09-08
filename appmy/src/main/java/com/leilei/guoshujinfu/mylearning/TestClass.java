package com.leilei.guoshujinfu.mylearning;

import java.io.File;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/8 Time: 15:24
 */

public class TestClass {
    private String name;
    private File[] file;

    public TestClass() {
    }

    public TestClass(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
