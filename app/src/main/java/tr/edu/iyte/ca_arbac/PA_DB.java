package tr.edu.iyte.ca_arbac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by john on 27.08.2015.
 */
public class PA_DB extends SQLiteOpenHelper {

    private static PA_DB sInstance;
    private static final String TAG5 = "Test Message";

    public static final String ROLE = "ROLE";
    public static final String PERMISSION = "PERMISSION";
    public static final String CONTEXT = "CONTEXT";
    public static final String CONTEXT_POLICY_TYPE = "CONTEXT_POLICY_TYPE";

    static final String DATABASE_NAME = "PA_DB";
    static final String TABLE_NAME = "PA_DB_TABLE";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ROLE+" TEXT NOT NULL, " +
            PERMISSION+" TEXT NOT NULL, " +
            CONTEXT+" TEXT, " +
            CONTEXT_POLICY_TYPE+" TEXT )";

    public static synchronized PA_DB getInstance(Context context) {
    if (sInstance == null) {
        sInstance = new PA_DB(context.getApplicationContext());
       // Log.i(TAG5, " Inside getInstance method of PA_DB");
    }
    return sInstance;
}

    private PA_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Log.i(TAG5, " Inside constructor of PA_DB");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       // Log.i(TAG5, " Inside onCreate method of PA_DB");
        db.execSQL(CREATE_DB_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // Log.i(TAG5, " Inside onUpgrade method of PA_DB");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
