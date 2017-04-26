package tr.edu.iyte.ca_arbac;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by john on 13.09.2015.
 */
public class C_DB extends SQLiteOpenHelper {

    private static final String TAG5 = "Test Message";

    private static C_DB sInstance;
    static final String DATABASE_NAME = "C_DB";
    static final String TABLE_NAME = "C_DB_TABLE";
    static final int DATABASE_VERSION = 1;


    static final String CONTEXT_NAME="CONTEXT_NAME";
    static final String LOCATION="LOCATION";
    static final String CALL_STATE="CALL_STATE";
    static final String TIME="TIME";
    static final String CONTEXT_VALUE="CONTEXT_VALUE";


    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CONTEXT_NAME +" TEXT NOT NULL, " +
            CONTEXT_VALUE + " TEXT NOT NULL)";

    public static synchronized C_DB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new C_DB(context.getApplicationContext());
           // Log.i(TAG5, "Inside getInstance method of C_DB");
        }
        return sInstance;
    }

    private C_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // Log.i(TAG5, "Inside constructor of C_DB");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);

        ContentValues values= new ContentValues();
        values.put(C_DB.CONTEXT_NAME, C_DB.LOCATION);
        values.put(C_DB.CONTEXT_VALUE,"37422006;-122084095");
        db.insert(C_DB.TABLE_NAME,null,values);

        values= new ContentValues();
        values.put(C_DB.CONTEXT_NAME, C_DB.CALL_STATE);
        values.put(C_DB.CONTEXT_VALUE, String.valueOf(TelephonyManager.CALL_STATE_IDLE));
        db.insert(C_DB.TABLE_NAME,null,values);

     // Log.i(TAG5, "Inside onCreate method of C_DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // Log.i(TAG5, " Inside onUpgrade method of C_DB");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
