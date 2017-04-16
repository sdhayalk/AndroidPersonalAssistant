package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by SAHIL on 15-04-2017.
 */

/*
 * referred from: https://www.youtube.com/watch?v=1fV9NmvxXJo
 * we modify the code so as to perfrom every XX-Hour:00-MInute:00-Secnond
 */

public class PersonalizationNotificationReceiver extends BroadcastReceiver {

    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "AndroidPersonalAssistant";
    int hourTime;  // only the hour (range from 0 to 23)
    int day;        // 1:Sunday ... 7:Saturday
    String city;    // city corresponding to current location of the user

    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, LoginActivity.class);

        // referred from: http://stackoverflow.com/questions/40167572/hot-to-get-current-hour-in-android
        Calendar calendar = Calendar.getInstance();
        hourTime = calendar.get(Calendar.HOUR_OF_DAY);
        day = calendar.get(Calendar.DAY_OF_WEEK);

        writeToCsv(hourTime, day, "F");






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

    public void writeToCsv(int hourTime, int day, String location)    {
        File folder = new File(FILE_PATH);
        if (!folder.exists())
            folder.mkdir();

        File file = new File(FILE_PATH + File.separator + "LocationData.csv");

        try {
            if(!file.exists())
                file.createNewFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("" + hourTime + "," + day + "," + location + "\n");

            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
