package com.procialize.mrgeApp20.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String newMessage = intent.getExtras().getString("message");

        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());

        if (newMessage != null) {// Start
            // service,
            // keeping the device
            // awake while it is
            // launching.
            startWakefulService(context, (intent.setComponent(comp)));
            //startServiceInForeground (context, (intent.setComponent(comp)));
        }

        setResultCode(Activity.RESULT_OK);
    }
}