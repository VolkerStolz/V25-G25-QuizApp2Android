package com.example.quizapp2;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private Button nextButton;

private List<Photo> photos = new ArrayList<>();
private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        imageView = findViewById(R.id.imageView2);
        textView = findViewById(R.id.textView2);
        nextButton = findViewById(R.id.btnNext);

        PhotoViewModel viewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        viewModel.getAllPhotos().observe(this, photoList -> {
            if (photoList != null && !photoList.isEmpty()) {
                photos = photoList;
                currentIndex = 0;  // Start with the first photo
                displayPhoto(currentIndex);
            }
        });

        nextButton.setOnClickListener(v -> {
        if (!photos.isEmpty()) {
            currentIndex++;
            // Loop back to the first photo if at the end
            if (currentIndex >= photos.size()) {
                currentIndex = 0;
            }
            displayPhoto(currentIndex);
        }
    });
}

private void displayPhoto(int index) {
    Photo photo = photos.get(index);
    if (photo.getImageResId() != null) {
        imageView.setImageResource(photo.getImageResId());
    } else if (photo.getImageUri() != null && !photo.getImageUri().isEmpty()) {
        imageView.setImageURI(Uri.parse(photo.getImageUri()));
    }

    textView.setText(photo.getName());
}

}