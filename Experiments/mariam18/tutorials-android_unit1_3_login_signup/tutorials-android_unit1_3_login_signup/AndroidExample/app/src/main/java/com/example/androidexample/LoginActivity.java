package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private CheckBox rememberMeCheckBox; // define remember me checkbox variable

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";  // name for SharedPreferences file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // link to Login activity XML

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox); // checkbox for Remember Me

        // Check if username is already saved in SharedPreferences
        String savedUsername = sharedPreferences.getString("USERNAME", null);
        if (savedUsername != null) {
            // If a saved username is found, automatically fill it in the EditText
            usernameEditText.setText(savedUsername);
            rememberMeCheckBox.setChecked(true);  // automatically check Remember Me
        }

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get user inputs
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // If Remember Me is checked, save the username in SharedPreferences
                if (rememberMeCheckBox.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME", username);  // save username
                    editor.apply();
                } else {
                    // Clear the saved username if Remember Me is unchecked
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("USERNAME");
                    editor.apply();
                }

                // Pass the username and password to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", username);  // pass username
                intent.putExtra("PASSWORD", password);  // pass password
                startActivity(intent);  // go to MainActivity
            }
        });

        // Set click listener for signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }
}
