package com.chatbot_ute;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    Context context;
    ArrayList<MessageClass> messageList;
    TextView tvMessage, tvTime;

    public MessageAdapter(Context context, ArrayList<MessageClass> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_message_box, viewGroup, false);
            return new MessageViewHolder(view);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bot_message_box, viewGroup, false);
            return new MessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int i) {
        MessageClass msg = messageList.get(i);
        if (msg.isbot ){
            holder.tv_message.setText(msg.message);

        }else {
            holder.tv_message.setText(msg.message);
        }
        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        holder.tv_time.setText(time);
    }

    @Override
    public int getItemCount() {
         return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        MessageClass msg = messageList.get(position);
        if (msg.isbot){
            return 1;
        }else{
            return 0;
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView tv_message, tv_time;
        public MessageViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

}
