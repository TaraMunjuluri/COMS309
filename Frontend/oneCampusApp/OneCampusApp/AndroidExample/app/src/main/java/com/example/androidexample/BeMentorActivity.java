package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class BeMentorActivity extends AppCompatActivity {

    private EditText etMajor;
    private Spinner spinnerClassification, spinnerMentorArea;
    private Button btnSubmitForm;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bementor);

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
//                String major = etMajor.getText().toString();
//                String classification = spinnerClassification.getSelectedItem().toString();
//                String mentorArea = spinnerMentorArea.getSelectedItem().toString();
//
//                // TODO: Handle the form data, like saving it to the database or passing it to another activity
//
//                Toast.makeText(FindMenteeActivity.this, "Form Submitted", Toast.LENGTH_SHORT).show();
                sendFormData();
            }
        });
    }

    // Function to send the form data as JSON to the backend
    private void sendFormData() {

        String major = etMajor.getText().toString();
        String classification = spinnerClassification.getSelectedItem().toString();
        String mentorArea = spinnerMentorArea.getSelectedItem().toString();

        // Create a JSON object with the form data
        JSONObject jsonBody = new JSONObject();
        try {
            // Add user object with the id
            JSONObject userObject = new JSONObject();
            userObject.put("id", 5);  // Replace 4 with the actual user ID if dynamic

            jsonBody.put("user", userObject);
            jsonBody.put("major", major);  // Ensure input matches expected format ("Computer Science")
            jsonBody.put("classification", classification.toUpperCase());  // Convert to uppercase, e.g., "JUNIOR"
            jsonBody.put("areaOfMentorship", mentorArea.toUpperCase());  // Convert to uppercase, e.g., "CAREER"

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String url = "http://coms-3090-033.class.las.lastate.edu:8080/mentor/create";
        String url = "http://10.90.74.238:8080/mentor/create";



        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(BeMentorActivity.this, "Form Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BeMentorActivity.this, "Failed to submit form", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mQueue.add(request);
    }

}

