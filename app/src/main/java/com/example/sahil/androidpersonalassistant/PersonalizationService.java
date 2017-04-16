package com.example.sahil.androidpersonalassistant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by SAHIL on 16-04-2017.
 */

public class PersonalizationService extends Service {
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "APA";
    int hour, day;
    String city;
    double latitude=0, longitude=0;
    BroadcastReceiver broadcastReceiver;
    Geocoder geocoder;
    List<Address> addresses;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, " MyService Created ", Toast.LENGTH_SHORT).show();
        checkForFileAndFolder();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, " MyService Started", Toast.LENGTH_SHORT).show();
        Log.d("Started", "started");

        checkForFileAndFolder();

        Calendar currentCalender = Calendar.getInstance();
        hour =  currentCalender.get(Calendar.HOUR_OF_DAY);
        day = currentCalender.get(Calendar.DAY_OF_WEEK);

        //referred from: https://www.youtube.com/watch?v=lvcGh2ZgHeA
        if(broadcastReceiver == null)   {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (double) intent.getExtras().get("latitude");
                    longitude = (double) intent.getExtras().get("longitude");
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("LocationUpdate"));

        city = getCurrentCity(latitude, longitude);
        writetocsv(hour, day, city, latitude, longitude);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Servics Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        if(broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    public void checkForFileAndFolder() {
        try {
            File folder = new File(FILE_PATH);
            if (!folder.exists())
                folder.mkdir();

            File file = new File(FILE_PATH + File.separator + "location.csv");
            if (!file.exists())
                file.createNewFile();
        } catch (Exception e)   {e.printStackTrace();}

    }

    public String getCurrentCity(double latitude, double longitude)  {
        //referred from: http://stackoverflow.com/questions/8119369/how-can-i-get-the-current-city-name-in-android
        String currentLocation = "";
        try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                currentLocation = addresses.get(0).getLocality();
        } catch (Exception e)   {e.printStackTrace();}
        return currentLocation;
    }

    public void writetocsv(int hour, int day, String city, double latitude, double longitude) {
        File file = new File(FILE_PATH + File.separator + "location.csv");
        try {
            if(latitude == 0 && longitude == 0) {}
            else {
                File myFile = new File(FILE_PATH + File.separator + "location.csv");
                FileOutputStream fOut = new FileOutputStream(myFile, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("" + hour + "," + day + "," + city + "," + latitude + "," + longitude + "\n");
                myOutWriter.close();
                fOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
