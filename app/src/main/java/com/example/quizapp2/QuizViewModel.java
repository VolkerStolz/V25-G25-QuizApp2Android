
/**

package com.example.quizapp2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizViewModel extends ViewModel {
    // The list of quiz photos (loaded from Room or defaults)
    private List<Photo> quizPhotos = new ArrayList<>();

    // Quiz state: current question index, score, and number of attempts
    private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> attempts = new MutableLiveData<>(0);

    // List of alternative answer options (for wrong choices)
    private final List<String> randomWords = new ArrayList<>(Arrays.asList("Ku", "Sau", "Okse", "Katt", "Hund"));

    // Initialize the quiz with a list of photos.
    public void initQuiz(List<Photo> photos) {
        this.quizPhotos = photos;
        // Optionally, shuffle the photos to randomize quiz order.
        Collections.shuffle(this.quizPhotos);
        currentIndex.setValue(0);
        score.setValue(0);
        attempts.setValue(0);
    }

    // Getters for LiveData state
    public LiveData<Integer> getCurrentIndex() { return currentIndex; }
    public LiveData<Integer> getScore() { return score; }
    public LiveData<Integer> getAttempts() { return attempts; }
    public List<Photo> getQuizPhotos() { return quizPhotos; }
    public List<String> getRandomWords() { return randomWords; }

    // Get the current Photo based on the currentIndex.
    public Photo getCurrentPhoto() {
        Integer index = currentIndex.getValue();
        if (quizPhotos != null && index != null && index < quizPhotos.size()) {
            return quizPhotos.get(index);
        }
        return null;
    }

    // Update score and attempts based on whether the answer is correct.
    public void updateScore(boolean isCorrect) {
        int currentScore = score.getValue() != null ? score.getValue() : 0;
        int currentAttempts = attempts.getValue() != null ? attempts.getValue() : 0;
        if (isCorrect) {
            score.setValue(currentScore + 1);
        } else {
            // Subtract one point but do not allow negative scores.
            score.setValue(Math.max(0, currentScore - 1));
        }
        attempts.setValue(currentAttempts + 1);
    }

    // Move to the next question; returns true if successful, false if no more questions.
    public boolean nextQuestion() {
        Integer index = currentIndex.getValue();
        if (quizPhotos != null && index != null && index < quizPhotos.size() - 1) {
            currentIndex.setValue(index + 1);
            return true;
        }
        return false;
    }
}
*/