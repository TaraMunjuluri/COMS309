package com.example.androidexample;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final String BACKEND_URL = "https://your-backend-url.com/upload"; // Replace with your backend endpoint

    private TextView tvUsername, tvEmail;
    private EditText etMajor;
    private Spinner spinnerClassification;
    private Button btnSaveProfile, btnUploadPicture;
    private ImageView ivProfilePicture;
    private Uri selectedImageUri;

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
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        btnUploadPicture = findViewById(R.id.btn_upload_picture);

        // Load username and email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "N/A");
        String email = sharedPreferences.getString("email", "N/A");

        tvUsername.setText(username);
        tvEmail.setText(email);

        String major = sharedPreferences.getString("major", "");
        String classification = sharedPreferences.getString("classification", "");
        etMajor.setText(major);

        ArrayAdapter<CharSequence> classificationAdapter = ArrayAdapter.createFromResource(this,
                R.array.classification_options, android.R.layout.simple_spinner_item);
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(classificationAdapter);

        // Open dialog for selecting gallery or camera
        btnUploadPicture.setOnClickListener(v -> openImageSourceDialog());

        // Save profile information
        btnSaveProfile.setOnClickListener(v -> saveProfileData());
    }

    private void openImageSourceDialog() {
        String[] options = {"Select from Gallery", "Take a Photo"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Upload Profile Picture")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openGallery();
                    } else {
                        checkCameraPermission();
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ivProfilePicture.setImageURI(selectedImageUri);
                    // Here you can save the image locally or keep it in memory for later upload
                    saveImageLocally(selectedImageUri);
                }
            });

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        selectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        cameraLauncher.launch(cameraIntent);
    }

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    ivProfilePicture.setImageURI(selectedImageUri);
                    // Save image locally or keep it in memory for later upload
                    saveImageLocally(selectedImageUri);
                }
            });

    // Placeholder method for saving the image locally
    private void saveImageLocally(Uri uri) {
        // Implement your local storage logic here if needed
        // For now, this method is a placeholder
        Log.d("ProfileActivity", "Image saved locally: " + uri.toString());
    }

    private void uploadImageToBackend(File file) {
        // This method can remain as is or be modified to handle uploading later
        // You can also save the image path in SharedPreferences if needed
        Log.d("ProfileActivity", "Ready to upload image: " + file.getPath());
        // Implement backend upload logic later when ready
    }

    private String getRealPathFromUri(Uri uri) {
        // Implement real path retrieval as needed for later uploads
        return uri.getPath(); // Simple placeholder return for demonstration
    }

    private void saveProfileData() {
        String major = etMajor.getText().toString();
        String classification = spinnerClassification.getSelectedItem().toString();

        if (major.isEmpty()) {
            Toast.makeText(this, "Please enter your major", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("major", major);
        editor.putString("classification", classification);
        editor.apply();

        Toast.makeText(this, "Profile information saved", Toast.LENGTH_SHORT).show();
        Intent profileIntent = new Intent(ProfileActivity.this, HomePage.class);
        startActivity(profileIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
