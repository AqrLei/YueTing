package com.example.testapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements PageView.OnScrollListener {

    private PageFactory pageFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Book book = new Book();
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/太古神王.txt";
        File file = new File(path);
        book.setName(file.getName());
        book.setPath(file.getPath());
        book.setEncoding("GBK");

        PageView mView = (PageView) findViewById(R.id.pv_page);
        pageFactory = new PageFactory(mView, book);
        pageFactory.nextPage();
        mView.setOnScrollListener(this);
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            String str = getString(inputStream);
            TextView tv = (TextView) findViewById(R.id.tv_test);
            tv.setMovementMethod(ScrollingMovementMethod.getInstance());
            tv.setText(str);

        }

    }

    private String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onLeftScroll() {
        pageFactory.nextPage();

    }

    @Override
    public void onRightScroll() {
        pageFactory.prePage();
    }
   /* public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }*/
}
