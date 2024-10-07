package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LookingFor extends AppCompatActivity implements View.OnClickListener{

    private Button friendsBtn, mentorBtn, menteeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookingfor);

        friendsBtn = findViewById(R.id.btnFriendRequest);
        mentorBtn = findViewById(R.id.btnFindMentorRequest);
        menteeBtn = findViewById(R.id.btnFindMenteeRequest);

        /* button click listeners */
        friendsBtn.setOnClickListener(this);
        mentorBtn.setOnClickListener(this);
        menteeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnFriendRequest) {
            startActivity(new Intent(LookingFor.this, FriendActivity.class));
        } else if (id == R.id.btnFindMentorRequest) {
            startActivity(new Intent(LookingFor.this, FindMentorActivity.class));
        } else if (id == R.id.btnFindMenteeRequest) {
            startActivity(new Intent(LookingFor.this, BeMentorActivity.class));
        }
    }
}