package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LookingFor extends AppCompatActivity implements View.OnClickListener{

    private Button friendsBtn, beMenteeBtn, beMentorBtn, getAllMentorsBtn, profileBtn, nav_match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookingfor);

        friendsBtn = findViewById(R.id.btnFriendRequest);
        beMenteeBtn = findViewById(R.id.btnBeMenteeRequest);
        beMentorBtn = findViewById(R.id.btnBeMentorRequest);
        getAllMentorsBtn = findViewById(R.id.btnGetAllMentors);
        profileBtn = findViewById(R.id.btnProfile);
        nav_match = findViewById(R.id.nav_match);

        /* button click listeners */
        friendsBtn.setOnClickListener(this);
        beMenteeBtn.setOnClickListener(this);
        beMentorBtn.setOnClickListener(this);
        getAllMentorsBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);
        nav_match.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnFriendRequest) {
            startActivity(new Intent(LookingFor.this, FriendActivity.class));
        } else if (id == R.id.btnBeMenteeRequest) {
            startActivity(new Intent(LookingFor.this, BeMenteeActivity.class));
        } else if (id == R.id.btnBeMentorRequest) {
            startActivity(new Intent(LookingFor.this, BeMentorActivity.class));
        } else if (id == R.id.btnGetAllMentors) {
            startActivity(new Intent(LookingFor.this, GetAllMentorsActivity.class));
        }
        else if (id == R.id.btnProfile) {
            startActivity(new Intent(LookingFor.this, ProfileActivity.class));  // Navigate to ProfileActivity
        }else if (id == R.id.nav_match) {
            startActivity(new Intent(LookingFor.this, UserMatch.class));  // Navigate to ProfileActivity
        }

    }
}
