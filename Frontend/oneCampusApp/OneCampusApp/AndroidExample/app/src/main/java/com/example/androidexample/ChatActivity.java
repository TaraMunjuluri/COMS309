//package com.example.androidexample;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import org.java_websocket.handshake.ServerHandshake;
//
//public class ChatActivity extends AppCompatActivity implements WebSocketListener {
//
//    private Button sendBtn;
//    private EditText msgEtx;
//    private TextView msgTv;
//
//
//    private static final String SERVER_URL = "ws://coms-3090-033.class.las.iastate.edu:8080/chat";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        /* initialize UI elements */
//        sendBtn = (Button) findViewById(R.id.sendBtn);
//        msgEtx = (EditText) findViewById(R.id.msgEdt);
//        msgTv = (TextView) findViewById(R.id.tx1);
//
//        /* connect this activity to the websocket instance */
//        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);
//
//        WebSocketManager.getInstance().connectWebSocket(SERVER_URL);
//
//        /* send button listener */
////        sendBtn.setOnClickListener(v -> {
////            try {
////                // send message
////                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
////            } catch (Exception e) {
////                Log.d("ExceptionSendMessage:", e.getMessage().toString());
////            }
////        });
//        sendBtn.setOnClickListener(v -> {
//            try {
//                String message = msgEtx.getText().toString();
//                if (!message.isEmpty()) {
//                    // Display the message locally
//                    String currentText = msgTv.getText().toString();
//                    msgTv.setText(currentText + "\nYou: " + message);
//
//                    // Send the message through WebSocket
//                    WebSocketManager.getInstance().sendMessage(message);
//
//                    // Clear the input field
//                    msgEtx.setText("");
//                }
//            } catch (Exception e) {
//                Log.d("ExceptionSendMessage:", e.getMessage().toString());
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        WebSocketManager.getInstance().disconnectWebSocket();
//        WebSocketManager.getInstance().removeWebSocketListener();
//    }
//
//    @Override
//    public void onWebSocketOpen(ServerHandshake handshakedata) {
//        runOnUiThread(() -> {
//            Log.d("WebSocket", "Connection established");
//            // Optionally show connection status to user
//            msgTv.setText("Connected to chat server\n");
//        });
//    }
//
//    @Override
//    public void onWebSocketMessage(String message) {
//        /**
//         * In Android, all UI-related operations must be performed on the main UI thread
//         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
//         * is used to post a runnable to the UI thread's message queue, allowing UI updates
//         * to occur safely from a background or non-UI thread.
//         */
//        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "\n" + message);
//        });
//    }
//
//    @Override
//    public void onWebSocketClose(int code, String reason, boolean remote) {
//        String closedBy = remote ? "server" : "local";
//        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
//        });
//    }
//
//    @Override
//    public void onWebSocketError(Exception ex) {
//        runOnUiThread(() -> {
//            Log.e("WebSocket", "Error: " + ex.getMessage());
//            // Optionally show error to user
//            msgTv.setText(msgTv.getText() + "\nError: " + ex.getMessage());
//        });
//    }
//
//}




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
