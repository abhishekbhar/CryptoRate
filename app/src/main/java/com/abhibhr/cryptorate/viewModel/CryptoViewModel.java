package com.abhibhr.cryptorate.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abhibhr.data.models.CoinModel;
import com.abhibhr.data.repository.CryptoRepository;
import com.abhibhr.data.repository.CryptoRepositoryImpl;

import java.util.List;

public class CryptoViewModel extends AndroidViewModel {

    private static final String TAG = CryptoViewModel.class.getSimpleName();
    private CryptoRepository mCryptoRepository;

    public LiveData<List<CoinModel>> getCoinsMarketData() {
        return mCryptoRepository.getCryptoCoinsData();
    }

    public LiveData<String> getErrorUpdates() {
        return mCryptoRepository.getErrorStream();
    }

    public CryptoViewModel(@NonNull Application application) {
        super(application);
        mCryptoRepository = CryptoRepositoryImpl.create(application);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public void fetchData() {
        mCryptoRepository.fetchData();
    }

    public LiveData<Double> getTotalMarketCap() {
        return mCryptoRepository.getTotalMarketCapStream();
    }


}
