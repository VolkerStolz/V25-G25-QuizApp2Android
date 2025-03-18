package com.example.quizapp2;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoRepo {

    private PhotoDao photoDao;
    private LiveData<List<Photo>> allPhotos;
    private final LiveData<Integer> photoCount;

    public PhotoRepo(Application application) {
        PhotoDatabase db = PhotoDatabase.getDatabase(application);
        photoDao = db.photoDao();
        allPhotos = photoDao.getAllPhotos();
        photoCount = photoDao.getPhotoCount();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public void insert(final Photo photo) {
        PhotoDatabase.databaseWriteExecutor.execute(() -> {
            photoDao.insert(photo);
        });
    }

    public void delete(final Photo photo) {
        PhotoDatabase.databaseWriteExecutor.execute(() -> {
            photoDao.delete(photo);
        });
    }
    public LiveData<Integer> getPhotoCount() {
        return photoCount;  //Expose count to ViewModel
    }
}
