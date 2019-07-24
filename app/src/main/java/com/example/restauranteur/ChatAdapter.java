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

    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(50);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                Visit v;
                if (e == null) {
                    mMessages.clear();
                }
                // mMessages.addAll(messages);
                Message m;
                Server server;
                String serverId;
                String userId;
                int size = messages.size();
                //only show the messages for visits that involve the current logged-in server
                for (int i = 0; i < size; i++) {
                    m = messages.get(i);
                   /* if (m.getActive()) {
                        v = (Visit) m.getVisit();
                        serverId = v.getServer().getObjectId();
                        userId = getCurrentUser().getObjectId();
                        if (serverId.equals(userId)) {
                            //Log.i(serverId, userId);
                            mMessages.add(m);
                        }
                    }
                    */
                }

                notifyDataSetChanged(); // update adapter
            }
        });
    }

}