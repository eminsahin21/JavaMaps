package com.example.javamaps.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.javamaps.view.model.Place;

@Database(entities = {Place.class}, version = 2)  // Version 2 yaptım çünkü schema değişti
public abstract class PlaceDatabase extends RoomDatabase {
    public abstract PlaceDao placeDao();
}
