package com.example.sahil.androidpersonalassistant;
import android.app.Service;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import java.io.File;

/*
 * Created by dpkch on 4/11/2017.
 */

public class CollectData extends Service {
    String username, password;
    SQLiteDatabase db;
    public String TABLE = "personalization";
    public static final String DATABASE_NAME = "group13";
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "Mydata";
    public static final String DATABASE_LOCATION = FILE_PATH + File.separator + DATABASE_NAME;
    Thread addThread=null;
    Handler mHandler=new Handler();
    public CollectData() {

    }
    @Override
    public void onCreate(){


        createDB();
        addThread= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        /*mHandler.post(new Runnable(){
                            public void run(){
                              Toast.makeText(CollectData.this, "test", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                        addDataToDB();
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        addThread.start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent intent, int flags, int startId) {
        //Bundle bundle = intent.getExtras();
        //username = bundle.getString("username");
        return START_STICKY;
    }
   public void addDataToDB(){
       try {
           //perform your database operations here ...
           db.beginTransaction();
           db.execSQL( "insert into tbl_"+ TABLE+"( time, location) values ("+"'12:12:05'"+", "+"'LOCATION_A'"+" );" );
           //db.setTransactionSuccessful(); //commit your changes
           db.endTransaction(); // end the transaction
       }
       catch (SQLiteException e) { // can't toast from a service
           final SQLiteException ee=e;
           mHandler.post(new Runnable(){
                public void run(){
                     Toast.makeText(CollectData.this, ee.getMessage(), Toast.LENGTH_LONG).show();
                }
           });
       }
   }

    public void createDB(){
        try{
            File file = new File(DATABASE_LOCATION);
            File folder = new File(FILE_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION, null);
            db.beginTransaction();
            try {
                //perform your database operations here ...
                db.execSQL("create table if not exists tbl_"+TABLE+" ("
                        + " time TEXT, "
                        + " location TEXT); " );

                db.setTransactionSuccessful(); //commit your changes
                db.endTransaction();// end transaction
            }
            catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }catch (SQLException e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

