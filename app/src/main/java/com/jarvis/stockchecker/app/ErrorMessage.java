package com.jarvis.stockchecker.app;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jarvis on 2014/5/12.
 */
public class ErrorMessage {
    public ErrorMessage(){
    }
    public static void showNetworkErrorMessage(Context context){
        Toast.makeText(context,"網路有問題, 請確認是否連上網路.", Toast.LENGTH_LONG).show();
    }
    public static void showSiteErrorMessage(Context context){
        Toast.makeText(context,"網站有問題, 請稍後再試!", Toast.LENGTH_LONG).show();
    }

}
