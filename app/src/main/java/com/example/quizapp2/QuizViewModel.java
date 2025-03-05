/**
package com.example.quizapp2;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
*/
/**
 * Viewmodel gjør at data fra quiz ikke endres når skjerm roteres eller endrer størrelse
 */

/**
public class QuizViewModel extends ViewModel {
    private MutableLiveData<List<Photo>> photoList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    public LiveData<List<Photo>> getPhotoList() {
        if(photoList == null) {
            photoList = new MutableLiveData<>();
        }
        return photoList;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int index) {
        this.currentQuestionIndex = index;
    }

    public int getScore() {
        return score;
    }
    public void updateScore(boolean isCorrect) {
        if (isCorrect) score++;
    }
}
**/