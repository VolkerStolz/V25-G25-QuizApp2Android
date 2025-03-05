package com.example.quizapp2;

import androidx.lifecycle.LiveData;

import java.util.List;

public class QuizRepo {
    private PhotoDao quizDao;
    private LiveData<List<Photo>> allQuizEntries;
}
