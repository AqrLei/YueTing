package com.leilei.guoshujinfu.mylearning.tool;

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

/**
 * Created by AqrLei on 2017/8/3.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private static List<ChatMessage> mMessageListm;
    private OnItemLongClickListener mOnItemLongClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftlayout;
        LinearLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;



        public ViewHolder(View itemView) {

            super(itemView);
            leftlayout = itemView.findViewById(R.id.ly_mesaage_left);
            rightLayout = itemView.findViewById(R.id.ly_mesaage_right);
            leftMessage = itemView.findViewById(R.id.tv_message_left);

            rightMessage = itemView.findViewById(R.id.tv_message_right);


        }

    }

    public ChatRecyclerViewAdapter(List<ChatMessage> msg) {
        mMessageListm = msg;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_items, parent, false);
        return new ViewHolder(v);

    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClickListener(holder.itemView, position);
                    return  true;
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

    @Override
    public int getItemCount() {
        return mMessageListm.size();
    }
   public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
       this.mOnItemLongClickListener = onItemLongClickListener;
   }
   public interface OnItemLongClickListener{
       void onItemLongClickListener(View view, int position);
   }
}
