package tr.edu.iyte.ca_arbac;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ContextManager extends Service {

    private static final String TAG5 = "Test Message";
    TelephonyManager telephonyManagerManager;
    MyPhoneStateListener listener;
    LocationManager locationManager;

    public ContextManager() {
    }

    @Override
    public IBinder onBind(Intent intent) {
     return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        telephonyManagerManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

       // Log.i(TAG5, "Inside onCreate method of ContextManager");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //noinspection ResourceType
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,new locationListener());
        telephonyManagerManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
       // Log.i(TAG5, "Inside onStartCommand method of ContextManager");
        return START_STICKY;
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {
            C_DB dbHelper = C_DB.getInstance(getApplicationContext());
            SQLiteDatabase context_Database = dbHelper.getWritableDatabase();
            ContentValues values= null;
            long rowID=0;
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    values= new ContentValues();
                    values.put(C_DB.CONTEXT_VALUE,"[CALL_STATE_IDLE]");
                    String[] selectionArgs1={C_DB.CALL_STATE};
                    rowID=context_Database.update(C_DB.TABLE_NAME,values, C_DB.CONTEXT_NAME+"=?",selectionArgs1);
                   // Log.i(TAG5, " CALL_STATE_IDLE");
                    Toast.makeText(getBaseContext(), "CALL_STATE_IDLE", Toast.LENGTH_LONG).show();
                  break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    values= new ContentValues();
                    values.put(C_DB.CONTEXT_VALUE,"[CALL_STATE_OFFHOOK]");
                    String[] selectionArgs2={C_DB.CALL_STATE};
                    rowID=context_Database.update(C_DB.TABLE_NAME,values, C_DB.CONTEXT_NAME+"=?",selectionArgs2);
                   // Log.i(TAG5, " CALL_STATE_OFFHOOK");
                    Toast.makeText(getBaseContext(), "CALL_STATE_OFFHOOK", Toast.LENGTH_LONG).show();
                     break;
                case TelephonyManager.CALL_STATE_RINGING:
                    values= new ContentValues();
                    values.put(C_DB.CONTEXT_VALUE,"[CALL_STATE_RINGING]");
                    String[] selectionArgs3={C_DB.CALL_STATE};
                    rowID=context_Database.update(C_DB.TABLE_NAME,values, C_DB.CONTEXT_NAME+"=?",selectionArgs3);
                   // Log.i(TAG5, " CALL_STATE_RINGING");
                    Toast.makeText(getBaseContext(), "CALL_STATE_RINGING", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }


        }

    }

    private class locationListener implements LocationListener{

            public void onLocationChanged(Location location){
                C_DB dbHelper = C_DB.getInstance(getApplicationContext());
                SQLiteDatabase context_Database = dbHelper.getWritableDatabase();

                long rowID=0;
                ContentValues values= new ContentValues();
                String locationValue=String.valueOf(location.getLatitude())+";"+String.valueOf(location.getLongitude());
                values.put(C_DB.CONTEXT_VALUE,"["+locationValue+"]");
                String[] selectionArgs={C_DB.LOCATION};
                rowID=context_Database.update(C_DB.TABLE_NAME,values, C_DB.CONTEXT_NAME+"=?",selectionArgs);
                Toast.makeText(getBaseContext(), "LOCATION CHANGED", Toast.LENGTH_LONG).show();
               // Log.i(TAG5, "LOCATION CHANGED");
            }
            public void onStatusChanged( String provider,    int status,    Bundle extras){
            }
            public void onProviderEnabled(String provider){
            }
            public void onProviderDisabled( String provider){
            }
        }

 }
