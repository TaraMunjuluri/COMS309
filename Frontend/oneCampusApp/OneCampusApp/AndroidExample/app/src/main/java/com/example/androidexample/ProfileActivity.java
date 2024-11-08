package com.example.androidexample;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.provider.DocumentsContract;

import java.io.File;
import java.io.IOException;

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
    private static final String BACKEND_URL = "http://10.0.2.2:8080/api/images/upload/"; // Update to your backend endpoint

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
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ivProfilePicture.setImageURI(selectedImageUri);
                    uploadImageToBackend();
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
                    uploadImageToBackend();
                }
            });

    private File getFileFromUri(Uri uri) {
        String path = null;
        try {
            // Handle the case where the URI is a document
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                if (uri.getAuthority().equals("com.android.providers.media.documents")) {
                    String[] idArr = documentId.split(":");
                    String type = idArr[0];
                    Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{idArr[1]};
                    path = getDataColumn(contentUri, selection, selectionArgs);
                } else if (uri.getAuthority().equals("com.android.providers.downloads.documents")) {
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                    path = getDataColumn(contentUri, null, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // If content URI, resolve file path
                path = getDataColumn(uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }
        } catch (Exception e) {
            Log.e("ProfileActivity", "Error getting file path from URI", e);
        }

        if (path != null) {
            return new File(path);
        } else {
            Toast.makeText(this, "Failed to retrieve image file", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // Helper function to get data column from content resolver
    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        String column = "_data";
        String[] projection = { column };

        try (Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            Log.e("ProfileActivity", "Error in getDataColumn", e);
        }
        return null;
    }

    private void uploadImageToBackend() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = getFileFromUri(selectedImageUri);
        if (imageFile == null || !imageFile.exists()) {
            Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(), requestBody)
                .build();

        Request request = new Request.Builder()
                .url(BACKEND_URL + "/" + userId)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
                Log.e("ProfileActivity", "Image upload failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show());
                    Log.d("ProfileActivity", "Image upload successful");
                } else {
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
                    Log.e("ProfileActivity", "Server responded with code: " + response.code());
                }
            }
        });
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
