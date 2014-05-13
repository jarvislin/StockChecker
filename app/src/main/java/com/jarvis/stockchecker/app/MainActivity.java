package com.jarvis.stockchecker.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private Button mButtonSubmit;
    private EditText mInputStockNumer;
    private TableLayout mStockList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetwork();
        setContentView(R.layout.activity_main);
        findViews();

        if(!isNetworkAvailable())
            ErrorMessage.showNetworkErrorMessage(this);
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

    private void findViews(){
        mContext = this;
        mInputStockNumer = (EditText) findViewById(R.id.input_number);
        mButtonSubmit = (Button) findViewById(R.id.submit);
        mButtonSubmit.setOnClickListener(clickSubmit(mInputStockNumer));
        mStockList = (TableLayout) findViewById(R.id.stock_list);
        mStockList.setStretchAllColumns(true);
    }

    private Button.OnClickListener clickSubmit(final EditText edittext){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                UpdateTask updateTask = new UpdateTask(mContext);
                updateTask.execute(edittext.getText().toString());
            }
         };
    }

    public void update(DataHandler dataHandler){
        mStockList.removeAllViews();
        for(int i = 0 ; i <= dataHandler.getDataSize() ; i++){

            StockData tempData = dataHandler.getStockData(i);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.stock_row, null);
            TextView num = (TextView) row.findViewById(R.id.num);
            TextView name = (TextView) row.findViewById(R.id.name);
            TextView price = (TextView) row.findViewById(R.id.price);
            TextView bought = (TextView) row.findViewById(R.id.bought);
            TextView sold = (TextView) row.findViewById(R.id.sold);

            if(i == 0){
                num.setText(R.string.num);
                name.setText(R.string.name);
                price.setText(R.string.price);
                bought.setText(R.string.bought);
                sold.setText(R.string.sold);
                mStockList.addView(row);
                continue;
            }

            num.setText(String.valueOf(i));
            name.setText(tempData.getName());
            price.setText(tempData.getPrice());
            bought.setText(tempData.getBought());
            sold.setText(tempData.getSold());

            mStockList.addView(row);
        }
    }



}
