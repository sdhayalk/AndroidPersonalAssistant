package com.example.sahil.androidpersonalassistant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class UserPreferencesActivity extends AppCompatActivity implements LocationListener{
    String username, password;
    Button confirmPreferencesButton;
    SQLiteDatabase db;
    Handler mHandler=new Handler();
    CheckBox clothing_C, footwear_C, accessories_C, books_C, movies_C, music_C, electronics_C, software_C;
    int clothing, footwear, accessories, books, movies, music, electronics, software;
    public String TABLE = "Preferences";
    public  StringBuilder DATABASE_NAME=new StringBuilder("");
    public  final String FILE_PATH_DB = Environment.getExternalStorageDirectory() + File.separator + "Mydata";
    public  StringBuilder DATABASE_LOCATION = new StringBuilder(FILE_PATH_DB + File.separator) ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);
        Bundle bundle = getIntent().getExtras();
        try {
            username = bundle.getString("username");
            password = bundle.getString("password");
        }catch(Exception e) {
            Toast.makeText(this, "Cannot get username and password", Toast.LENGTH_SHORT).show();
        }
        DATABASE_NAME.append(username);
        DATABASE_LOCATION.append(DATABASE_NAME.toString());

        clothing_C=(CheckBox)findViewById(R.id.checkClothing);
        footwear_C=(CheckBox)findViewById(R.id.checkFootwear);
        accessories_C=(CheckBox)findViewById(R.id.checkAccessories);
        books_C=(CheckBox)findViewById(R.id.checkBooks);
        movies_C=(CheckBox)findViewById(R.id.checkMovies);
        music_C=(CheckBox)findViewById(R.id.checkMusic);
        electronics_C=(CheckBox)findViewById(R.id.checkElectronics);
        software_C=(CheckBox)findViewById(R.id.checkSoftware);

        checkForFileAndFolder();
        createDB();
        getPresentEnteries();
        confirmPreferencesButton = (Button) findViewById(R.id.confirmPreferencesButton);
        confirmPreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOlderEnteries();
                writetdb();
                Toast.makeText(UserPreferencesActivity.this, "Preferences Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkForFileAndFolder() {
        try {
            File folder = new File(FILE_PATH_DB);
            if (!folder.exists())
                folder.mkdir();

        } catch (Exception e)   {e.printStackTrace();}
    }


    public void createDB(){
        try {
            File folder = new File(FILE_PATH_DB);
            if (!folder.exists())
                folder.mkdir();
        } catch (Exception e)   {e.printStackTrace();}

        try{
            File folder = new File(FILE_PATH_DB);
            if (!folder.exists()) {
                folder.mkdir();
            }
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            db.beginTransaction();
            try {
                //perform your database operations here ...
                String create_table="create table if not exists tbl_"+TABLE+" (clothing BOOLEAN, footwear BOOLEAN,accessories BOOLEAN, books BOOLEAN, movies BOOLEAN, music BOOLEAN,electronics BOOLEAN ,software BOOLEAN);";
                db.execSQL(create_table );
                db.setTransactionSuccessful(); //commit your changes
            }
            catch (SQLiteException e) {
                final SQLiteException ee=e;
                mHandler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(UserPreferencesActivity.this,"ERROR DB Create"+ ee.getMessage(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(UserPreferencesActivity.this, ee.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void getPresentEnteries() {
        try {
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            //perform your database operations here ...
            db.beginTransaction();
            Cursor c = db.rawQuery("select *  from tbl_"+ TABLE , null);
            if(c.moveToFirst()){
                do{
                    clothing=c.getInt(0);
                    footwear=c.getInt(1);
                    accessories=c.getInt(2);
                    books=c.getInt(3);
                    movies=c.getInt(4);
                    music=c.getInt(5);
                    electronics=c.getInt(6);
                    software=c.getInt(7);
                }while(c.moveToNext());
            }
            c.close();
            db.setTransactionSuccessful(); //commit your changes

            if(clothing==1)
                clothing_C.setChecked(true);
            else
                clothing_C.setChecked(false);

            if(footwear==1)
                footwear_C.setChecked(true);
            else
                footwear_C.setChecked(false);

            if(accessories==1)
                accessories_C.setChecked(true);
            else
                accessories_C.setChecked(false);

            if(books==1)
                books_C.setChecked(true);
            else
                books_C.setChecked(false);

            if(movies==1)
                movies_C.setChecked(true);
            else
                movies_C.setChecked(false);

            if(music==1)
                music_C.setChecked(true);
            else
                music_C.setChecked(false);

            if(electronics==1)
                electronics_C.setChecked(true);
            else
                electronics_C.setChecked(false);

            if(software==1)
                software_C.setChecked(true);
            else
                software_C.setChecked(false);
        }
        catch (SQLiteException e) { // can't toast from a service
            final SQLiteException ee=e;
            mHandler.post(new Runnable(){
                public void run(){
                    Toast.makeText(UserPreferencesActivity.this,"ERROR DB select"+ ee.getMessage(), Toast.LENGTH_LONG).show();
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

    public void deleteOlderEnteries() {
        try {
            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            //perform your database operations here ...
            db.beginTransaction();
            db.execSQL( "delete from tbl_"+ TABLE+";" );
            db.setTransactionSuccessful(); //commit your changes
        }
        catch (SQLiteException e) { // can't toast from a service
            final SQLiteException ee=e;
            mHandler.post(new Runnable(){
                public void run(){
                    Toast.makeText(UserPreferencesActivity.this,"ERROR DB delete"+ ee.getMessage(), Toast.LENGTH_LONG).show();
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

    public void writetdb() {
        try {
            if(clothing_C.isChecked())
                clothing=1;
            else
                clothing=0;

            if(footwear_C.isChecked())
                footwear=1;
            else
                footwear=0;

            if(accessories_C.isChecked())
                accessories=1;
            else
                accessories=0;

            if(books_C.isChecked())
                books=1;
            else
                books=0;

            if(movies_C.isChecked())
                movies=1;
            else
                movies=0;

            if(music_C.isChecked())
                music=1;
            else
                music=0;

            if(electronics_C.isChecked())
                electronics=1;
            else
                electronics=0;

            if(software_C.isChecked())
                software=1;
            else
                software=0;

            db = SQLiteDatabase.openOrCreateDatabase(DATABASE_LOCATION.toString(), null);
            //perform your database operations here ...
            db.beginTransaction();
            db.execSQL( "insert into tbl_"+ TABLE+"(clothing, footwear, accessories, books, movies, music, electronics, software) values ("+clothing+", "+footwear+", "+accessories+", "+books+", "+movies+", "+music+", "+electronics+", "+software+" );" );
            db.setTransactionSuccessful(); //commit your changes
        }
        catch (SQLiteException e) { // can't toast from a service
            final SQLiteException ee=e;
            mHandler.post(new Runnable(){
                public void run(){
                    Toast.makeText(UserPreferencesActivity.this,"ERROR DB Insert"+ ee.getMessage(), Toast.LENGTH_LONG).show();
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
    @Override
    public void onLocationChanged(Location location) {

    }
}
