package com.example.sahil.androidpersonalassistant;
import android.app.Service;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;


/**
 * Created by dpkch on 4/11/2017.
 */


public class CollectData extends Service {
    String username, password;
    SQLiteDatabase db;
    //public static final String DATABASE_NAME = "personalization.db";
    //public static final String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "Mydata";
    //public final String FILE_PATH = getFilesDir().toString();
    //public final String DATABASE_LOCATION = FILE_PATH + File.separator + DATABASE_NAME;
    public CollectData() {

    }
    @Override
    public void onCreate(){
        //createDB();
        //addDataToDB();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        return START_STICKY;
    }
   public void addDataToDB(){
       try {
           //perform your database operations here ...
          // db.beginTransaction();
           db.execSQL( "insert into tbl_"+ username+"(param1, param2, param3) values ('"+1+"', '"+2+"', '"+3+"' );" );
           //db.setTransactionSuccessful(); //commit your changes
       }
       catch (SQLiteException e) {
           Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
       }
       finally {
          // db.endTransaction();
       }
   }

   /* public void createDB(){
        try{
            File folder = new File(FILE_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION, null);
            db.beginTransaction();
            try {
                //perform your database operations here ...
                db.execSQL("create table tbl_"+username+" ("
                        + " param1 integer, "
                        + " param2 integer, "
                        + " param3 integer ); " );

                db.setTransactionSuccessful(); //commit your changes
            }
            catch (SQLiteException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                db.endTransaction();
            }
        }catch (SQLException e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }*/
}

