package com.example.androidexample;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

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

            // Here, you would send the collected data to your backend
            // Example:
            // sendDataToBackend(major, classification, selectedInterests);

            // For demonstration, show a toast message
            Toast.makeText(this, "Submitted: " + major + ", " +
                    classification + ", Interests: " + selectedInterests, Toast.LENGTH_LONG).show();
        });
    }

    // Method to handle backend submission (implement your backend logic here)
    private void sendDataToBackend(String major, String classification, List<String> interests) {
        // Implement your backend submission logic here
    }
}
