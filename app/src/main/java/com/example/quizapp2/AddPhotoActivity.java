package com.example.quizapp2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

public class AddPhotoActivity extends AppCompatActivity {
    private EditText photoNameEditText;
    private ImageView photoImageView;
    private Button choosePhotoButton, saveButton;
    private Uri selectedImageUri;
    private PhotoViewModel photoViewModel;
    private ActivityResultLauncher<String[]> imagePickerLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        photoNameEditText = findViewById(R.id.editTextPhotoName);
        photoImageView = findViewById(R.id.imageViewPhoto);
        choosePhotoButton = findViewById(R.id.buttonChoosePhoto);
        saveButton = findViewById(R.id.buttonSavePhoto);

        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);

        // Register the launcher using OpenDocument to persist URI permission
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        final int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                        try {
                            getContentResolver().takePersistableUriPermission(uri, flags);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                        selectedImageUri = uri;
                        photoImageView.setImageURI(uri);
                    }
                }
        );

        choosePhotoButton.setOnClickListener(v -> {
            // Launch the document picker for images
            imagePickerLauncher.launch(new String[]{"image/*"});
        });

        saveButton.setOnClickListener(v -> {
            String photoName = photoNameEditText.getText().toString().trim();
            if (photoName.isEmpty() || selectedImageUri == null) {
                Toast.makeText(AddPhotoActivity.this, "Please enter a name and select a photo", Toast.LENGTH_SHORT).show();
                return;
            }
            Photo newPhoto = new Photo(photoName, null, selectedImageUri.toString());
            photoViewModel.insert(newPhoto);
            finish();
        });
    }

}