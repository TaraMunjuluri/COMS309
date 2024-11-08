package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ChatActivity extends AppCompatActivity {

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private WebSocketClient webSocketClient;
    private String username;
    private static final String SERVER_URL = "ws://coms-3090-033.class.las.iastate.edu:8080/chat/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

//        if (username != null) {
//            initializeWebSocket();
//        }


        // Initialize UI elements
        sendBtn = findViewById(R.id.sendBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);



        // Initialize WebSocket
        initializeWebSocket();

        // Send message button listener
        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString();
            if (webSocketClient != null && webSocketClient.isOpen()) {
                webSocketClient.send(message);
            }
        });
    }

    private void initializeWebSocket() {
        URI uri;
        try {
            // Connect to the WebSocket with the username
            uri = new URI(SERVER_URL + username);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                runOnUiThread(() -> msgTv.append("Connected to the chat\n"));
            }

            @Override
            public void onMessage(String message) {
                runOnUiThread(() -> msgTv.append(message + "\n"));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                runOnUiThread(() -> msgTv.append("Connection closed: " + reason + "\n"));
            }

            @Override
            public void onError(Exception ex) {
                Log.e("WebSocketError", ex.getMessage());
            }
        };

        // Connect the WebSocket client
        webSocketClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}