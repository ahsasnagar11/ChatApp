package com.example.mytalkapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytalkapp.adapter.Useradapter;
import com.example.mytalkapp.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Useradapter userAdapter;  // Assuming you have this initialized properly somewhere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        // Initialize Firebase instances
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Navigate back
        binding.backarrow3.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Image picker intent
        binding.imageView7.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 33);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 33 && resultCode == RESULT_OK && data != null) {
            Uri sFile = data.getData();
            if (sFile != null) {
                binding.profileImage.setImageURI(sFile);

                final StorageReference reference = storage.getReference().child("profile pictures")
                        .child(auth.getUid());
                reference.putFile(sFile).addOnSuccessListener(taskSnapshot ->
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(auth.getUid())
                                        .child("profilepic").setValue(uri.toString()).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                                                // Notify the adapter to reload new data
                                                notifyUserAdapter();
                                            } else {
                                                Toast.makeText(SettingsActivity.this, "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                ).addOnFailureListener(e ->
                        Toast.makeText(SettingsActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            } else {
                Toast.makeText(this, "Failed to get image URI", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void notifyUserAdapter() {
        // Notify the adapter that the profile picture is updated
        if (userAdapter != null) {
            userAdapter.notifyDataSetChanged();  // Refresh the RecyclerView with new data
        }
    }
}
