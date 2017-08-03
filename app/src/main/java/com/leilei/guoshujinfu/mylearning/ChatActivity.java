package com.leilei.guoshujinfu.mylearning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.leilei.guoshujinfu.mylearning.model.ChatMessage;
import com.leilei.guoshujinfu.mylearning.tool.ChatRecyclerViewAdapter;
import com.leilei.guoshujinfu.mylearning.util.AppCache;
import com.leilei.guoshujinfu.mylearning.util.AppLog;
import com.leilei.guoshujinfu.mylearning.util.AppToast;
import com.leilei.guoshujinfu.mylearning.util.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ChatActivity extends BaseActivity {
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMessageContent.setLayoutManager(layoutManager);
        mAdapter = new ChatRecyclerViewAdapter(mChatMessageList);
        mAdapter.setOnItemLongClickListener(new ChatRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("撤回消息？")
                        .setCancelable(false)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mChatMessageList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
                    AppLog.logDebug(AppLog.LOG_TAG_TEST,"withdrawMessage");

            }
        });
        mMessageContent.setAdapter(mAdapter);




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
    public void widthdrawMessage(int position){

        mChatMessageList.remove(position);
        mAdapter.notifyDataSetChanged();
    }


}
