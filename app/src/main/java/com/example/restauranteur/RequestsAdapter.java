package com.example.restauranteur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Customer.Fragment.CustomerRequestsFragment;
import com.example.restauranteur.Customer.Fragment.MenuAdapter;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Server.Activity.ServerVisitDetailActivity;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Boolean serverPage;
    private Boolean detailPage;
    private Context context;
    private ImageButton ibCheck;
    private TextView tvCheck;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public RequestsAdapter(Context myContext, Boolean serverScreen, Boolean detailScreen, List<Message> messages) {
        mMessages = messages;
        serverPage = serverScreen;
        detailPage = detailScreen;
        context = myContext;
        preferences = PreferenceManager.getDefaultSharedPreferences(myContext);
        editor = preferences.edit();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
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
            ibCheck = ((Activity) context).findViewById(R.id.ibCheck);
            tvCheck = ((Activity) context).findViewById(R.id.tvCheck);
        }

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Message message = mMessages.get(position);
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
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.tvBody);
            tableNum = itemView.findViewById(R.id.tvTableNumber);
            if (serverPage) {
                checkBox = itemView.findViewById(R.id.checkBox);
                checkBox.setOnClickListener(this);
                checkBox.setChecked(false);
            } else {
                final TextView tvCancel = itemView.findViewById(R.id.tvCancel);
                tvCancel.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final Message m = mMessages.get(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                final String foodName = m.getBody();
                if (!serverPage) {
                    builder.setPositiveButton("Cancel Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeMessage(m, position);
                            //if the customer cancels their check, set check button to visible again
                            if (m.getBody().equals("Check")) {
                                ibCheck.setVisibility(View.VISIBLE);
                                tvCheck.setVisibility(View.VISIBLE);
                                editor.putBoolean("showCheckButton", true);
                                // Commit the edits!
                                editor.commit();
                            }
                        }
                    }).setTitle(foodName).setMessage("Would you like to cancel your request?").create().show();
                } else {
                    if (checkBox.isChecked()) {
                        builder.setPositiveButton("Mark Complete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeMessage(m, position);
                                checkBox.setChecked(false);
                            }
                        }).setTitle(foodName).setMessage("Would you like to mark this request as complete?").create().show();
                    }
                }
            }
        }
    };

    private void removeMessage(Message m, final int position){
        m.setActive(false);
        m.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mMessages.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                if (detailPage){
                    if (mMessages.size() == 0){
                        ((ServerVisitDetailActivity)context).setVisibilities();
                    }
                }
            }
        });
    }

}