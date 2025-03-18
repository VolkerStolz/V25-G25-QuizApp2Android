package com.example.quizapp2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Insert
    void insert(Photo photo);

    @Delete
    void delete(Photo photo);

    @Query("SELECT * FROM photos")
    LiveData<List<Photo>> getAllPhotos();

    @Query("SELECT COUNT(*) FROM photos")
    LiveData<Integer> getPhotoCount();
}
