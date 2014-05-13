package com.jarvis.stockchecker.app;

/**
 * Created by Jarvis on 2014/5/12.
 */
public class StockData {
    private String[] mData = new String[4];
    public StockData(String name, String price, String bought, String sold){
        mData[0] = name;
        mData[1] = price;
        mData[2] = bought;
        mData[3] = sold;
    }
    public String getName(){
        return mData[0];
    }
    public String getPrice(){
        return mData[1];
    }
    public String getBought(){
        return mData[2];
    }
    public String getSold(){
        return mData[3];
    }
}
