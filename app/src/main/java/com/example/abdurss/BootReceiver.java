package com.example.abdurss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("TestBootReceiver", "Boot completed");
        Toast.makeText(context, "BroadcastReceiver for news reader!", Toast.LENGTH_SHORT).show();

        // start service
        Intent service = new Intent(context, BBCNewsService.class);
        context.startService(service);

    }
}
