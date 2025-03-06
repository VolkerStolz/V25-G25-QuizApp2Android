package com.example.quizapp2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PhotoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Setup RecyclerView with an empty adapter.
        recyclerView = findViewById(R.id.recyclerView);


        // Obtain the PhotoViewModel and observe the LiveData.
        PhotoViewModel viewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        viewModel.getAllPhotos().observe(this, photos -> {
            adapter.setPhotos(photos);
            adapter.notifyDataSetChanged();
        });

        adapter = new PhotoAdapter(this, new ArrayList<>(), photo -> {
            //delete when click photo in recycler
            viewModel.delete(photo);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // button to AddPhotoActivity.
        Button addPhotoButton = findViewById(R.id.buttonAddPhoto);
        addPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
            startActivity(intent);
        });

        //button to quiz
        Button btnActivity2 = findViewById(R.id.btnactivity2);
        btnActivity2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }
}