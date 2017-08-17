package com.guoshujinfu.aqrlei;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv_spannable_test);
        SpannableString spannableString = new SpannableString("你好 世界!");
        SpannableString spannableString1 = new SpannableString("Hello World");
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
        spannableString.setSpan(sizeSpan, 0, spannableString.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);

    }
}
