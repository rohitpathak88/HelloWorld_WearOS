package com.example.helloworld;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


public class MainActivity extends WearableActivity implements HttpRestCallback {

    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        mTextView.setText("Hello World");
        mButton = findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testAPI();
            }
        });

        // Enables Always-on
        setAmbientEnabled();

    }

    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    //checks to see if we are online
    protected static boolean isOnline(Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean connected = false;
        if((networkInfo != null) && (networkInfo.isConnected())){
            connected = true;
        }

        return connected;
    }

    public void testAPI(){

        if(isOnline(this)) {

            Date now = new Date();

            String url = "http://yin2.schwarzsoftware.com.au/cgi-bin/hello_world.py";

            List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);

            namevaluepair.add(new BasicNameValuePair("timestamp", getISO8601StringForDate(now)));

            HttpPostAsyncTask task = new HttpPostAsyncTask(namevaluepair, RequestType.REQUEST_TYPE_1, this);

            task.execute(url);

        }else{
            //log error
            mTextView.setText("Check Network Connection");
        }
    }

    public void updateUI(String message, Boolean success){


        mTextView.setText(message);

        if (!success){
            mTextView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.dark_red));
        }

    }

    @Override
    public void completionHandler(Boolean success, RequestType type, final JSONObject object) {
        switch (type) {
            case REQUEST_TYPE_1:
                // Do UI updates ON THE UI THREAD needed for response to REQUEST_TYPE_1 using the object that sent here
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        // UI updates here
                        try{

                            String msg = object.getString("message");
                            boolean success = object.getBoolean("success");

                            updateUI(msg,success);

                        } catch (Throwable t) {

                            Log.e("My App", "Could not parse malformed JSON: \"" + object + "\"");

                        }
                    }
                });

                break;
            case REQUEST_TYPE_2:
                // Do something
                break;
            default: break;
        }
    }
}

