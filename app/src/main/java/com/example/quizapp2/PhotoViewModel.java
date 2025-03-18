package com.example.quizapp2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepo repository;
    private LiveData<List<Photo>> allPhotos;
    private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
    private final LiveData<Integer> photoCount;
    public PhotoViewModel(@NonNull Application application) {
        super(application);
        repository = new PhotoRepo(application);
        allPhotos = repository.getAllPhotos();
        photoCount = repository.getPhotoCount();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public LiveData<Integer> getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        currentIndex.setValue(index);
    }

    public void insert(Photo photo) {
        repository.insert(photo);
    }
    public void delete(Photo photo) {
        repository.delete(photo);
    }

    public LiveData<Integer> getPhotoCount() {
        return photoCount;
    }
}
