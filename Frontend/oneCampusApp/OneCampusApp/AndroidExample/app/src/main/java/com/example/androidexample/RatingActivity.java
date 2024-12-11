package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * RatingActivity allows users to rate the app and leave a review.
 */
public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Initialize views
        ratingBar = findViewById(R.id.rating_bar);
        reviewInput = findViewById(R.id.review_input);
        submitButton = findViewById(R.id.submit_rating_button);

        // Set listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String review = reviewInput.getText().toString().trim();

                if (rating == 0) {
                    Toast.makeText(RatingActivity.this, "Please select a rating.", Toast.LENGTH_SHORT).show();
                } else if (review.isEmpty()) {
                    Toast.makeText(RatingActivity.this, "Please write a review.", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle rating and review submission
                    Toast.makeText(RatingActivity.this,
                            "Thank you for your feedback!\nRating: " + rating + "\nReview: " + review,
                            Toast.LENGTH_LONG).show();

                    // Optionally reset the form
                    ratingBar.setRating(0);
                    reviewInput.setText("");
                }
            }
        });
    }
}
