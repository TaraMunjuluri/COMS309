package com.example.androidexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private ChipGroup chipGroupInterests;
    private Button btnSubmitForm;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriend);

        // Initialize Views
        chipGroupInterests = findViewById(R.id.chip_group_interests);
        btnSubmitForm = findViewById(R.id.btn_submit);

        // Initialize Volley RequestQueue
        mQueue = Volley.newRequestQueue(this);

        // Submit Button Click Listener
        btnSubmitForm.setOnClickListener(view -> {
            List<String> interests = getSelectedInterests();
            if (interests.isEmpty()) {
                Toast.makeText(FriendActivity.this, "Please select at least one interest", Toast.LENGTH_SHORT).show();
                return;
            }
            sendFormData(interests);
        });
    }

    private List<String> getSelectedInterests() {
        List<String> selectedInterests = new ArrayList<>();
        for (int i = 0; i < chipGroupInterests.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupInterests.getChildAt(i);
            if (chip.isChecked()) {
                selectedInterests.add(chip.getText().toString());
            }
        }
        return selectedInterests;
    }

    private void sendFormData(List<String> interests) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String fullSessionId = sharedPreferences.getString("sessionId", null);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(FriendActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String sessionId;
        if (fullSessionId != null) {
            sessionId = fullSessionId.split(";")[0];
        } else {
            Toast.makeText(FriendActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray jsonArray = new JSONArray(interests);
        Log.d("Debug", "Payload: " + jsonArray.toString());

        String url = "http://10.90.74.238:8080/api/interests/users/" + userId + "/interests";
        Log.d("Debug", "Request URL: " + url);

        // Use StringRequest instead of JsonArrayRequest
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("Debug", "Response received: " + response);
                    Toast.makeText(FriendActivity.this, "Interests Submitted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String responseBody = new String(error.networkResponse.data);
                        Log.e("Error", "Status Code: " + statusCode + ", Response: " + responseBody);
                    } else {
                        Log.e("Error", "Request failed without network response", error);
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                // Send the JSON array as the body
                return jsonArray.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", sessionId);
                headers.put("Content-Type", "application/json");
                Log.d("Debug", "Headers set: " + headers.toString());
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, // Timeout in ms
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Log.d("Debug", "Adding request to queue...");
        mQueue.add(request);
        Log.d("Debug", "Request added to queue.");
    }



}
