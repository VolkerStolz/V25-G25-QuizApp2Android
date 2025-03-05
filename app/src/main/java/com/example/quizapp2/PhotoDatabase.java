package com.example.quizapp2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**standard template*/
@Database(entities = {Photo.class}, version = 2, exportSchema = false)
public abstract class PhotoDatabase extends RoomDatabase {
    public abstract PhotoDao photoDao();

    private static volatile PhotoDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PhotoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhotoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PhotoDatabase.class, "photo_database")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Insert default photos on a background thread
                                    databaseWriteExecutor.execute(() -> {
                                        PhotoDao dao = INSTANCE.photoDao();
                                        // Insert default photos only when the database is created for the first time.
                                        for (Photo defaultPhoto : DefaultPhotos.getDefaultPhotos()) {
                                            dao.insert(defaultPhoto);
                                        }
                                    });
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
