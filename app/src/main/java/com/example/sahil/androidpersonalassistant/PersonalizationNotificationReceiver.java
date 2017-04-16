package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by SAHIL on 15-04-2017.
 */

/*
 * referred from: https://www.youtube.com/watch?v=1fV9NmvxXJo
 * we modify the code so as to perfrom every XX-Hour:00-MInute:00-Secnond
 */

public class PersonalizationNotificationReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, LoginActivity.class);






//        //should be notified only when future location will change
//        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(android.R.drawable.arrow_up_float)
//                .setContentTitle("Title of notification")
//                .setContentText("Text of notification")
//                .setAutoCancel(true);
//
//        notificationManager.notify(123, builder.build());
    }
}
