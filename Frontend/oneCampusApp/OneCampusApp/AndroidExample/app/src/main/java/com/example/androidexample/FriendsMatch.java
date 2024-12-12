package com.example.androidexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendsMatch extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private List<User> users;
    private static final String FRIENDS_API_URL = "http://10.90.74.238:8080/api/potential-friends/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_page);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_view);
        users = new ArrayList<>(); // Ensure users is initialized as an empty list

        // Initialize the RecyclerView
        adapter = new MatchAdapter(users, new MatchAdapter.OnUserRequestClickListener() {
            @Override
            public void onMatchClicked(User user) {
                Toast.makeText(FriendsMatch.this, "Friend match clicked for " + user.getName(), Toast.LENGTH_SHORT).show();
                // Implement friend matching logic here if needed
            }

            @Override
            public void onRejectClicked(User user) {
                Toast.makeText(FriendsMatch.this, "Friend reject clicked for " + user.getName(), Toast.LENGTH_SHORT).show();
                // Implement friend rejection logic here if needed
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchPotentialFriends(); // Ensure this method is called in onCreate
    }

    // Fetch potential friends from the backend
    private void fetchPotentialFriends() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        String url = FRIENDS_API_URL + userId;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HTTP Request", "Failed to fetch potential friends", e);
                runOnUiThread(() -> Toast.makeText(FriendsMatch.this, "Failed to load potential friends", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonData = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonData);

                        List<User> friendList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject friendObject = jsonArray.getJSONObject(i);
                            String name = friendObject.getString("name");
                            String major = friendObject.getString("major");
                            String classification = friendObject.getString("classification");
                            int potentialFriendId = friendObject.getInt("potentialFriendId");

                            Set<String> commonInterests = new HashSet<>();
                            JSONArray interestsArray = friendObject.getJSONArray("commonInterests");
                            for (int j = 0; j < interestsArray.length(); j++) {
                                commonInterests.add(interestsArray.getString(j));
                            }

                            // Create a new User object for each potential friend
                            User user = new User(name, major, classification, potentialFriendId);
                            friendList.add(user);
                        }

                        runOnUiThread(() -> {
                            users.clear();
                            users.addAll(friendList);
                            adapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        Log.e("JSON Parsing", "Error parsing potential friend data", e);
                    }
                } else {
                    Log.e("HTTP Response", "Unsuccessful response or null body");
                }
            }
        });
    }
}
