package com.leilei.guoshujinfu.mylearning.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.activity.MainActivity;
import com.leilei.guoshujinfu.mylearning.model.ChatMessage;
import com.leilei.guoshujinfu.mylearning.presenter.TabChatPresenter;
import com.leilei.guoshujinfu.mylearning.util.AppCache;
import com.leilei.guoshujinfu.mylearning.util.AppToast;
import com.leilei.guoshujinfu.mylearning.util.MvpFragment;
import com.leilei.guoshujinfu.mylearning.util.ui.recyclerview.ChatRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/7
 */

public class TabChatFragment extends MvpFragment <TabChatPresenter, MainActivity> {
    @BindView(R.id.rlv_message_content)
    RecyclerView mMessageContent;
    @BindView(R.id.et_message)
    EditText mEditMessage;
    @BindView(R.id.iv_message_send)
    ImageView mMessageSend;
    @BindView(R.id.iv_message_clear)
    ImageView mMessageClear;

    private static List<ChatMessage> mChatMessages;
    private static ChatRecyclerViewAdapter mRVAdapter;
    public static TabChatFragment newInstance( ) {
        Bundle args = new Bundle();
        TabChatFragment fragment = new TabChatFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected TabChatPresenter createPresenter() {
        return new TabChatPresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        mChatMessages = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContainerActivity());
        mMessageContent.setLayoutManager(layoutManager);
        mRVAdapter = new ChatRecyclerViewAdapter(mChatMessages);

        mRVAdapter.setOnItemLongClickListener(new ChatRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TabChatFragment.this.getContainerActivity())
                        .setTitle("撤回消息？")
                        .setCancelable(false)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mChatMessages.remove(position);
                                mRVAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", null);
                builder.show();

            }
        });
        mMessageContent.setAdapter(mRVAdapter);
    }
    @OnClick({R.id.iv_message_send, R.id.iv_message_clear})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_message_clear:
                mChatMessages.clear();
                break;
            case R.id.iv_message_send:
                String chatContent = mEditMessage.getText().toString();
                if(chatContent == null || chatContent.equals("")){

                    AppToast.toastShow(TabChatFragment.this.getContainerActivity(),"没有内容");
                    return;
                }
                int type = new Random().nextInt(10) % 10;
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setContent(chatContent);
                chatMessage.setType(type);
                chatMessage.setAvatar(AppCache.getAvatar(TabChatFragment.this.getContainerActivity()));
                mChatMessages.add(chatMessage);
                mEditMessage.setText("");
                break;
            default:
                break;
        }
        mRVAdapter.notifyDataSetChanged();
    }
}
