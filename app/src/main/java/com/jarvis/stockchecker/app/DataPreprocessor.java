package com.jarvis.stockchecker.app;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 2014/5/9.
 */
public class DataPreprocessor {
    private String mViewState;
    private String mValidation;
    private String mSp_Date;
    private String mStockNumber;
    private List<NameValuePair> mPostArray;
    private final String mUrl = "http://bsr.twse.com.tw/bshtm/bsMenu.aspx";

    public DataPreprocessor(String stockNumber){
        this.mStockNumber = stockNumber;
        initParams();
        initPostArray();
    }

    private void initParams(){
        String rawData = DataFetcher.getHtmlContent(mUrl);
        this.mViewState = DataParser.matchData("<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"(.*)\" />",rawData);
        this.mSp_Date = DataParser.matchData("name=\"sp_Date\" style=\"display: none;\">(\\d+)</span>", rawData);
        this.mValidation = DataParser.matchData("id=\"__EVENTVALIDATION\" value=\"(.*)\" />", rawData);
    }

    private void initPostArray() {
        //Array for post
        mPostArray = new ArrayList<NameValuePair>(2);
        mPostArray.add(new BasicNameValuePair("__EVENTTARGET", ""));
        mPostArray.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        mPostArray.add(new BasicNameValuePair("__VIEWSTATE", mViewState));
        mPostArray.add(new BasicNameValuePair("__EVENTVALIDATION", mValidation));
        mPostArray.add(new BasicNameValuePair("HiddenField_spDate", mSp_Date));
        mPostArray.add(new BasicNameValuePair("HiddenField_page", "PAGE_BS"));
        mPostArray.add(new BasicNameValuePair("txtTASKNO", mStockNumber));
        mPostArray.add(new BasicNameValuePair("hidTASKNO", mStockNumber));
        mPostArray.add(new BasicNameValuePair("btnOK", "%E6%9F%A5%E8%A9%A2"));
   }

    public String getPage(){
        String rawData = DataFetcher.getHtmlContent(mUrl, mPostArray);
        return DataParser.matchData("<span id=\"sp_ListCount\">(\\d{1,3})<",rawData);
    }





}
