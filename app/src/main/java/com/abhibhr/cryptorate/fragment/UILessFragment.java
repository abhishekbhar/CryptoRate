package com.abhibhr.cryptorate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.abhibhr.cryptorate.viewModel.CryptoViewModel;


public class UILessFragment extends Fragment {
    private static final String TAG = UILessFragment.class.getSimpleName();
    private CryptoViewModel mViewModel;
    private final Observer<Double> mObserver = totalMarketCap ->
            Log.d(TAG, "onChanged() called with: aDouble = [" + totalMarketCap + "]");
    Context aContext;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saBundle) {
        super.onActivityCreated(saBundle);
        mViewModel = ViewModelProviders.of(this).get(CryptoViewModel.class);
        mViewModel.getTotalMarketCap().observe(getViewLifecycleOwner(), mObserver);

    }
}
