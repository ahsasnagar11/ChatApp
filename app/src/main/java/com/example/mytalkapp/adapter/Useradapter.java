package com.example.mytalkapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytalkapp.R;
import com.example.mytalkapp.chatdetailactivity;
import com.example.mytalkapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Useradapter extends RecyclerView.Adapter<Useradapter.ViewHolder> {

    private ArrayList<Users> list;
    private Context context;

    public Useradapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = list.get(position);

        // Firebase listener to update profile picture in real-time
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId())
                .child("profilepic").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String profilePicUrl = snapshot.getValue(String.class);
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                            Picasso.get()
                                    .load(profilePicUrl)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(holder.image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any error
                    }
                });

        holder.username.setText(user.getUsername());
        holder.lastmessage.setText(user.getLastmessage());

        // Fetch the last message from Firebase
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + user.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                holder.lastmessage.setText(snapshot1.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any error
                    }
                });

        // Open chat details when an item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, chatdetailactivity.class);
            intent.putExtra("userId", user.getUserId());
            intent.putExtra("profilepic", user.getProfilepic());
            intent.putExtra("username", user.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView username, lastmessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username1);
            lastmessage = itemView.findViewById(R.id.lastmessage);
        }
    }
}
