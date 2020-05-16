package com.abhibhr.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abhibhr.data.entities.CryptoCoinEntity;

@Database(entities = {CryptoCoinEntity.class}, version = 1)
public abstract class RoomDb extends RoomDatabase {
    private final static String DATABASE_NAME = "market_data";
    private static RoomDb INSTANCE;

    public abstract CoinDao mCoinDao();

    public static RoomDb getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDb.class, DATABASE_NAME).build();

        return INSTANCE;
    }
}
