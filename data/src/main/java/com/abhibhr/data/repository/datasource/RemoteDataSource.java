package com.abhibhr.data.repository.datasource;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abhibhr.data.entities.CryptoCoinEntity;
import com.abhibhr.data.mapper.CryptoMapper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class RemoteDataSource implements DataSource<List<CryptoCoinEntity>> {
    private static final String TAG = RemoteDataSource.class.getSimpleName();
    private final String ENDPOINT_FETCH_CRYPTO_DATA = "https://api.coinmarketcap.com/v1/ticker/?limit=100";
    private final RequestQueue mQueue;
    private final CryptoMapper mCryptoMapper;
    private final MutableLiveData<String> mError = new MutableLiveData<>();
    private final MutableLiveData<List<CryptoCoinEntity>> mDataApi =
            new MutableLiveData<>();

    public RemoteDataSource(Context context, CryptoMapper cryptoMapper) {
        mQueue = Volley.newRequestQueue(context);
        mCryptoMapper = cryptoMapper;
    }


    @Override
    public LiveData<List<CryptoCoinEntity>> getDataStream() {
        return mDataApi;
    }


    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void fetch() {
        final JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(ENDPOINT_FETCH_CRYPTO_DATA,
                        response -> {
                            Log.d(TAG, "Thread - > " +
                                    Thread.currentThread().getName() + "\tGot some network Response");
                            final ArrayList<CryptoCoinEntity> data = mCryptoMapper.mapJSONToEntity(response.toString());
                            mDataApi.setValue(data);
                        },
                        error -> {
                            Log.d(TAG, "Thread -> " +
                                    Thread.currentThread().getName() + "\t Got network error");
                            mError.setValue(error.toString());
                        });
        mQueue.add(jsonArrayRequest);
    }

}
