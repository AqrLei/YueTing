package com.example.testapp;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/10.
 */

public class BookMessage {
    private String name;
    private String path;
    private String encoding;
    private long accessTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }
}
