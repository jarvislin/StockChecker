package com.jarvis.stockchecker.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Jarvis on 2014/5/13.
 */
public class UpdateTask extends AsyncTask<String, Void, DataHandler> {

    public UpdateTask(Context context){
        mContext = context;
    }

    private Context mContext;
    private ProgressDialog mProgressDialog;


    @Override
    protected DataHandler doInBackground(String... stockNum) {
        // TODO Auto-generated method stub
        //開始更新
        return new DataHandler(stockNum[0], mContext);
    }

    @Override
    protected void onPreExecute(){
        //開啟更新畫面
        mProgressDialog = ProgressDialog.show(mContext, "更新資料", "更新中，請稍候...");
    }

    @Override
    protected void onPostExecute(DataHandler result){
        //關掉更新畫面
        mProgressDialog.dismiss();
        if(mContext instanceof MainActivity)
            ((MainActivity)mContext).update(result);
    }

}
