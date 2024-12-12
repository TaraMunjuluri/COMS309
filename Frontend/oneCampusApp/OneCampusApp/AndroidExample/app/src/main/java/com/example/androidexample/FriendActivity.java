package com.example.androidexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private EditText etMajor;
    private Spinner spinnerClassification;
    private ChipGroup chipGroupInterests;
    private Button btnSubmitForm;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriend);

        // Initialize Views
        etMajor = findViewById(R.id.et_major);
        spinnerClassification = findViewById(R.id.spinner_classification);
        chipGroupInterests = findViewById(R.id.chip_group_interests);
        btnSubmitForm = findViewById(R.id.btn_submit);

        // Initialize Volley RequestQueue
        mQueue = Volley.newRequestQueue(this);

        // Set up Classification Spinner
        ArrayAdapter<CharSequence> classificationAdapter = ArrayAdapter.createFromResource(this,
                R.array.classification_options, android.R.layout.simple_spinner_item);
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(classificationAdapter);

        // Submit Button Click Listener
        btnSubmitForm.setOnClickListener(view -> {
            String major = etMajor.getText().toString();
            String classification = spinnerClassification.getSelectedItem().toString();
            List<String> interests = getSelectedInterests();
            if (interests.isEmpty()) {
                Toast.makeText(FriendActivity.this, "Please select at least one interest", Toast.LENGTH_SHORT).show();
                return;
            }
            sendFormData(interests);
        });
    }

    // Method to get selected interests from the ChipGroup
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
        // Retrieve the session or token and user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String fullSessionId = sharedPreferences.getString("sessionId", null);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(FriendActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String sessionId;
        if (fullSessionId != null) {
            sessionId = fullSessionId.split(";")[0]; // Extract "JSESSIONID=XYZ"
        } else {
            Toast.makeText(FriendActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert interests list to JSONArray
        JSONArray jsonArray = new JSONArray(interests);

        String url = "http://10.90.74.238:8080/api/interests/users/" + userId + "/interests";

        // Create the request
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                url,
                jsonArray,
                response -> {
                    Toast.makeText(FriendActivity.this, "Interests Submitted Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity upon success
                },
                error -> {
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String responseBody = new String(error.networkResponse.data);
                        Log.e("Error", "Status Code: " + statusCode + ", Response: " + responseBody);
                    } else {
                        Log.e("Error", "Request failed without network response");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", sessionId); // Pass the properly formatted session ID
                headers.put("Content-Type", "application/json"); // Specify JSON content type
                return headers;
            }
        };
        mQueue.add(request);
    }
}
