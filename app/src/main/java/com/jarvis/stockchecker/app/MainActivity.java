package com.jarvis.stockchecker.app;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {
    String mViewState ="";
    String mValidation ="";
    String mSp_Date ="";
    String mStockNumber ="1477";
    final String homeUrl = "http://bsr.twse.com.tw/bshtm/bsMenu.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetwork();
        setContentView(R.layout.activity_main);
        setPostParams(getHtmlContent(homeUrl));



        String url = "http://bsr.twse.com.tw/bshtm/bsContent.aspx?StartNumber=" + mStockNumber + "&FocusIndex=All_"+getPage();

        Log.e("HTML", getHtmlContent(url));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getPage() {
// Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://bsr.twse.com.tw/bshtm/bsMenu.aspx");

        try {
            // Add your data
            //you can add all the parameters your php needs in the BasicNameValuePair.
            //The first parameter refers to the name in the php field for example
            // $id=$_POST['id']; the second parameter is the value.
            List<NameValuePair> postArray = new ArrayList<NameValuePair>(2);
            postArray.add(new BasicNameValuePair("__EVENTTARGET", ""));
            postArray.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
            postArray.add(new BasicNameValuePair("__VIEWSTATE", mViewState));
            postArray.add(new BasicNameValuePair("__EVENTVALIDATION", mValidation));
            postArray.add(new BasicNameValuePair("HiddenField_spDate", mSp_Date));
            postArray.add(new BasicNameValuePair("HiddenField_page", "PAGE_BS"));
            postArray.add(new BasicNameValuePair("txtTASKNO", mStockNumber));
            postArray.add(new BasicNameValuePair("hidTASKNO", mStockNumber));
            postArray.add(new BasicNameValuePair("btnOK", "%E6%9F%A5%E8%A9%A2"));
            httppost.setEntity(new UrlEncodedFormEntity(postArray));


            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();

            String page = matchData("<span id=\"sp_ListCount\">(\\d{1,3})<",builder.toString());

            return page;





        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }}

    public String getHtmlContent(String url) {
        String result = "";

        try {
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            inputStream.close();
            result = builder.toString();
        } catch(Exception e) {
            Log.e("log_tag", e.toString());
        }

        return result;
    }

    private void setPostParams(String htmlContent){

        mViewState = matchData("<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\"(.*)\" />",htmlContent);
        mSp_Date = matchData("name=\"sp_Date\" style=\"display: none;\">(\\d+)</span>", htmlContent);
        mValidation = matchData("id=\"__EVENTVALIDATION\" value=\"(.*)\" />", htmlContent);

    }

    private String matchData(String rule, String htmlContent){
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(htmlContent);
        if (m.find())
            return m.group(1).trim();
        else
            return null;
    }

    private void setNetwork(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                .build());
    }
}
