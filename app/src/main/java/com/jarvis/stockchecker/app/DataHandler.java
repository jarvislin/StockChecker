package com.jarvis.stockchecker.app;

import android.content.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jarvis on 2014/5/9.
 */
public class DataHandler {
    private final String STOCK_URL = "http://bsr.twse.com.tw/bshtm/bsMenu.aspx";
    private Map<String, String> mPostmap = new HashMap<String, String>();
    private HashMap<Integer,StockData> mStockDataMap = new HashMap<Integer, StockData>();
    private String mViewState;
    private String mValidation;
    private String mSp_Date;
    private String mStockNumber;
    private Context mContext;
    private int mDataSize;

    public DataHandler(String stockNumber, Context context){
        mContext = context;
        mStockNumber = stockNumber;
        init();
        fetchStockData();
    }

    private void init(){
        initPostParams();
        initPostMap();
    }

    private void initPostParams(){
        try {
            Document doc = getDocumentByUrl(STOCK_URL);
            mViewState = doc.select("input#__VIEWSTATE").first().attr("value");
            mValidation = doc.select("input#__EVENTVALIDATION").first().attr("value");
            mSp_Date = doc.select("span#sp_Date").first().text();
        }
        catch (Exception ex){
            ex.printStackTrace();
            ErrorMessage.showSiteErrorMessage(mContext);
        }
    }

    private void initPostMap() {
        //Map for post
        mPostmap.put("__EVENTTARGET", "");
        mPostmap.put("__EVENTARGUMENT", "");
        mPostmap.put("__VIEWSTATE", mViewState);
        mPostmap.put("__EVENTVALIDATION", mValidation);
        mPostmap.put("HiddenField_spDate", mSp_Date);
        mPostmap.put("HiddenField_page", "PAGE_BS");
        mPostmap.put("txtTASKNO", mStockNumber);
        mPostmap.put("hidTASKNO", mStockNumber);
        mPostmap.put("btnOK", "%E6%9F%A5%E8%A9%A2");
    }

    private void fetchStockData(){
        Document doc = getDocumentByUrl("http://bsr.twse.com.tw/bshtm/bsContent.aspx?StartNumber=" + mStockNumber + "&FocusIndex=All_" + getPage());
        Elements numbers = doc.select("td.column_value_center");
        Elements stockNumbers = doc.select("td.column_value_left");
        Elements stockTransactions = doc.select("td.column_value_right"); //順序是price, amount of bought, amount of sold...
        mDataSize = numbers.size();

        for (int i = 0; i < stockTransactions.size(); i += 3) {
            addData(Integer.valueOf(numbers.get(i / 3).text()), new StockData(stockNumbers.get(i / 3).text(), stockTransactions.get(i).text(), stockTransactions.get(i + 1).text(), stockTransactions.get(i + 2).text()));
        }
    }

    private void addData(int key, StockData data){
        mStockDataMap.put(key, data);
    }

    private String getPage(){
        return getDocumentByUrlWithPostMap(STOCK_URL, mPostmap).select("span#sp_ListCount").first().text();
    }

    public int getDataSize(){
        return mDataSize;
    }

    private Document getDocumentByUrl(String url){
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorMessage.showSiteErrorMessage(mContext);
            return null;
        }
    }

    private Document getDocumentByUrlWithPostMap(String url, Map<String, String> map){
        try {
            return Jsoup.connect(url).data(map).post();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorMessage.showSiteErrorMessage(mContext);
            return null;
        }
    }

    public HashMap<Integer, StockData> getStockDataMap(){
        return mStockDataMap;
    }

    public StockData getStockData(int key){
        return mStockDataMap.get(key);
    }

}
