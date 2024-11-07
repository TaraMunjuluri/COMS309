package com.example.androidexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import okhttp3.Call;
import okhttp3.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserMatch extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private List<User> users;
    private WebSocket webSocket;
    private static final String SOCKET_URL = "ws://localhost:8080/matches-websocket/websocket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_page);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_view);
        users = getPotentialMatches();

        // Initialize the RecyclerView
        adapter = new MatchAdapter(users, new MatchAdapter.OnUserRequestClickListener() {
            @Override
            public void onMatchClicked(User user) {
                sendMatchRequest(user);
            }

            @Override
            public void onRejectClicked(User user) {
                rejectMatchRequest(user);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        initializeWebSocket();
    }

    // Initialize WebSocket
    private void initializeWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SOCKET_URL).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("WebSocket", "Connection established");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("WebSocket", "Message received: " + text);
                handleWebSocketResponse(text);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                Log.e("WebSocket", "Error: " + t.getMessage());
            }
        });
    }

    // Send Match Request
    private void sendMatchRequest(User user) {
        String message = "{\"action\": \"match\", \"userId\": \"" + user.getUserId() + "\"}";
        webSocket.send(message);
    }

    // Send Reject Request
    private void rejectMatchRequest(User user) {
        String message = "{\"action\": \"reject\", \"userId\": \"" + user.getUserId() + "\"}";
        webSocket.send(message);
    }

    // Handle WebSocket Responses
    private void handleWebSocketResponse(String text) {
        runOnUiThread(() -> {
            try {
                JSONObject jsonObject = new JSONObject(text);
                String action = jsonObject.getString("action");

                if ("matchConfirmed".equals(action)) {
                    String matchedUserName = jsonObject.getString("matchedUser");
                    Toast.makeText(this, "You are now matched with " + matchedUserName + "!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private List<User> getPotentialMatches() {
        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return new ArrayList<>();
        }

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.90.74.238:8080/api/matches/" + userId;

        Request request = new Request.Builder().url(url).build();

        List<User> matchList = new ArrayList<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(UserMatch.this, "Failed to load matches", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonData);

                        // Parse each match object
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject matchObject = jsonArray.getJSONObject(i);

                            String username = matchObject.getString("username");
                            String classification = matchObject.getString("classification");
                            String major = matchObject.getString("major");
                            // You might want to set some default or null for userId if it's not part of the response
                            int matchUserId = -1; // You could also set this based on other available data, if possible

                            // Create a new User object for each match
                            User user = new User(username, major, classification, matchUserId);
                            matchList.add(user);
                        }

                        runOnUiThread(() -> {
                            users.clear();
                            users.addAll(matchList);
                            adapter.notifyDataSetChanged();
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return matchList;
    }

}
