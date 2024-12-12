package com.example.androidexample;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AverageRatingActivity extends AppCompatActivity {

    private TextView averageRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_rating);

        averageRatingTextView = findViewById(R.id.averageRatingTextView);

        // Mock average rating
        double mockAverageRating = 4.2;
        averageRatingTextView.setText("Average Rating: " + mockAverageRating);
    }
}
