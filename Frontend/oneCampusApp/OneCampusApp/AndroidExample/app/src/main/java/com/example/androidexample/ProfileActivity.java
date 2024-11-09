package com.example.androidexample;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final String BACKEND_URL = "http://10.90.74.238:8080/api/images/upload/";
    private static final String USER_UPDATE_URL = "http://coms-3090-033.class.las.iastate.edu:8080/api/users/update";

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
        int userId = sharedPreferences.getInt("userId", -1);

        tvUsername.setText(username);
        tvEmail.setText(email);

        String major = sharedPreferences.getString("major", "");
        String classification = sharedPreferences.getString("classification", "");
        etMajor.setText(major);

        ArrayAdapter<CharSequence> classificationAdapter = ArrayAdapter.createFromResource(this,
                R.array.classification_options, android.R.layout.simple_spinner_item);
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassification.setAdapter(classificationAdapter);

        // Display profile picture if available
        displayProfilePicture(userId);

        // Open dialog for selecting gallery or camera
        btnUploadPicture.setOnClickListener(v -> openImageSourceDialog());

        // Save profile information
        btnSaveProfile.setOnClickListener(v -> saveProfileData(userId));
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
                    // Call your image upload method here
                    uploadImageToBackend(selectedImageUri);
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

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    ivProfilePicture.setImageURI(selectedImageUri);
                    // Call your image upload method here
                    uploadImageToBackend(selectedImageUri);
                }
            });

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        selectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        cameraLauncher.launch(cameraIntent);
    }

    private void saveProfileData(int userId) {
        // Removed the code for saving major and classification to the backend
        // as you mentioned you don't need that functionality for now

        // Upload the selected image to the backend
        if (selectedImageUri != null) {
            uploadImageToBackend(selectedImageUri);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToBackend(Uri imageUri) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String filePath = createTempFileFromUri(imageUri);
        if (filePath == null) {
            Toast.makeText(this, "Error: Unable to get the real path of the image", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = new File(filePath);
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(),
                        RequestBody.create(MediaType.parse("image/*"), imageFile))
                .build();

        Request request = new Request.Builder()
                .url(BACKEND_URL + userId)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


    private String createTempFileFromUri(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(new File(getCacheDir(), "temp_image"))) {
            if (inputStream == null) return null;

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return new File(getCacheDir(), "temp_image").getAbsolutePath();
        } catch (IOException e) {
            Log.e("ProfileActivity", "Error creating temp file from URI", e);
            return null;
        }
    }


    private void displayProfilePicture(int userId) {
        OkHttpClient client = new OkHttpClient();
        String imageUrl = "http://coms-3090-033.class.las.iastate.edu:8080/api/images/user/" + userId;

        Request request = new Request.Builder().url(imageUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Log.e("ProfileActivity", "Failed to load image: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    runOnUiThread(() -> ivProfilePicture.setImageBitmap(bitmap));
                } else {
                    runOnUiThread(() -> Log.e("ProfileActivity", "Failed to load image"));
                }
            }
        });
    }
}