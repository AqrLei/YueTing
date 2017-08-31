package com.leilei.guoshujinfu.mylearning.util.view.recyclerview;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leilei.guoshujinfu.mylearning.R;
import com.leilei.guoshujinfu.mylearning.model.ChatMessage;
import com.leilei.guoshujinfu.mylearning.util.AppLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AqrLei on 2017/8/3.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    /*要处理的数据*/
    private List<ChatMessage> mMessageListm;
    /*自定义的点击接口*/
    private OnItemLongClickListener mOnItemLongClickListener;

    /*静态内部类 继承RecyclerView.ViewHolder*/
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ly_message_left)
        LinearLayout leftlayout;
        @BindView(R.id.ly_message_right)
        LinearLayout rightLayout;
        @BindView(R.id.tv_message_left)
        TextView leftMessage;
        @BindView(R.id.tv_message_right)
        TextView rightMessage;

        /*对view进行初始化*/
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /*构造方法，获取数据*/
    public ChatRecyclerViewAdapter(List<ChatMessage> msg) {
        mMessageListm = msg;
    }

    /*获取布局，返回一个ViewHolder*/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_items, parent, false);
        return new ViewHolder(v);

    }

    /*在onBindViewHolder中进行逻辑处理*/
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClickListener(holder.itemView, position);
                    return true;
                }
            });
        }

        ChatMessage msg = mMessageListm.get(position);
        Drawable drawable = msg.getAvatar();
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        AppLog.logDebug(AppLog.LOG_TAG_TEST, "onBindViewHolder\t" + "content:" + msg.getContent() + "\ttype:" + msg.getType());
        if (msg.getType() >= ChatMessage.TYPE_MESSAGE_FlAG) {

            AppLog.logDebug(AppLog.LOG_TAG_TEST, "type_left");
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftlayout.setVisibility(View.VISIBLE);
            holder.leftMessage.setCompoundDrawablesRelative(drawable, null, null, null);

            holder.leftMessage.setText(": " + msg.getContent());
        } else if (msg.getType() < ChatMessage.TYPE_MESSAGE_FlAG) {

            AppLog.logDebug(AppLog.LOG_TAG_TEST, "type_right");
            holder.leftlayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMessage.setCompoundDrawablesRelative(null, null, drawable, null);
            holder.rightMessage.setText(msg.getContent() + " :");

        }

    }

    /*返回Item的数量*/
    @Override
    public int getItemCount() {
        return mMessageListm.size();
    }

    /*设置监听*/
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    /*监听接口*/
    public interface OnItemLongClickListener {
        /*自定义点击监听回掉方法*/
        void onItemLongClickListener(View view, int position);
    }
}
