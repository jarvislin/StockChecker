package com.jarvis.stockchecker.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends ActionBarActivity {
    private String mStockNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetwork();
        setContentView(R.layout.activity_main);

        mStockNumber = "1477";

        if(!isNetworkAvailable())
            ErrorMessage.showNetworkErrorMessage(this);

        else {
            DataHandler dataHandler = new DataHandler(mStockNumber, this);
            StockData temp = dataHandler.getStockData(dataHandler.getDataSize());
            Log.e("DATA", temp.getName()+"@"+temp.getPrice()+"@"+temp.getBought()+"@"+temp.getSold());
        }


    }



    private void setNetwork(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                .build());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
