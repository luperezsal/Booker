package com.example.booker.data.database;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface PublicacionDAO {

    @Query("SELECT * FROM publicacion")
    public LiveData<List<Publicacion>> getAll();


    @Query ("SELECT * FROM publicacion where isFavorite = 1")
    public LiveData<List<Publicacion>> getFavorites();

    @Query ("SELECT * FROM publicacion WHERE id = :id")
    public LiveData<Publicacion> getFromId(String id);

    @Query ("SELECT * FROM publicacion WHERE title LIKE :name")
    public LiveData<List<Publicacion>>  getFromName(String name);



    @Query ("SELECT * FROM publicacion WHERE  1 = 0")
    public LiveData<List<Publicacion>>  getNullList();

    @Update
    public int updatePublicacion(Publicacion publicacionActualizada);

    @Query("UPDATE publicacion SET isFavorite = 0")
    public void deleteAllFavorites();

    @Query("DELETE FROM publicacion")
    public void deleteAll();

    // Inserta un numero indefinido de publicaciones
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(Publicacion... publicaciones);
}
