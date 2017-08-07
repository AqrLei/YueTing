package com.leilei.guoshujinfu.mylearning.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.model.ChatMessage;
import com.leilei.guoshujinfu.mylearning.presenter.ChatPresenter;
import com.leilei.guoshujinfu.mylearning.util.AppCache;
import com.leilei.guoshujinfu.mylearning.util.AppLog;
import com.leilei.guoshujinfu.mylearning.util.AppToast;
import com.leilei.guoshujinfu.mylearning.util.MvpActivity;
import com.leilei.guoshujinfu.mylearning.util.ui.recyclerview.ChatRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ChatActivity extends MvpActivity {
    @BindView(R.id.rlv_message_content)
    RecyclerView mMessageContent;
    @BindView(R.id.et_message)
    EditText mEditMessage;
    @BindView(R.id.iv_message_send)
    ImageView mMessageSend;
    @BindView(R.id.iv_message_clear)
    ImageView mMessageClear;

    private static  List<ChatMessage> mChatMessageList;
    private static ChatRecyclerViewAdapter mAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        super.initComponents(savedInstanceState);

        mChatMessageList = new ArrayList<>();
        /*
        * 获取RecyclerView.LayoutManager,有三个实现类
        * LinearLayoutManager: 线性，支持横向、纵向
        * GridLayoutManager: 网格
        * StaggeredGridLayoutManager: 瀑布流式
        * */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMessageContent.setLayoutManager(layoutManager);
        /*
        * 动画
        * RecyclerView.ItemAnimator
        * DefaultItemAnimator extends SimpleItemAnimator
        * */
        mMessageContent.setItemAnimator(new DefaultItemAnimator());
        /*
        * 分割线
        * RecyclerView.ItemDecoration
        * DividerItemDecoration
        * ...
        * */
        mMessageContent.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL),1);
        /*传入数据，创建Adapter实例*/
        mAdapter = new ChatRecyclerViewAdapter(mChatMessageList);
        /*点击事件在adapter中用回调接口实现
        * 匿名内部类的实现长按监听
        * */
        mAdapter.setOnItemLongClickListener(new ChatRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("撤回消息？")
                        .setCancelable(false)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /*更新RecyclerView*/
                                mChatMessageList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", null);
                builder.show();
                AppLog.logDebug(AppLog.LOG_TAG_TEST,"withdrawMessage");

            }
        });
        /*RecyclerView设置Adapter*/
        mMessageContent.setAdapter(mAdapter);




    }

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter(this);
    }

    @OnClick({R.id.iv_message_send, R.id.iv_message_clear})
    public void onCick(View v){
        AppLog.logDebug(AppLog.LOG_TAG_TEST,"onClick");
        switch (v.getId()) {
            case R.id.iv_message_send:
                String chatContent = mEditMessage.getText().toString();
                if (chatContent.equals("") || chatContent == null) {
                    AppToast.toastShow(ChatActivity.this, "没有内容！");
                    AppLog.logDebug(AppLog.LOG_TAG_TEST, "onClick_Null");
                    return;
                }
                int type = new Random().nextInt(10) % 10;
                AppLog.logDebug(AppLog.LOG_TAG_TEST, "onClick\t" + "type: " + type);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setType(type);
                chatMessage.setContent(chatContent);
                chatMessage.setAvatar(AppCache.getAvatar(ChatActivity.this));
                mChatMessageList.add(chatMessage);
                mEditMessage.setText("");
                break;
            case R.id.iv_message_clear:
                mChatMessageList.clear();
                break;
            default:

                break;
        }
        mAdapter.notifyDataSetChanged();


    }


}
