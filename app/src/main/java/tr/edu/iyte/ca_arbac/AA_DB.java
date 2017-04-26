package tr.edu.iyte.ca_arbac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by john on 27.08.2015.
 */
public class AA_DB extends SQLiteOpenHelper {

    private static final String TAG5 = "Test Message";
    // private SQLiteDatabase db;
    private static AA_DB sInstance;
    static final String DATABASE_NAME = "AA_DB";
    static final String TABLE_NAME = "AA_DB_TABLE";
    public static final String ROLE = "ROLE";
    public static final String APP_ID = "APP_ID";
    static final int DATABASE_VERSION = 1;

    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME +
            "( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            APP_ID + " TEXT NOT NULL," +
            ROLE + " TEXT NOT NULL )";

    public static synchronized AA_DB getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new AA_DB(context.getApplicationContext());
           // Log.i(TAG5, " Inside getInstance method of AA_DB");
        }
        return sInstance;
    }

    private AA_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // Log.i(TAG5, " Inside constructor of AA_DB");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
      //  Log.i(TAG5, " Inside onCreate method of AA_DB");
        db.execSQL(CREATE_DB_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // Log.i(TAG5, " Inside onUpgrade method of AA_DB");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
