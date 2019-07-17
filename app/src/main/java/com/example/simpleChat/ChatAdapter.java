package com.example.simpleChat;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restauranteur.R;
import com.example.restauranteur.Visit;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
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
        Visit visit = (Visit) message.getVisit();
        String author = (message.getAuthor()).getObjectId();
        final boolean isMe = (this.mUserId == author);

        if (isMe) {
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.body.setBackgroundColor(0xADD8E6);
            Log.i("MY", "MESSAGE");
        } else {
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        holder.body.setText(message.getBody());
        /*
        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
        Glide.with(mContext).load(getProfileUrl(getCurrentUser().getObjectId())).into(profileView);
        */
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.tvBody);
        }
    }

}