package com.abhibhr.data.mapper;

import com.abhibhr.data.entities.CryptoCoinEntity;
import com.abhibhr.data.models.CoinModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class CryptoMapper extends ObjectMapper {


    private final String CRYPTO_URL_PATH =
            "https://files.coinmarketcap.com/static/img/coins/128x128/%s.png";


    public ArrayList<CryptoCoinEntity> mapJSONToEntity(String jsonStr) {
        ArrayList<CryptoCoinEntity> data = null;

        try {
            data = readValue(jsonStr, new TypeReference<ArrayList<CryptoCoinEntity>>() {
            });
        } catch (Exception e) {

        }
        return data;
    }

    public List<CoinModel> mapEntityToModel(List<CryptoCoinEntity> datum) {
        final ArrayList<CoinModel> listData =
                new ArrayList<>();
        CryptoCoinEntity entity;

        for (int i = 0; i < datum.size(); i++) {
            entity = datum.get(i);
            listData.add(new CoinModel(entity.getName(), entity.getSymbol(),
                    String.format(CRYPTO_URL_PATH, entity.getId()),
                    entity.getPriceUsd(), entity.get_24hVolumeUsd(), Double.valueOf(entity.getMarketCapUsd())));


        }

        return listData;
    }
}
