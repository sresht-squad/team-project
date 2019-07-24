package com.example.restauranteur;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Model.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import static android.os.SystemClock.sleep;
import static com.parse.ParseUser.getCurrentUser;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> mMessages;
    private String mUserId;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        String author = (message.getAuthor()).getObjectId();
        final boolean isMe = (this.mUserId.equals(author));

        if (isMe) {
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.body.setBackgroundColor(0xADD8E6);
            Log.i("MY", "MESSAGE");
        } else {
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        holder.body.setText(message.getBody());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView body;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.tvBody);
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