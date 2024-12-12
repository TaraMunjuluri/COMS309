package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SubmitRatingActivity extends AppCompatActivity {

    private EditText menteeUsernameInput, mentorUsernameInput, ratingInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_rating);

        menteeUsernameInput = findViewById(R.id.menteeUsernameInput);
        mentorUsernameInput = findViewById(R.id.mentorUsernameInput);
        ratingInput = findViewById(R.id.ratingInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String menteeUsername = menteeUsernameInput.getText().toString();
            String mentorUsername = mentorUsernameInput.getText().toString();
            String rating = ratingInput.getText().toString();

            if (!menteeUsername.isEmpty() && !mentorUsername.isEmpty() && !rating.isEmpty()) {
                submitRating(menteeUsername, mentorUsername, Integer.parseInt(rating));
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitRating(String menteeUsername, String mentorUsername, int rating) {
        String url = "http://coms-3090-033.class.las.iastate.edu:8080/mentee-ratings/rate?menteeUsername"
                + menteeUsername + "&mentorUsername=" + mentorUsername + "&rating=" + rating;

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a StringRequest to submit the rating
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e("SubmitRatingError", error.toString());
                    Toast.makeText(this, "Failed to submit rating.", Toast.LENGTH_SHORT).show();
                });

        // Add the request to the queue
        requestQueue.add(stringRequest);
    }
}
