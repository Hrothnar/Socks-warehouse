package com.app.warehouse.sockswarehouse.services;

import com.app.warehouse.sockswarehouse.models.SockJournal;
import com.app.warehouse.sockswarehouse.models.SockPair;

import java.io.InputStream;

public interface SockService {
    boolean addSocks(SockPair newSupply);
    boolean addSocksFromFile(SockJournal newSupply);
    boolean realizeGoods(SockPair newSupply);
    boolean realizeGoodsFromFile(SockJournal newRealise);
    boolean removeDefectiveGoods(SockPair newWriteOff);
    boolean removeDefectiveGoodsFromFile(SockJournal newWriteOff);
    int getQuantityOfSocks(String color, int size, Integer cottonMin, Integer cottonMax);
    void saveToFile();
    boolean addFromFile(InputStream inputStream);
}