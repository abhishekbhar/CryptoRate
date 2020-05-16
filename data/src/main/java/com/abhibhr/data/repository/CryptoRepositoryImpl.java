package com.abhibhr.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.abhibhr.data.entities.CryptoCoinEntity;
import com.abhibhr.data.mapper.CryptoMapper;
import com.abhibhr.data.models.CoinModel;
import com.abhibhr.data.repository.datasource.LocalDataSource;
import com.abhibhr.data.repository.datasource.RemoteDataSource;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CryptoRepositoryImpl implements CryptoRepository {
    private static final String TAG = CryptoRepositoryImpl.class.getSimpleName();
    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    private final RemoteDataSource mRemoteDataSource;
    private final LocalDataSource mLocalDataSource;
    private final CryptoMapper mMapper;

    private MediatorLiveData<List<CoinModel>> mDataMerger = new MediatorLiveData<>();
    private MediatorLiveData<String> mErrorMerger = new MediatorLiveData<>();

    private CryptoRepositoryImpl(RemoteDataSource mRemoteDataSource, LocalDataSource
            mLocalDataSource, CryptoMapper mapper) {
        this.mLocalDataSource = mLocalDataSource;
        this.mRemoteDataSource = mRemoteDataSource;
        mMapper = mapper;

        mDataMerger.addSource(this.mRemoteDataSource.getDataStream(),
                entities -> mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "mDataMerger\tmRemoteDataSource on change Invoked");
                        mLocalDataSource.writeData(entities);
                        List<CoinModel> list = mMapper.mapEntityToModel(entities);
                        mDataMerger.postValue(list);
                    }
                }));

        mErrorMerger.addSource(mRemoteDataSource.getErrorStream(), errStr -> {
            mErrorMerger.setValue(errStr);
            Log.d(TAG, "Network error -> fetching from LocalDataSource");
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    List<CryptoCoinEntity> entities = (mLocalDataSource).getAllCoins();
                    mDataMerger.postValue(mapper.mapEntityToModel(entities));
                }
            });
        });

        mErrorMerger.addSource(mLocalDataSource.getErrorStream(), errorStr -> mErrorMerger.setValue(errorStr));
    }


    public static CryptoRepository create(Context context) {
        final CryptoMapper mapper = new CryptoMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(context, mapper);
        final LocalDataSource localDataSource = new LocalDataSource(context);
        return new CryptoRepositoryImpl(remoteDataSource, localDataSource, mapper);
    }

    @Override
    public void fetchData() {
        mRemoteDataSource.fetch();
    }

    @Override
    public LiveData<List<CoinModel>> getCryptoCoinsData() {
        return mDataMerger;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mErrorMerger;
    }

    @Override
    public LiveData<Double> getTotalMarketCapStream() {
        return Transformations.map(mDataMerger, input -> {
            double totalMarketCap = 0;
            for (int i = 0; i < input.size(); i++) {
                totalMarketCap += input.get(i).marketCap;
            }
            return totalMarketCap;
        });
    }
}
