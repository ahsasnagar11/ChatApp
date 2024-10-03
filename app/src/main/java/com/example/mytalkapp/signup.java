package com.example.mytalkapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mytalkapp.databinding.ActivitySignupBinding;
import com.example.mytalkapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();




        binding.buttonsinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                auth.createUserWithEmailAndPassword(binding.emailget1.getText().toString(), binding.getpass1.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Users user = new Users(binding.textget1.getText().toString(), binding.emailget1.getText().toString(), binding.getpass1.getText().toString());
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("users").child(id).setValue(user);

                            Toast.makeText(signup.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, signin.class);
                startActivity(intent);
            }
        });
    }
}
