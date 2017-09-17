package com.leilei.guoshujinfu.mylearning.util.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.leilei.guoshujinfu.mylearning.R;

import static android.text.TextUtils.isEmpty;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/9/6
 */

public class TestDialog {
    private Dialog mDialog;
    private Context mContext;
    private TextView mTitle;
    private TextView mMsg;
    private TextView mNegativeButton;
    private TextView mPositiveButton;
    private View mDivider;

    public TestDialog(Context context, int resId) {
        View v = LayoutInflater.from(context).inflate(resId, null);
        mTitle = (TextView) v.findViewById(R.id.tv_test_dialog_title);
        mMsg = (TextView) v.findViewById(R.id.tv_test_dialog_msg);
        mNegativeButton = (TextView) v.findViewById(R.id.tv_test_dialog_negative_button);
        mPositiveButton = (TextView) v.findViewById(R.id.tv_test_dialog_positive_button);
        mDivider = v.findViewById(R.id.v_test_dialog_divider);
        mDialog = new Dialog(context, R.style.TestDialog);
        mDialog.setContentView(v);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        v.setLayoutParams(new FrameLayout.LayoutParams((int) (metrics.widthPixels * 0.75),
                ViewGroup.LayoutParams.WRAP_CONTENT));
        new OnListener() {
            @Override
            public void onListener(int position) {

            }
        }.onListener(1);
    }

    public TestDialog setTitle(String title) {
        boolean empty = isEmpty(title);
        mTitle.setText(empty ? "无标题" : title);
        mTitle.setVisibility(View.VISIBLE);
        return this;
    }

    public TestDialog setMsg(String msg) {
        boolean empty = TextUtils.isEmpty(msg);
        mMsg.setText(TextUtils.isEmpty(msg) ? "无内容" : msg);
        mMsg.setVisibility(View.VISIBLE);
        return this;
    }

    public TestDialog setNegativeButton(String content, final View.OnClickListener listener) {
        mDivider.setVisibility(View.VISIBLE);
        boolean empty = TextUtils.isEmpty(content);

        mNegativeButton.setText(empty ? "0" : content);
        mNegativeButton.setVisibility(View.VISIBLE);
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) listener.onClick(v);
            }
        });
        return this;
    }

    public TestDialog setPostiveButton(String content, final View.OnClickListener listener) {
        mDivider.setVisibility(View.VISIBLE);
        boolean empty = TextUtils.isEmpty(content);
        mPositiveButton.setText(empty ? "1" : content);
        mPositiveButton.setVisibility(View.VISIBLE);
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) listener.onClick(v);
            }
        });
        return this;
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void show() {
        mDialog.show();
    }

    interface OnListener {
        public void onListener(int position);
    }
}
