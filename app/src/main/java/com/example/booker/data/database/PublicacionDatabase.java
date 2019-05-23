package com.example.booker.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

// Todas las clases que definen entidades para ROOM, con la version de la BD
@Database(entities = {Publicacion.class}, version = 1, exportSchema = false)
//@TypeConverters(authorsConverter.class)
public abstract class PublicacionDatabase extends RoomDatabase {

    private static volatile PublicacionDatabase sInstance;
    private static final String DATABASE_NAME = "publicacion";

    private static final Object LOCK = new Object();

    public abstract PublicacionDAO publicacionDAO();

    public static PublicacionDatabase getInstance(Context context) {
        if (sInstance == null) synchronized (LOCK) {
            if (sInstance == null)
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PublicacionDatabase.class, PublicacionDatabase.DATABASE_NAME).fallbackToDestructiveMigration().build();
        }

        return sInstance;
    }
}
