package com.example.quizapp2;

import java.util.ArrayList;
import java.util.List;

/**
 * hvis skal legge til nytt bilde: reinstaler appen*/
public class DefaultPhotos {
    public static List<Photo> getDefaultPhotos() {
        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo("Gorilla", R.drawable.gorilla, null));
        photos.add(new Photo("Wolf", R.drawable.wolf, null));
        photos.add(new Photo("Tiger", R.drawable.tiger, null));
        return photos;
    }
}
