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
            String menteeUsername = menteeUsernameInput.getText().toString().trim();
            String mentorUsername = mentorUsernameInput.getText().toString().trim();
            String ratingStr = ratingInput.getText().toString().trim();

            if (!menteeUsername.isEmpty() && !mentorUsername.isEmpty() && !ratingStr.isEmpty()) {
                try {
                    int rating = Integer.parseInt(ratingStr);
                    if (rating >= 1 && rating <= 5) {
                        submitRating(menteeUsername, mentorUsername, rating);
                    } else {
                        Toast.makeText(this, "Rating must be between 1 and 5.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid number for the rating.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitRating(String menteeUsername, String mentorUsername, int rating) {
        String url = "http://coms-3090-033.class.las.iastate.edu:8080/mentee-ratings/rate" +
                "?menteeUsername=" + menteeUsername +
                "&mentorUsername=" + mentorUsername +
                "&rating=" + rating;

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a StringRequest to submit the rating
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("SubmitRatingSuccess", "Response: " + response);
                    Toast.makeText(this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e("SubmitRatingError", "Error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("SubmitRatingError", "HTTP Status Code: " + error.networkResponse.statusCode);
                    }
                    Toast.makeText(this, "Failed to submit rating. Please check your input or try again later.", Toast.LENGTH_SHORT).show();
                });

        // Add the request to the queue
        requestQueue.add(stringRequest);
    }
}
