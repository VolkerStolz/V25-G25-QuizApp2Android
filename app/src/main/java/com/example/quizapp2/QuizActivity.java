package com.example.quizapp2;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuizActivity extends AppCompatActivity {


    private PhotoViewModel photoViewModel;
    private List<Photo> photoList = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    private ImageView photoImageView;
    private Button btnNext, btnAnswer1, btnAnswer2, btnAnswer3;
    private List<Button> answerButtons;
    private TextView scoreTextView;

    private Random random = new Random();

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

        photoImageView = findViewById(R.id.photoImageView);
        btnNext = findViewById(R.id.btnNext);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        scoreTextView = findViewById(R.id.scoreTextView);

        answerButtons = Arrays.asList(btnAnswer1, btnAnswer2, btnAnswer3);

        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);

        photoViewModel.getAllPhotos().observe(this, photos -> {
            photoList.clear();
            photoList.addAll(photos);

            if (photoList.size() >= 3) {
                Integer index = photoViewModel.getCurrentIndex().getValue();
                currentIndex = (index != null) ? index : 0;
                displayPhoto(currentIndex);
                setupAnswerButtons();
            }
        });

        photoViewModel.getCurrentIndex().observe(this, index -> {
            currentIndex = index;
            displayPhoto(currentIndex);
            setupAnswerButtons();
        });

        btnNext.setOnClickListener(v -> {
            if (!photoList.isEmpty()) {
                currentIndex = (currentIndex + 1) % photoList.size();
                photoViewModel.setCurrentIndex(currentIndex);
            }
        });

        for (Button btn : answerButtons) {
            btn.setOnClickListener(this::checkAnswer);
        }

        updateScoreUI();
    }

    private void displayPhoto(int index) {
        if (photoList.isEmpty()) return; // Prevent crash if photoList is empty.

        Photo photo = photoList.get(index);
        if (photo.getImageUri() != null) {
            photoImageView.setImageURI(Uri.parse(photo.getImageUri()));
        } else if (photo.getImageResId() != null) {
            photoImageView.setImageResource(photo.getImageResId());
        } else {
            photoImageView.setImageResource(R.drawable.gorilla);
        }
    }

    private void setupAnswerButtons() {
        if (photoList.size() < 3) return;

        Photo correctPhoto = photoList.get(currentIndex);
        Set<String> answers = new HashSet<>();
        answers.add(correctPhoto.getName());

        while (answers.size() < 3) {
            Photo randomPhoto = photoList.get(random.nextInt(photoList.size()));
            answers.add(randomPhoto.getName());
        }

        List<String> shuffledAnswers = new ArrayList<>(answers);
        Collections.shuffle(shuffledAnswers);

        for (int i = 0; i < answerButtons.size(); i++) {
            answerButtons.get(i).setText(shuffledAnswers.get(i));
        }
    }

    private void checkAnswer(View clickedButton) {
        String chosenAnswer = ((Button) clickedButton).getText().toString();
        String correctAnswer = photoList.get(currentIndex).getName();

        if (chosenAnswer.equals(correctAnswer)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            score++;
            updateScoreUI();
        } else {
            Toast.makeText(this, "Wrong! Correct answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }

    }

    private void updateScoreUI() {
        scoreTextView.setText("Score: " + score);
    }

    // for testing:
    public int getScore() {
        return score;
    }

    public String getCorrectAnswer() {
        return photoList.get(currentIndex).getName();
    }
    // Expose buttons for testing
    public Button getBtnAnswer1() {
        return btnAnswer1;
    }

    public Button getBtnAnswer2() {
        return btnAnswer2;
    }

    public Button getBtnAnswer3() {
        return btnAnswer3;
    }

    public Button getBtnNext() {
        return btnNext;
    }
}