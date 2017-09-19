package com.example.testapp;

import android.app.Application;
import android.content.Intent;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/19 Time: 14:29
 */

public class TestApplication extends Application {
    private String name = "application";
    private Intent testIntent;

    public String getName() {
        return name;
    }

    public Intent getTestIntent() {
        testIntent = new Intent(this, TestService.class);
        return testIntent;
    }

    @Override
    public void onTerminate() {
        this.stopService(testIntent);
        super.onTerminate();
    }
}
