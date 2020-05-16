package com.abhibhr.data.repository.datasource;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abhibhr.data.db.RoomDb;
import com.abhibhr.data.entities.CryptoCoinEntity;

import java.util.List;

public class LocalDataSource implements DataSource<List<CryptoCoinEntity>> {
    private final RoomDb mRoomDb;
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public LocalDataSource(Context context) {
        mRoomDb = RoomDb.getInstance(context);
    }

    @Override
    public LiveData<List<CryptoCoinEntity>> getDataStream() {
        return mRoomDb.mCoinDao().getAllCoinsLive();
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void writeData(List<CryptoCoinEntity> coins) {
        try {
            mRoomDb.mCoinDao().insertCoins(coins);
        } catch (Exception e) {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }

    public List<CryptoCoinEntity> getAllCoins() {
        return mRoomDb.mCoinDao().getAllCoins();
    }
}


