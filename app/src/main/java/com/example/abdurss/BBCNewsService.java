package com.example.abdurss;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class BBCNewsService extends Service {

    private Timer timer;

    @Override
    public void onCreate() {
        // Code that's executed once when the service is created.
        Log.d("Service", "Service Created");
        sendNotification("Service has started");
        startTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId) {
        // Code that's executed each time another component
        // starts the service by calling the startService method.
        Log.d("Service", "Service Started");

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // Code that's executed once when the service
        // is no longer in use and is being destroyed.
        Log.d("Service", "Service Destroyed!");
        stopTimer();
    }

    private void startTimer() {
        // create task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d("News reader", "Timer task executed");
            }
        };

        // create and start timer
        timer = new Timer(true);
        int delay = 1000 * 10;      // 10 seconds
        int interval = 1000 * 10;   // 10 seconds
        timer.schedule(task, delay, interval);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void sendNotification(String text )
    {

        // create the intent for the notification
        Intent notificationIntent = new Intent(this, ItemsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // create the pending intent
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, flags);

        // create the variables for the notification
        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = "News update available!";
        CharSequence contentTitle = getText(R.string.app_name);
        CharSequence contentText = text;


        NotificationChannel notificationChannel =
                new NotificationChannel("Channel_ID", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);


        // create the notification and set its data
        Notification notification = new NotificationCompat
                .Builder(this, "Channel_ID")
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId("Channel_ID")
                .build();

        final int NOTIFICATION_ID = 1;
        manager.notify(NOTIFICATION_ID, notification);
    }
}



