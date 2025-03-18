package com.example.quizapp2;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizFragment extends Fragment {

    private PhotoViewModel photoViewModel;
    private List<Photo> photoList = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    private ImageView photoImageView;
    private Button btnNext, btnAnswer1, btnAnswer2, btnAnswer3;
    private List<Button> answerButtons;
    private TextView scoreTextView;

    private Random random = new Random();

    public QuizFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        photoImageView = view.findViewById(R.id.photoImageView);
        btnNext = view.findViewById(R.id.btnNext);
        btnAnswer1 = view.findViewById(R.id.btnAnswer1);
        btnAnswer2 = view.findViewById(R.id.btnAnswer2);
        btnAnswer3 = view.findViewById(R.id.btnAnswer3);
        scoreTextView = view.findViewById(R.id.scoreTextView);

        answerButtons = Arrays.asList(btnAnswer1, btnAnswer2, btnAnswer3);

        // Initialize ViewModel
        photoViewModel = new ViewModelProvider(requireActivity()).get(PhotoViewModel.class);

        photoViewModel.getAllPhotos().observe(getViewLifecycleOwner(), photos -> {
            photoList.clear();
            photoList.addAll(photos);

            if (photoList.size() >= 3) {
                Integer index = photoViewModel.getCurrentIndex().getValue();
                currentIndex = (index != null) ? index : 0;
                displayPhoto(currentIndex);
                setupAnswerButtons();
            }
        });

        photoViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), index -> {
            currentIndex = index;
            displayPhoto(currentIndex);
            setupAnswerButtons();
        });

        photoViewModel.getScore().observe(getViewLifecycleOwner(), newScore -> {
            scoreTextView.setText("Score: " + newScore);
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

        return view;
    }

    private void displayPhoto(int index) {
        if (photoList.isEmpty()) return;

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
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            photoViewModel.incrementScore();

        } else {
            Toast.makeText(getContext(), "Wrong! Correct answer: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }
    }



    // For testing
    public int getScore() {
        return score;
    }

    public String getCorrectAnswer() {
        if (!photoList.isEmpty() && currentIndex < photoList.size()) {
            return photoList.get(currentIndex).getName();
        }
        return null;
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