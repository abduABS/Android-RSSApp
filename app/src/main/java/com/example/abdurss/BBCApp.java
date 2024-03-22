package com.example.abdurss;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;

public class BBCApp extends Application {

    private long feedMillis = -1;

    public void setFeedMillis(long feedMillis) {
        this.feedMillis = feedMillis;
    }

    public long getFeedMillis() {
        return feedMillis;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BBCApp", "App started");

        // start service
        Intent service = new Intent(getApplicationContext(), BBCNewsService.class);
        startService(service);

        Log.d("BBCApp", "Prompting to Start the service...");


    }
}
