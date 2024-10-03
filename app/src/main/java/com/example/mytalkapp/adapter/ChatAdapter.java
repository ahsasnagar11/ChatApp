package com.example.mytalkapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytalkapp.R;
import com.example.mytalkapp.models.MessagesModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytalkapp.R;
import com.example.mytalkapp.models.MessagesModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ChatAdapter";
    private static final int SENDER_VIEW_TYPE = 1;
    private static final int RECEIVER_VIEW_TYPE = 2;

    private ArrayList<MessagesModel> messagesModels;
    private Context context;

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENDER_VIEW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.layoutsender, parent, false);
            return new SenderViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.layoutreciever, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel message = messagesModels.get(position);
        switch (holder.getItemViewType()) {
            case SENDER_VIEW_TYPE:
                ((SenderViewHolder) holder).bind(message);
                break;
            case RECEIVER_VIEW_TYPE:
                ((ReceiverViewHolder) holder).bind(message);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public void addMessage(MessagesModel message) {
        messagesModels.add(message);
        notifyItemInserted(messagesModels.size() - 1);
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;

        SenderViewHolder(View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.sendertext);
            senderTime = itemView.findViewById(R.id.sendertime);
        }

        void bind(MessagesModel message) {
            senderMsg.setText(message.getMessage());
            if (message.getTimestamp() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                senderTime.setText(dateFormat.format(message.getTimestamp()));
            }
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg, receiverTime;

        ReceiverViewHolder(View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.recievertext);
            receiverTime = itemView.findViewById(R.id.recievertime);
        }

        void bind(MessagesModel message) {
            receiverMsg.setText(message.getMessage());
            if (message.getTimestamp() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                receiverTime.setText(dateFormat.format(message.getTimestamp()));
            }
        }
    }
}

