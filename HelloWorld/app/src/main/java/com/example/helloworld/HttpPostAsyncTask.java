package com.example.helloworld;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;


import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {

    // This is the JSON body of the post
    List<NameValuePair> postData;
    RequestType type;
    HttpRestCallback callback;
    static InputStream inputStream = null;

    // This is a constructor that allows you to pass in the JSON body
    public HttpPostAsyncTask(List<NameValuePair>  postData, RequestType type, HttpRestCallback callback) {
        this.postData = postData;
        this.type = type;
        this.callback = callback;
    }

    // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
    @Override
    protected Void doInBackground(String... params) {


            try {

                // This is getting the url from the string we passed in
                URL url = new URL(params[0]);

                // Making HTTP request

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url.toString());
                httpPost.setEntity(new UrlEncodedFormEntity(postData));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

                String response = convertInputStreamToString(inputStream);

                JSONObject obj = new JSONObject(response);

                switch (type) {
                    case REQUEST_TYPE_1:
                        // Use the response to create the object you need
                        callback.completionHandler(true,type,obj);
                        break;
                    case REQUEST_TYPE_2:
                        // Do something
                        break;
                    default:
                        break;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON");
            }


        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}