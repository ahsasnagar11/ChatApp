package com.example.mytalkapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.mytalkapp.R;

import android.widget.Toast;
import android.R.id;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mytalkapp.adapter.fragmentadapter;
import com.example.mytalkapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("hello,world");
        auth = FirebaseAuth.getInstance();

        binding.viewpager.setAdapter(new fragmentadapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewpager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.setting1) {
            Intent i = new Intent(MainActivity.this , SettingsActivity.class);
            startActivity(i);
        } else if (itemId == R.id.logout1) {
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, signin.class);
            startActivity(intent);
        } else if (itemId == R.id.groupchat){
            Intent intent = new Intent(MainActivity.this, GroupChatActivity.class);
            startActivity(intent);


        }

        return true;
    }
}