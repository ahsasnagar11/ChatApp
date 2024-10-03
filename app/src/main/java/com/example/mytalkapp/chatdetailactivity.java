package com.example.mytalkapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mytalkapp.adapter.ChatAdapter;
import com.example.mytalkapp.databinding.ActivityChatdetailactivityBinding;
import com.example.mytalkapp.models.MessagesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatdetailactivity extends AppCompatActivity {
    private static final String TAG = "ChatDetailActivity";
    private ActivityChatdetailactivityBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ChatAdapter chatAdapter;
    private ArrayList<MessagesModel> messagesModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatdetailactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("username");

        // Set the username
        binding.textView00710.setText(userName);

        // Fetch the profile picture from Firebase for the receiver
        fetchProfilePicture(receiverId);

        // Navigate back to MainActivity
        binding.imageView123.setOnClickListener(v -> {
            Intent intent = new Intent(chatdetailactivity.this, MainActivity.class);
            startActivity(intent);
        });

        messagesModels = new ArrayList<>();
        chatAdapter = new ChatAdapter(messagesModels, this);
        binding.chatRecyclerView1.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView1.setLayoutManager(layoutManager);

        String senderRoom = senderId + receiverId;
        String receiverRoom = receiverId + senderId;

        // Fetch chat messages
        database.getReference().child("chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessagesModel model = snapshot1.getValue(MessagesModel.class);
                            messagesModels.add(model);
                        }
                        Log.d(TAG, "Number of messages received: " + messagesModels.size());
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to read value.", error.toException());
                    }
                });

        // Send a message
        binding.send2.setOnClickListener(v -> {
            String messageText = binding.inputtext.getText().toString().trim();
            if (!messageText.isEmpty()) {
                MessagesModel message = new MessagesModel(senderId, messageText, new Date().getTime());
                sendMessage(senderRoom, receiverRoom, message);
            }
        });
    }

    // Fetch profile picture from Firebase
    private void fetchProfilePicture(String userId) {
        database.getReference().child("Users").child(userId).child("profilepic")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profilePicUrl = snapshot.getValue(String.class);
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                            Picasso.get().load(profilePicUrl)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(binding.profileImage);  // CircleImageView in the layout
                        } else {
                            // Set a default image if no profile picture is available
                            binding.profileImage.setImageResource(R.drawable.ic_launcher_background);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to load profile picture.", error.toException());
                    }
                });
    }

    // Send the message to Firebase database
    private void sendMessage(String senderRoom, String receiverRoom, MessagesModel message) {
        database.getReference().child("chats").child(senderRoom).push().setValue(message)
                .addOnSuccessListener(aVoid ->
                        database.getReference().child("chats").child(receiverRoom).push().setValue(message)
                                .addOnSuccessListener(aVoid1 -> binding.inputtext.setText(""))
                );
    }
}
