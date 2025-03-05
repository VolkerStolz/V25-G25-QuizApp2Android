package com.example.quizapp2;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos")
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Integer imageResId; //drawable
    private String imageUri; // user added image

    public Photo(String name, Integer imageResId, String imageUri) {
        this.name = name;
        this.imageResId = imageResId;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImageResId() {
        return imageResId;
    }

    public void setImageResId(Integer imageResId) {
        this.imageResId = imageResId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
