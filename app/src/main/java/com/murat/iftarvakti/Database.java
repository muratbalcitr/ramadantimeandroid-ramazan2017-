package com.murat.iftarvakti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by murat on 11.04.2017.
 */

public class Database extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "imsakiyeler.db";
    public final static String DATABASE_PATH = "/data/data/com.murat.iftarvakti/databases/imsakiyeler.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteDatabase dataBase;
    private final Context dbContext;

    public Database(Context context) {
        super(context, "imsakiyeler.db", null, DATABASE_VERSION);
        this.dbContext = context;

        // checking Database and open it if exists
        if (checkDataBase()) {
            try {
                openDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
        {
            try {
                this.getReadableDatabase();
                copyDataBase();
                this.close();
                openDataBase();

            } catch (IOException e) {
                //throw new Error("Error copying Database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
  //  public static final String DATABASE_ACTIVE_CITY = "CREATE TABLE user_Default_activity (active_main   TEXT);";
    @Override
    public void onCreate(SQLiteDatabase db){
   //     db.execSQL(DATABASE_ACTIVE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
         //   db.execSQL("DROP TABLE IF EXISTS" + DATABASE_ACTIVE_CITY);
         }}

    public void copyDataBase() throws IOException {
        InputStream myInput = dbContext.getAssets().open("imsakiyeler.db");
        String outFileName = DATABASE_PATH;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024*81];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String dbPath = DATABASE_PATH;
        dataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        boolean exist = false;
        try {
            String dbPath = DATABASE_PATH;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.v("db log", "Database does't exist");
        }

        if (checkDB != null) {
            exist = true;
            checkDB.close();
        }
        return exist;
    }


}


