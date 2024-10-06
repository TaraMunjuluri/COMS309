package com.example.androidexample;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FindMenteeActivity extends AppCompatActivity {

    private EditText etMajor;
    private Spinner spinnerClassification, spinnerMentorArea;
    private Button btnSubmitForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findmentee);

        // Initialize UI components
        etMajor = findViewById(R.id.et_major);
        spinnerClassification = findViewById(R.id.spinner_classification);
        spinnerMentorArea = findViewById(R.id.spinner_mentor_area);
        btnSubmitForm = findViewById(R.id.btn_submit_form);

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
                String major = etMajor.getText().toString();
                String classification = spinnerClassification.getSelectedItem().toString();
                String mentorArea = spinnerMentorArea.getSelectedItem().toString();

                // TODO: Handle the form data, like saving it to the database or passing it to another activity

                Toast.makeText(FindMenteeActivity.this, "Form Submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

