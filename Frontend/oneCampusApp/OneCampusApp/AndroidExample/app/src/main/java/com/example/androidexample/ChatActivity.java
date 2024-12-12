package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * ChatActivity enables users to connect to a WebSocket-based chat server and exchange messages.
 */
public class ChatActivity extends AppCompatActivity {

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private WebSocketClient webSocketClient;
    private String username;
    private static final String SERVER_URL = "ws://coms-3090-033.class.las.iastate.edu:8080/chat/";

    /**
     * Called when the activity is created.
     * Initializes UI elements, retrieves the username from shared preferences, and sets up the WebSocket connection.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState().
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Retrieve username from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        // Initialize UI elements
        sendBtn = findViewById(R.id.sendBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);

        // Initialize WebSocket connection
        initializeWebSocket();

        // Set up a listener for the send button to send messages
        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString();
            if (webSocketClient != null && webSocketClient.isOpen()) {
                webSocketClient.send(message);
            }
        });
    }

    /**
     * Initializes the WebSocket connection using the provided server URL and username.
     * Handles WebSocket events like opening, receiving messages, closing, and errors.
     */
    private void initializeWebSocket() {
        URI uri;
        try {
            // Build the WebSocket URI with the username
            uri = new URI(SERVER_URL + username);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        // Create a new WebSocketClient instance
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                // Handle connection open event
                runOnUiThread(() -> msgTv.append("Connected to the chat\n"));
            }

            @Override
            public void onMessage(String message) {
                // Handle incoming messages
                runOnUiThread(() -> msgTv.append(message + "\n"));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                // Handle connection close event
                runOnUiThread(() -> msgTv.append("Connection closed: " + reason + "\n"));
            }

            @Override
            public void onError(Exception ex) {
                // Log WebSocket errors
                Log.e("WebSocketError", ex.getMessage());
            }
        };

        // Establish the WebSocket connection
        webSocketClient.connect();
    }

    /**
     * Called when the activity is destroyed.
     * Closes the WebSocket connection to release resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}