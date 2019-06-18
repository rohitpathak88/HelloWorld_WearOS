package com.example.helloworld;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
                Log.d("My App", "Start....");

                testAPI();
            }
        });

        // Enables Always-on
        setAmbientEnabled();

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

        Log.d("My App", "Start 0....");

        if(isOnline(this)) {

            Log.d("My App", "Start 1....");

            Map<String, String> postData = new HashMap<>();
            postData.put("timestamp", "2019-06-17T15:56:02Z");
            HttpPostAsyncTask task = new HttpPostAsyncTask(postData, RequestType.REQUEST_TYPE_1, this);
            task.execute("http://yin2.schwarzsoftware.com.au/cgi-bin/hello_world.py");
        }else{
            //log error
        }
    }

    @Override
    public void completionHandler(Boolean success, RequestType type, Object object) {
        switch (type) {
            case REQUEST_TYPE_1:
                // Do UI updates ON THE UI THREAD needed for response to REQUEST_TYPE_1 using the object that sent here
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        // UI updates here
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

