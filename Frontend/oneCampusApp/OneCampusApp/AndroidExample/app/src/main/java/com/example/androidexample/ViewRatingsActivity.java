package com.example.androidexample;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ViewRatingsActivity extends AppCompatActivity {

    private ListView ratingsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);

        ratingsListView = findViewById(R.id.ratingsListView);

        // Mock data for ratings
        List<String> ratings = new ArrayList<>();
        ratings.add("Mentee 1 rated 5 on Mentor 1");
        ratings.add("Mentee 2 rated 4 on Mentor 1");
        ratings.add("Mentee 3 rated 3 on Mentor 2");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, ratings);
        ratingsListView.setAdapter(adapter);
    }
}
