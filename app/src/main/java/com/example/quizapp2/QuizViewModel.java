


package com.example.quizapp2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizViewModel extends ViewModel {
    // List of photos for the quiz.
    private MutableLiveData<List<Photo>> quizPhotos = new MutableLiveData<>(new ArrayList<>());


    // Quiz state: current question index and score.
    private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);

    // A list of alternative names (for generating wrong answer options).
    private final List<String> randomWords = new ArrayList<>(Arrays.asList("Ku", "Sau", "Okse", "Katt", "Hund"));

    /**
     * Initialize the quiz with a given list of photos.
     * This can be a merged list from defaults and user-added photos.
     */
    // Getters to allow observers in the Activity to react to data changes.
    public LiveData<List<Photo>> getQuizPhotos() {
        return quizPhotos;
    }

    public LiveData<Integer> getCurrentIndex() {
        return currentIndex;
    }

    public LiveData<Integer> getScore() {
        return score;
    }

    public List<String> getRandomWords() {
        return randomWords;
    }

    /**
     * Initialize the quiz with a given list of photos.
     * If you already have a LiveData source, you can call this method
     * after extracting the List with getValue().
     */
    public void initQuiz(List<Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            // Shuffle photos to randomize order.
            Collections.shuffle(photos);
            quizPhotos.setValue(photos);
            currentIndex.setValue(0);
            score.setValue(0);
        }
    }

    /**
     * Move to the next question.
     */
    public void nextQuestion() {
        List<Photo> photos = quizPhotos.getValue();
        Integer index = currentIndex.getValue();
        if (photos != null && index != null && index < photos.size() - 1) {
            currentIndex.setValue(index + 1);
        }
    }

    /**
     * Increase the quiz score.
     */
    public void incrementScore() {
        Integer currentScore = score.getValue();
        if (currentScore != null) {
            score.setValue(currentScore + 1);
        }
    }
    public QuestionData updateQuestion() {
        List<Photo> photos = quizPhotos.getValue();
        Integer index = currentIndex.getValue();
        if (photos == null || index == null || index >= photos.size()) {
            return null; // Quiz completed.
        }

        Photo currentPhoto = photos.get(index);
        String correctAnswer = currentPhoto.getName();

        // Prepare a list of alternative answers.
        List<String> alternatives = new ArrayList<>(randomWords);
        // Remove the correct answer if it exists among the alternatives.
        alternatives.remove(correctAnswer);
        Collections.shuffle(alternatives);

        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        if (alternatives.size() >= 2) {
            options.add(alternatives.get(0));
            options.add(alternatives.get(1));
        } else {
            options.addAll(alternatives);
        }
        // Shuffle options so that the correct answer is in a random position.
        Collections.shuffle(options);

        return new QuestionData(currentPhoto, options);
    }

    /**
     * Checks whether the selected answer is correct.
     * If correct, increments the score.
     * Returns true if the answer is correct; otherwise, false.
     */
    public boolean checkAnswer(String selectedAnswer) {
        List<Photo> photos = quizPhotos.getValue();
        Integer index = currentIndex.getValue();
        if (photos == null || index == null || index >= photos.size()) {
            return false;
        }

        Photo currentPhoto = photos.get(index);
        String correctAnswer = currentPhoto.getName();
        boolean isCorrect = selectedAnswer.equals(correctAnswer);
        if (isCorrect) {
            incrementScore();
        }
        return isCorrect;
    }

    public static class QuestionData {
        private final Photo photo;
        private final List<String> options;

        public QuestionData(Photo photo, List<String> options) {
            this.photo = photo;
            this.options = options;
        }

        public Photo getPhoto() {
            return photo;
        }

        public List<String> getOptions() {
            return options;
        }
    }
}
