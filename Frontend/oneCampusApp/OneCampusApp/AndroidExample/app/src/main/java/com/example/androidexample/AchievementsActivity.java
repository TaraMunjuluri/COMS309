package com.example.androidexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.androidexample.adapter.AchievementAdapter;
import com.example.androidexample.model.Achievement;
//import com.example.androidexample.network.VolleyRequestQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AchievementAdapter achievementAdapter;
    private List<Achievement> achievementList;
    private static final String BASE_URL = "http://coms-3090-033.class.las.iastate.edu:8080";
    private String sessionId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        // Get session and user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        sessionId = sharedPreferences.getString("sessionId", "");
        userId = sharedPreferences.getInt("userId", -1);  // Assuming userId is saved as an integer

        recyclerView = findViewById(R.id.achievementsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        achievementList = new ArrayList<>();
        achievementAdapter = new AchievementAdapter(this, achievementList);
        recyclerView.setAdapter(achievementAdapter);

        fetchAchievements();
    }

    private void fetchAchievements() {
        String url = BASE_URL + "/achievements/users/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    achievementList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject achievementObj = response.getJSONObject(i);
                            String name = achievementObj.getString("name");
                            String description = achievementObj.getString("description");

                            Achievement achievement = new Achievement(name, description);
                            achievementList.add(achievement);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    achievementAdapter.notifyDataSetChanged();
                },
                error -> {
                    Log.e("AchievementsActivity", "Error fetching achievements", error);
                    Toast.makeText(this, "Error fetching achievements", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionId);
                return headers;
            }
        };

        VolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
}
