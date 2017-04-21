package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Created by SAHIL on 16-04-2017.
 */

public class PersonalizationService extends Service {
    int hour, day;
    String city="";
    double latitude=0, longitude=0;
    SharedPreferences sharedPreferences;
    SQLiteDatabase db;
    public String TABLE = "Personal";
    public  StringBuilder DATABASE_NAME=new StringBuilder("");
    public  final String FILE_PATH_DB = Environment.getExternalStorageDirectory() + File.separator + "Mydata";
    public  StringBuilder DATABASE_LOCATION = new StringBuilder(FILE_PATH_DB + File.separator) ;
    Handler mHandler=new Handler(); // handler for toasting messages form the service
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
        sharedPreferences = getSharedPreferences("PreferencesToCheckIfLoggedIn", Context.MODE_PRIVATE);
        DATABASE_NAME.append(sharedPreferences.getString("username", null));
        DATABASE_LOCATION.append(DATABASE_NAME.toString());
        Toast.makeText(this, " MyService Created ", Toast.LENGTH_SHORT).show();
        checkForFileAndFolder();
        createDB();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, " MyService Started", Toast.LENGTH_SHORT).show();

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
        writetdb(hour, day, city, latitude, longitude);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Service Stopped!", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        this.stopSelf();
        if(broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    public void checkForFileAndFolder() {
        try {
            File folder = new File(FILE_PATH_DB);
            if (!folder.exists())
                folder.mkdir();

            File file = new File(FILE_PATH_DB + File.separator + "location.csv");
            if (!file.exists())
                file.createNewFile();

            File myFile = new File(FILE_PATH_DB + File.separator + "latitude.csv");
            if(!myFile.exists())
                myFile.createNewFile();

            myFile = new File(FILE_PATH_DB + File.separator + "longitude.csv");
            if(!myFile.exists())
                myFile.createNewFile();

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

    public void createDB(){
        try {
            File folder = new File(FILE_PATH_DB);
            if (!folder.exists())
                folder.mkdir();
        } catch (Exception e)   {e.printStackTrace();}

        try{
            File file = new File(DATABASE_LOCATION.toString());
            File folder = new File(FILE_PATH_DB);
            if (!folder.exists()) {
                folder.mkdir();
            }
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            db.beginTransaction();
            try {
                //perform your database operations here ...
                String create_table="create table if not exists tbl_"+TABLE+" (hour INTEGER, day INTEGER, city TEXT, latitude REAL, longitude REAL);";
                db.execSQL(create_table );
                db.setTransactionSuccessful(); //commit your changes
            }
            catch (SQLiteException e) {
                final SQLiteException ee=e;
                mHandler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(PersonalizationService.this,"ERROR DB Create"+ ee.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            finally {
                db.endTransaction();
                db.close();


            }
        }
        catch (SQLiteException e) { // can't toast from a service
            final SQLiteException ee=e;
            mHandler.post(new Runnable(){
                public void run(){
                    Toast.makeText(PersonalizationService.this, ee.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void writetdb(int hour, int day, String city, double latitude, double longitude) {
        if((latitude == 0 && longitude == 0) || city.equals("")){
            return;
        }
        try {
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            //perform your database operations here ...
            db.beginTransaction();
            db.execSQL( "insert into tbl_"+ TABLE+"(hour,day, city,latitude,longitude) values ("+hour+", "+day+", '"+city+"', "+latitude+", "+longitude+" );" );
            db.setTransactionSuccessful(); //commit your changes

            File myFile = new File(FILE_PATH_DB + File.separator + "location.csv");
            if(!myFile.exists())
                myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write("" + hour + "," + day + "," + city + "\n");
            myOutWriter.close();
            fOut.close();

            if(hour < 23) {
                ConverterUtils.DataSource locationDataset = new ConverterUtils.DataSource(FILE_PATH_DB + File.separator + "location.csv");
                locationDataset.getDataSet(2);
                Instances instances = locationDataset.getDataSet();
                instances.setClassIndex(instances.numAttributes() - 1);

                J48 j48 = new J48();
                j48.buildClassifier(instances);

                weka.core.Instance testInstance = new weka.core.DenseInstance(3);
                testInstance.setValue(0, hour + 1);
                testInstance.setValue(1, day);
                testInstance.setValue(2, -1);
                Instances testInstances = new Instances(instances);
                testInstances.setClassIndex(testInstances.numAttributes() - 1);
                testInstances.delete();
                testInstances.add(testInstance);

                double result = j48.classifyInstance(testInstances.instance(0));
                Log.d("Predicted City = ", instances.classAttribute().value((int) result) + "");

                sendNotification(instances.classAttribute().value((int) result), 1);
            }
        }
        catch (SQLiteException e) { // can't toast from a service
            final SQLiteException ee=e;
            mHandler.post(new Runnable(){
                public void run(){
                    Toast.makeText(PersonalizationService.this,"ERROR DB Insert"+ ee.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e) {
        }
        finally {
            db.endTransaction();
            db.close();
        }
    }

    public void sendNotification(String predictedCity, int code)  {
        Context context = this;
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification);
        String mainMsg="";
        String sideMsg="";

        Intent intent = null;
        if(code == 1) {
            intent = new Intent(this, WeatherForecastActivity.class);
            intent.putExtra("city", predictedCity);
            mainMsg = "Weather in " + predictedCity;
            sideMsg = "Based on your predicted location after some time";
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContent(remoteViews);

        remoteViews.setImageViewResource(R.id.notificationLogo, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.mainMsgTextView, mainMsg);
        remoteViews.setTextViewText(R.id.sideMsgTextView, sideMsg);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
