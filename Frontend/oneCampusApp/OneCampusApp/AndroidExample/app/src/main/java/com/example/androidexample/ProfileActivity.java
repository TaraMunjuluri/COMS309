package com.example.androidexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail;
    private EditText etMajor;
    private Spinner spinnerClassification;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        etMajor = findViewById(R.id.et_major);
        spinnerClassification = findViewById(R.id.spinner_classification);
        btnSaveProfile = findViewById(R.id.btn_save_profile);

        // Load username and email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "N/A");
        String email = sharedPreferences.getString("email", "N/A");

        // Display user information
        tvUsername.setText(username);
        tvEmail.setText(email);

        // Check if major and classification already exist
        String major = sharedPreferences.getString("major", "");
        String classification = sharedPreferences.getString("classification", "");

        etMajor.setText(major);
        // Set the spinner selection based on saved classification (if necessary)

        // Set up classification spinner
        ArrayAdapter<CharSequence> classificationAdapter = ArrayAdapter.createFromResource(this,
                R.array.classification_options, android.R.layout.simple_spinner_item);
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(classificationAdapter);

        // Save profile information
        btnSaveProfile.setOnClickListener(v -> saveProfileData());
    }

    private void saveProfileData() {
        String major = etMajor.getText().toString();
        String classification = spinnerClassification.getSelectedItem().toString();

        if (major.isEmpty()) {
            Toast.makeText(this, "Please enter your major", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save major and classification
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("major", major);
        editor.putString("classification", classification);
        editor.apply();

        Toast.makeText(this, "Profile information saved", Toast.LENGTH_SHORT).show();
        // Optionally redirect to the main activity or dashboard
    }
}
