package com.example.androidexample;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.HttpHeaderParser;

public class BeMenteeActivity extends AppCompatActivity {

    private EditText etMajor;
    private Spinner spinnerClassification, spinnerMentorArea;
    private Button btnSubmitForm;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bementee);

        // Initialize UI components
        etMajor = findViewById(R.id.et_major);
        spinnerClassification = findViewById(R.id.spinner_classification);
        spinnerMentorArea = findViewById(R.id.spinner_mentor_area);
        btnSubmitForm = findViewById(R.id.btn_submit_form);

        // Initialize the Volley request queue
        mQueue = Volley.newRequestQueue(this);

        // Set up classification spinner
        ArrayAdapter<CharSequence> classificationAdapter = ArrayAdapter.createFromResource(this,
                R.array.classification_options, android.R.layout.simple_spinner_item);
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(classificationAdapter);

        // Set up mentoring area spinner
        ArrayAdapter<CharSequence> mentorAreaAdapter = ArrayAdapter.createFromResource(this,
                R.array.mentor_area_options, android.R.layout.simple_spinner_item);
        mentorAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMentorArea.setAdapter(mentorAreaAdapter);

        // Handle form submission
        btnSubmitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFormData();  // Send data to backend
                Intent intent = new Intent(BeMenteeActivity.this, HomePage.class);
                startActivity(intent);  // Navigate to the HomePage after submission
                finish();  // Close the BeMenteeActivity
            }
        });
    }

    // Function to send the form data as JSON to the backend
    private void sendFormData() {
        // Retrieve the session or token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String fullSessionId = sharedPreferences.getString("sessionId", null); // Fetch the session ID

        String sessionId;
        if (fullSessionId != null) {
            // Extract only the JSESSIONID part before the semicolon
            sessionId = fullSessionId.split(";")[0]; // This extracts "JSESSIONID=D96D5BB22C27B0C04232A9DE8424455B"
        } else {
            Toast.makeText(BeMenteeActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return; // Exit if session ID is null
        }

        String major = etMajor.getText().toString();
        String classification = spinnerClassification.getSelectedItem().toString();
        String mentorArea = spinnerMentorArea.getSelectedItem().toString();

        // Create a JSON object with the form data
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("major", major);
            jsonBody.put("classification", classification.toUpperCase());
            jsonBody.put("areaOfMenteeship", mentorArea.toUpperCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://10.90.74.238:8080/mentee/create";  // URL for Mentee

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(BeMenteeActivity.this, "Form Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseBody = new String(error.networkResponse.data);
                            Log.e("Error", "Status Code: " + statusCode + ", Response: " + responseBody);
                        }
                    }
                }
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Cookie", sessionId);  // Pass the properly formatted session ID
                return headers;
            }
        };

        mQueue.add(request);  // Add request to the request queue
    }
}
