package com.example.androidexample;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LookingFor extends AppCompatActivity implements View.OnClickListener{

    private Button friendsBtn, studyGrpBtn, mentorBtn, menteeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookingfor);

        friendsBtn = findViewById(R.id.btnFriendRequest);
        studyGrpBtn = findViewById(R.id.btnStudyGrpRequest);
        mentorBtn = findViewById(R.id.btnMentorRequest);
        menteeBtn = findViewById(R.id.btnMenteeRequest);

        /* button click listeners */
        friendsBtn.setOnClickListener(this);
        studyGrpBtn.setOnClickListener(this);
        mentorBtn.setOnClickListener(this);
        menteeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnFriendRequest) {
            startActivity(new Intent(LookingFor.this, FriendActivity.class));
        } else if (id == R.id.btnStudyGrpRequest) {
            startActivity(new Intent(LookingFor.this, StudyGrpActivity.class));
        } else if (id == R.id.btnMentorRequest) {
            startActivity(new Intent(LookingFor.this, MentorActivity.class));
        } else if (id == R.id.btnMenteeRequest) {
            startActivity(new Intent(LookingFor.this, MenteeActivity.class));
        }
    }
}