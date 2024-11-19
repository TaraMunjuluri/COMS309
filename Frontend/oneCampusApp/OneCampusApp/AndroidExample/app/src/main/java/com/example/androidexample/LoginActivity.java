package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LoginActivity handles the login functionality of the app.
 * It allows the user to log in with a username and password,
 * or navigate to the signup page.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private RequestQueue requestQueue;

    private static final String LOGIN_URL = "http://coms-3090-033.class.las.iastate.edu:8080/login";

    /**
     * Called when the activity is first created.
     * Initializes UI elements and sets up button listeners for login and signup actions.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState().
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);

        requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the login button click event.
             * Retrieves the username and password entered by the user and calls the loginUser method.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the signup button click event.
             * Redirects the user to the SignupActivity.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Logs in the user by sending a POST request to the server with the provided username and password.
     * If the login is successful, it stores the sessionId and userId in SharedPreferences
     * and redirects the user to the LookingFor activity.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    private void loginUser(String username, String password) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    /**
                     * Handles the response from the server upon a successful login request.
                     * Saves sessionId and userId to SharedPreferences and navigates to the LookingFor activity.
                     *
                     * @param response The JSON response from the server.
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract sessionId if it exists
                            String sessionId = response.optString("sessionId", null);

                            // Extract user object and retrieve userId
                            JSONObject userObject = response.getJSONObject("user");
                            int userId = userObject.optInt("id", -1);  // Retrieve userId from user object

                            // Save sessionId and userId to SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (sessionId != null) {
                                editor.putString("sessionId", sessionId);
                            }
                            if (userId != -1) {
                                editor.putInt("userId", userId); // Save userId if available
                            }
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Start LookingFor activity after login success
                            Intent intent = new Intent(LoginActivity.this, LookingFor.class);
                            intent.putExtra("USERNAME", username);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Failed to parse user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * Handles error responses from the server during login.
                     * Displays appropriate error messages to the user.
                     *
                     * @param error The error returned by the server.
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            /**
             * Parses the network response from the server to extract the sessionId, if available.
             * Saves the sessionId to SharedPreferences.
             *
             * @param response The network response from the server.
             * @return The parsed JSON response.
             */
            @Override
            protected Response<JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                String sessionId = response.headers.get("Set-Cookie");

                if (sessionId != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("sessionId", sessionId);
                    editor.apply();
                }

                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
