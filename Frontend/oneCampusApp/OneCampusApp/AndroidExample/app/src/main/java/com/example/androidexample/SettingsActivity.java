package com.example.androidexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private EditText newPasswordInput;
    private Spinner appModeSpinner;
    private Button savePasswordBtn, deleteAccountBtn;
    private SharedPreferences sharedPreferences;
    private static final String BASE_URL = "http://coms-3090-033.class.las.iastate.edu:8080";
    private String username;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        sessionId = sharedPreferences.getString("sessionId", "");

        Log.d("SettingsActivity", "Username from SharedPrefs: " + username);
        Log.d("SettingsActivity", "SessionId from SharedPrefs: " + sessionId);

        if (username.isEmpty() || sessionId.isEmpty()) {
            showToast("Please log in first");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        newPasswordInput = findViewById(R.id.newPasswordInput);
        appModeSpinner = findViewById(R.id.appModeSpinner);
        savePasswordBtn = findViewById(R.id.savePasswordBtn);
        deleteAccountBtn = findViewById(R.id.deleteAccountBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.app_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appModeSpinner.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
    }

    private void setupClickListeners() {
        savePasswordBtn.setOnClickListener(v -> updatePassword());
        deleteAccountBtn.setOnClickListener(v -> showDeleteConfirmationDialog());
        appModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMode = parent.getItemAtPosition(position).toString();
                updateAppMode(selectedMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteAccount() {
        String url = BASE_URL + "/delete?username=" + username;

        Log.d("SettingsActivity", "Attempting to delete account for user: " + username);
        Log.d("SettingsActivity", "Delete URL: " + url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Log.d("SettingsActivity", "Delete success response: " + response);
                    showToast("Account deleted successfully");
                    // Clear shared preferences
                    sharedPreferences.edit().clear().apply();
                    // Return to login activity
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    String errorMessage = "Failed to delete account";

                    // Log the complete error details
                    Log.e("SettingsActivity", "Delete error object: " + error);
                    Log.e("SettingsActivity", "Delete error message: " + error.getMessage());

                    if (error.networkResponse != null) {
                        Log.e("SettingsActivity", "Error Status Code: " + error.networkResponse.statusCode);
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            Log.e("SettingsActivity", "Error Response body: " + responseBody);
                            errorMessage += ": " + responseBody;
                        } catch (Exception e) {
                            Log.e("SettingsActivity", "Error parsing error response: " + e.getMessage());
                        }
                    }

                    if (error instanceof AuthFailureError) {
                        errorMessage = "Authentication failed. Please login again.";
                        // Redirect to login
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Request timed out. Please try again.";
                    } else if (error instanceof NetworkError) {
                        errorMessage = "Network error. Please check your connection.";
                    }

                    final String finalErrorMessage = errorMessage;
                    runOnUiThread(() -> showToast(finalErrorMessage));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionId);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,  // 30 seconds timeout
                0,      // no retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Log.d("SettingsActivity", "Sending delete request with sessionId: " + sessionId);
        VolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }



    private void updatePassword() {
        String newPassword = newPasswordInput.getText().toString();
        if (newPassword.isEmpty()) {
            newPasswordInput.setError("Password cannot be empty");
            return;
        }

        String url = BASE_URL + "/updatePassword/" + username;
        StringRequest request = new StringRequest(Request.Method.PUT, url,
                response -> {
                    showToast("Password updated successfully");
                    newPasswordInput.setText("");
                },
                error -> {
                    Log.e("SettingsActivity", "Password update error: " + error.toString());
                    showToast("Failed to update password: " + error.getMessage());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionId);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("newPassword", newPassword);
                    return jsonBody.toString().getBytes();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        VolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    private void updateAppMode(String mode) {
        String url = BASE_URL + "/mode?id=" + username + "&mode=" + mode;
        StringRequest request = new StringRequest(Request.Method.PUT, url,
                response -> showToast("App mode updated successfully"),
                error -> {
                    Log.e("SettingsActivity", "App mode update error: " + error.toString());
                    showToast("Failed to update app mode: " + error.getMessage());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionId);
                return headers;
            }
        };

        VolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}