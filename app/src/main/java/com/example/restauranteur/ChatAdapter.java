package com.example.restauranteur;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.Message;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import static android.os.SystemClock.sleep;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> mMessages;
    private String mUserId;
    Boolean serverPage;

    public ChatAdapter(Context context, Boolean serverScreen, String userId, List<Message> messages) {
        mMessages = messages;
        serverPage = serverScreen;
        this.mUserId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView;
        if (serverPage){
            contactView = inflater.inflate(R.layout.item_chat_server, parent, false);
        } else {
            contactView = inflater.inflate(R.layout.item_chat_customer, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        if (serverPage){
            holder.tableNum.setText(message.tableNum);
        }
        //String author = (message.getAuthor()).getObjectId();
        holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        holder.body.setText(message.getBody());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView body;
        CheckBox checkBox;
        TextView tableNum;

        ViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.tvBody);
            tableNum = itemView.findViewById(R.id.tvTableNum);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Message m = mMessages.get(position);
                Log.i("CLICK", m.getBody());
                m.setActive(false);
                m.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        sleep(200);
                        mMessages.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                    }
                });
            }

        }
    }

}