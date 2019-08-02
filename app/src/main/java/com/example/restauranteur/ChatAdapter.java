package com.example.restauranteur;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Boolean serverPage;
    private Boolean detailPage;
    private Context context;

    public ChatAdapter(Context context, Boolean serverScreen, Boolean detailScreen, List<Message> messages) {
        mMessages = messages;
        serverPage = serverScreen;
        detailPage = detailScreen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView;
        if (serverPage){
            if (detailPage){
                contactView = inflater.inflate(R.layout.item_chat_server_detail, parent, false);
            }
            else{
                contactView = inflater.inflate(R.layout.item_chat_server, parent, false);
            }
        } else {
            contactView = inflater.inflate(R.layout.item_chat_customer, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        if (serverPage && !detailPage){
            holder.tableNum.setText(message.tableNum);
        }
        holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        holder.body.setText(message.getBody());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView body;
        TextView tableNum;

        ViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.tvBody);
            tableNum = itemView.findViewById(R.id.tvTableNumber);
            if (serverPage) {
                CheckBox checkBox = itemView.findViewById(R.id.checkBox);
                checkBox.setChecked(false);
                checkBox.setOnClickListener(this);

            } else {
                TextView tvCancel;
                tvCancel = itemView.findViewById(R.id.tvCancel);
                tvCancel.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final Message m = mMessages.get(position);
                if (!serverPage) {
                    String foodName = m.getBody();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                    builder.setPositiveButton("Cancel Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            m.setActive(false);
                            m.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    mMessages.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                }
                            });
                        }
                    });
                    builder.setTitle(foodName).setMessage("Would you like to cancel your order for 1  " + foodName + " ?").create().show();
                } else {
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

    // Clean all elements of the recycler
    public void clear() {
        mMessages.clear();
        notifyDataSetChanged();
    }

}