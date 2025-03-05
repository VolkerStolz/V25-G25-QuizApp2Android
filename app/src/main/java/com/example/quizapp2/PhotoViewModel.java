package com.example.quizapp2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepo repository;
    private LiveData<List<Photo>> allPhotos;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        repository = new PhotoRepo(application);
        allPhotos = repository.getAllPhotos();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public void insert(Photo photo) {
        repository.insert(photo);
    }
    public void delete(Photo photo) {
        repository.delete(photo);
    }
}
