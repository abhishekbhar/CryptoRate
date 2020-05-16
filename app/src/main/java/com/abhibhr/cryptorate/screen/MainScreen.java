package com.abhibhr.cryptorate.screen;

import com.abhibhr.data.models.CoinModel;

import java.util.List;

public interface MainScreen {
    void updateData(List<CoinModel> data);

    void setError(String msg);
}
