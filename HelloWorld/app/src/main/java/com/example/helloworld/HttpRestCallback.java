package com.example.helloworld;

import org.json.JSONObject;

public interface HttpRestCallback {
    // This function will be called from inside of your AsyncTask when you are ready to callback to your controllers (like a fragment, for example)
    // The object in the completionHandler will be whatever it is that you need to send to your controllers

    void completionHandler(Boolean success, RequestType type, JSONObject object);
}
