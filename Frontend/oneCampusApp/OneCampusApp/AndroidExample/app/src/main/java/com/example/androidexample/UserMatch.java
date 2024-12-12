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

/**
 * UserMatch manages the matchmaking functionality, allowing users to view potential matches,
 * send match requests, and reject match requests. It integrates with WebSocket for real-time updates.
 */
public class UserMatch extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private List<User> users;
    private WebSocket webSocket;
    private static final String SOCKET_URL = "ws://10.90.74.238:8080/matches-websocket/websocket";

    /**
     * Called when the activity is created. Initializes the RecyclerView for potential matches,
     * sets up WebSocket communication, and fetches potential matches from the backend.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the data it most
     *                           recently supplied. Otherwise, it is null.
     */
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
        fetchPotentialMatches(); // Ensure this method is called in onCreate
    }

    /**
     * Initializes the WebSocket connection for real-time communication with the server.
     * Logs connection establishment and handles messages or failures.
     */
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

    /**
     * Sends a match request to the server for the specified user.
     *
     * @param user The user to send the match request to.
     */
    private void sendMatchRequest(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId != -1) {
            String message = "{\"action\": \"match\", \"userId\": " + userId + ", \"matchUserId\": " + user.getUserId() + "}";
            webSocket.send(message);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends a reject request to the server for the specified user.
     *
     * @param user The user to reject.
     */
    private void rejectMatchRequest(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId != -1) {
            String message = "{\"action\": \"reject\", \"userId\": " + userId + ", \"matchUserId\": " + user.getUserId() + "}";
            webSocket.send(message);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the response received via WebSocket. Processes the JSON message and performs
     * appropriate actions based on the message content (e.g., match confirmation).
     *
     * @param text The JSON message received from the WebSocket server.
     */
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

    /**
     * Fetches the list of potential matches for the logged-in user from the backend.
     * Populates the RecyclerView with the received match data.
     */
    private void fetchPotentialMatches() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.90.74.238:8080/api/matches/" + userId;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HTTP Request", "Failed to fetch matches", e);
                runOnUiThread(() -> Toast.makeText(UserMatch.this, "Failed to load matches", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonData = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonData);

                        List<User> matchList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject matchObject = jsonArray.getJSONObject(i);
                            String username = matchObject.getString("username");
                            String classification = matchObject.getString("classification");
                            String major = matchObject.getString("major");
                            int matchUserId = matchObject.optInt("id", -1); // Ensure correct ID parsing

                            matchList.add(new User(username, major, classification, matchUserId));
                        }

                        runOnUiThread(() -> {
                            users.clear();
                            users.addAll(matchList);
                            adapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        Log.e("JSON Parsing", "Error parsing match data", e);
                    }
                } else {
                    Log.e("HTTP Response", "Unsuccessful response or null body");
                }
            }
        });
    }
    private void updateMatchStatus(User user, String action) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.90.74.238:8080/api/matches/" + user.getMatchId() + "/update-status?action=" + action;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(UserMatch.this, "Failed to update match status", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String message = response.body().string();
                    runOnUiThread(() -> Toast.makeText(UserMatch.this, message, Toast.LENGTH_SHORT).show());

                    // Remove rejected matches from the list
                    if (action.equalsIgnoreCase("reject")) {
                        runOnUiThread(() -> {
                            users.remove(user);
                            adapter.notifyDataSetChanged();
                        });
                    }
                }
            }
        });
    }

}
