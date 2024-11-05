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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private EditText majorEditText;
    private Spinner classificationSpinner;
    private ChipGroup interestsChipGroup;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriend);

        // Initialize Views
        majorEditText = findViewById(R.id.et_major);
        classificationSpinner = findViewById(R.id.spinner_classification);
        interestsChipGroup = findViewById(R.id.chip_group_interests);
        submitButton = findViewById(R.id.btn_submit);

        // Set up Classification Spinner
        List<String> classifications = new ArrayList<>();
        classifications.add("Freshman");
        classifications.add("Sophomore");
        classifications.add("Junior");
        classifications.add("Senior");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, classifications);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classificationSpinner.setAdapter(adapter);

        // Submit button listener
        submitButton.setOnClickListener(view -> {
            String major = majorEditText.getText().toString();
            String classification = classificationSpinner.getSelectedItem().toString();
            List<String> selectedInterests = new ArrayList<>();

            // Get selected chips
            for (int i = 0; i < interestsChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) interestsChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedInterests.add(chip.getText().toString());
                }
            }

            // Send the collected data to backend
            sendFormData(major, classification, selectedInterests);
        });
    }

    private void sendFormData(String major, String classification, List<String> interests) {
        // Retrieve the session or token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String fullSessionId = sharedPreferences.getString("sessionId", null);

        String sessionId;
        if (fullSessionId != null) {
            sessionId = fullSessionId.split(";")[0]; // Extract "JSESSIONID=XYZ"
        } else {
            Toast.makeText(FriendActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a JSON object with the form data
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("major", major);
            jsonBody.put("classification", classification);
            jsonBody.put("interests", interests);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://your-backend-url/submitForm";

        // Create the request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> Toast.makeText(FriendActivity.this, "Form Submitted Successfully", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String responseBody = new String(error.networkResponse.data);
                        Log.e("Error", "Status Code: " + statusCode + ", Response: " + responseBody);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", sessionId);  // Pass the properly formatted session ID
                return headers;
            }
        };

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(request);
    }
}
