package com.example.javamaps.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.javamaps.view.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM Place")
    Flowable<List<Place>> getAll();

    @Insert
    Completable insert(Place place);

    @Delete
    Completable delete(Place place);
}
