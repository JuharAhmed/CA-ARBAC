package tr.edu.iyte.ca_arbac;

import android.app.KeyguardManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PDM extends ContentProvider {

    private static final String TAG5 = "Test Message";
    private SQLiteDatabase APP_ROLE_Database;
    private SQLiteDatabase ROLE_PERM_Database;
    private SQLiteDatabase CONTEXT_Database;
    static final String APP_ID = "APP_ID";
    static final String PERMISSION="PERMISSION";
    static final String CONTEXT="CONTEXT";
    static final String ROLE="ROLE";
    static final String ALLOW="ALLOW";
    static final String DENY="DENY";
    static final String RESPONSE="RESPONSE";
    Context appContext;

     public PDM() {

    }

    @Override
    public boolean onCreate() {
       // Log.i(TAG5, " Inside onCreate method of PDM");
        appContext = getContext();
        return true;
  }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        String response=DENY;
        int appID=extras.getInt(APP_ID);
        String requestedPermission=extras.getString(PERMISSION);
        response=checkAppPermission(appID, requestedPermission);
        Bundle reply= new Bundle();
        reply.putString(RESPONSE,response);
        return reply;

    }

    private String checkAppPermission(int appID, String requestedPermission) {
       // Log.i(TAG5, "Inside checkAppPermission method of PDM");
        ArrayList<String> appRoles= new ArrayList<String>();
        ArrayList<String> appPermissions= new ArrayList<String>();
        boolean contextSatisfied=false;
        String response=DENY;
        appRoles=getAppRoles(appID);
        if(appRoles.isEmpty()){
            response=DENY;
           // Log.i(TAG5, "No role is assigned for App:"+appID);
        }
        else {
           // Log.i(TAG5, "Inside checkAppPermission method of PDM 1");
            Cursor c=getRolePermissions(appRoles,requestedPermission);
            while (c.moveToNext()){
                String role=c.getString(c.getColumnIndex(ROLE));
                String perm=c.getString(c.getColumnIndex(PERMISSION));
               // Log.i(TAG5, "Inside checkAppPermission method of PDM 2");
               // Log.i(TAG5, "Role:"+role+" Permission:"+perm);
                if(perm.equals(requestedPermission)){
                   // Log.i(TAG5," Role "+role+" has the requested permission: "+perm);
                    String contextPolicy=c.getString(c.getColumnIndex(PA_DB.CONTEXT));
                    String contextPolicyType=c.getString(c.getColumnIndex(PA_DB.CONTEXT_POLICY_TYPE));

                    if(contextPolicy==null){
                      response=ALLOW;
                     // Log.i(TAG5,"No context associated with permission: "+perm);
                    }
                    else{
                      //  Log.i(TAG5,"There is Context associated with "+perm+" permission");
                      //  Log.i(TAG5," Context Policy: "+"<"+contextPolicy+","+contextPolicyType+">");
                        contextSatisfied=checkContext(contextPolicy,contextPolicyType);
                        if(contextSatisfied){
                            response=ALLOW;
                         //   Log.i(TAG5,"Context associated with permission: "+perm+" is satisfied");
                        }
                        else{
                            response=DENY;
                          //  Log.i(TAG5,"Context associated with permission: "+perm+" is not satisfied");
                            break;
                        }

                    }

                }

            }

        }


        return response;
    }

    private boolean checkContext(String contextPolicy, String contextPolicyType) {
        List<String> combinedContextPolicy= new ArrayList<>();
        String[] combinedContextPolicyArray;
         boolean contextSatisfied=false;
        boolean contextFulfilled=false;
        C_DB dbHelper3 = C_DB.getInstance(appContext);
        CONTEXT_Database = dbHelper3.getWritableDatabase();
       // if(CONTEXT_Database==null)
           // Log.i(TAG5, " CONTEXT_Database is null inside checkIfContextSatisfied method of PDM");

         // Log.i(TAG5, "Inside checkIfContextSatisfied method of PDM");

        if(contextPolicy.startsWith("[")){ //single context
           // Log.i(TAG5, "Single context policy found: "+"<"+contextPolicy+","+contextPolicyType+">");
            String contextType=contextPolicy.substring(contextPolicy.indexOf("[")+1,contextPolicy.indexOf("="));
           // Log.i(TAG5, "Context Type: " + contextType);
            contextSatisfied=checkIfContextSatisfied(contextType, contextPolicy);
            contextFulfilled=checkIfContextFulfilled(contextSatisfied,contextPolicyType);
            }
        else if(contextPolicy.startsWith("(")){ //combination of contexts
           // Log.i(TAG5, "Combination of context policies found: "+"<"+contextPolicy+","+contextPolicyType+">");
            contextPolicy=contextPolicy.replace("(","");
            contextPolicy=contextPolicy.replace(")","");
            combinedContextPolicyArray=contextPolicy.split("∧");
            combinedContextPolicy=Arrays.asList(combinedContextPolicyArray);
            int contextCounter=0;
            for(String conPolicy: combinedContextPolicy) {
                String contextType = conPolicy.substring(conPolicy.indexOf("[") + 1, conPolicy.indexOf("="));
               // Log.i(TAG5, "Context Type: " + contextType);
                if(contextCounter==0)
                contextSatisfied=checkIfContextSatisfied(contextType, conPolicy);
                else
                contextSatisfied=getConditionalAND(contextSatisfied, checkIfContextSatisfied(contextType, conPolicy));
                contextCounter++;
            }
            contextFulfilled=checkIfContextFulfilled(contextSatisfied,contextPolicyType);
        }
        else {
            Log.i(TAG5, "Error in Context Policy");
        }

        return contextFulfilled;
    }

    private boolean getConditionalAND(boolean contextSatisfied, boolean b) {
        boolean conditionalAnd=false;
                if((contextSatisfied==true) &&(b==true))
                    conditionalAnd=true;
                   else
                    conditionalAnd=false;
            return conditionalAnd;
    }

    private boolean checkIfContextFulfilled(boolean contextSatisfied, String contextPolicyType) {
        boolean contextFulfilled=false;
        switch (contextPolicyType){
            case ALLOW:
                if(contextSatisfied==true)
                    contextFulfilled=true;
                else
                    contextFulfilled=false;
                break;
            case DENY:
                if(contextSatisfied==false)
                    contextFulfilled=true;
                else
                    contextFulfilled=false;
                break;
            default:
                break;
        }
        return contextFulfilled;
    }

    private boolean checkIfContextSatisfied(String contextType,String contextPolicy) {
        boolean contextSatisfied=false;
        switch (contextType){
            case ContextAssigner.LOCATION:
                 contextSatisfied=checkLocationContext(contextPolicy);
                 break;
            case ContextAssigner.CALL_STATE:
                contextSatisfied=checkCallStateContext(contextPolicy);
                break;
            case ContextAssigner.TIME:
                contextSatisfied=checkTimeContext(contextPolicy);
                break;
            case ContextAssigner.SCREEN_STATE:
                contextSatisfied=checkScreenStateContext(contextPolicy);
                break;
            default:
                break;
        }
    return contextSatisfied;
     }

    private boolean checkScreenStateContext(String contextPolicy) {
        String currentScreenState=null;
        boolean contextSatisfied=false;
        //Log.i(TAG5, "Inside  checkScreenStateContext method of PDM");
        contextPolicy=contextPolicy.replace("[SCREEN_STATE=","");
        contextPolicy=contextPolicy.replace("]","");
       // Log.i(TAG5, "Screen_State Context Policy Value:" + contextPolicy);
        currentScreenState=getCurrentScreenState();
       // Log.i(TAG5, "Current screen state value:"+currentScreenState);
        if(currentScreenState.equals(contextPolicy))
             contextSatisfied=true;
        return contextSatisfied;
    }

    private String getCurrentScreenState() {
        String currentScreenState=null;
        KeyguardManager myKM = (KeyguardManager) appContext.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            currentScreenState="SCREEN_STATE_OFF";
        } else {
            currentScreenState="SCREEN_STATE_ON";
        }
        return currentScreenState;
    }

    private boolean checkTimeContext(String contextPolicy) {
        String[] temp;
        boolean contextSatisfied=false;
        int startTime=0;
        int endTime=0;
        String days=new String();

        //TimePicker timePicker= new TimePicker(appContext);
       // Log.i(TAG5, "Inside  checkTimeContext method of PDM");
        contextPolicy=contextPolicy.replace("[TIME=","");
        contextPolicy=contextPolicy.replace("]","");
       // Log.i(TAG5, "Time Context Policy Value:" + contextPolicy);
        temp=contextPolicy.split(";");
        startTime=Integer.valueOf(temp[0]);
        endTime=Integer.valueOf(temp[1]);
        days=temp[2];
       // Log.i(TAG5, "Start Time from PDM:" + startTime);
       // Log.i(TAG5, "End Time from PDM:" + endTime);
       // Log.i(TAG5, "Days from PDM:" + endTime);

        Calendar calendar=Calendar.getInstance();
        String currentDay=calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String timeInString=String.valueOf(Calendar.HOUR)+String.valueOf(Calendar.MINUTE);
        int currentTime=Integer.valueOf(timeInString);
       // Log.i(TAG5, "Current day from PDM:" + currentDay);
       // Log.i(TAG5, "Current Time from PDM:" + currentTime);
        if((currentTime>=startTime)&&(currentTime<=endTime)&&(days.contains(currentDay)));
        contextSatisfied=true;

        return contextSatisfied;
    }

    private boolean checkCallStateContext(String contextPolicy) {

        boolean contextSatisfied=false;
       // Log.i(TAG5, "Inside  checkCallStateContext method of PDM");
        contextPolicy=contextPolicy.replace("[CALL_STATE=","");
        contextPolicy=contextPolicy.replace("]","");
       // Log.i(TAG5, "Call_State Context Policy Value:" + contextPolicy);
        String[]selectionArg={ContextAssigner.CALL_STATE};
        Cursor c = CONTEXT_Database.rawQuery("SELECT CONTEXT_VALUE FROM C_DB_TABLE WHERE CONTEXT_NAME=?", selectionArg);
        if(c==null)
            Log.i(TAG5, "Cursor is null inside checkCallStateContext method of PDM 4");
        c.moveToFirst();
        String currentCallState=c.getString(c.getColumnIndex(C_DB.CONTEXT_VALUE));
        Log.i(TAG5, "Current call state value from C_DB:"+currentCallState);
        currentCallState=currentCallState.replace("[","");
        currentCallState=currentCallState.replace("]","");
        if(contextPolicy.equals(currentCallState))
            contextSatisfied=true;
        return contextSatisfied;
    }

    private boolean checkLocationContext(String contextPolicy) {
        String[] temp;
        boolean contextSatisfied=false;
        //Log.i(TAG5, "Inside  checkLocationContext method of PDM");
        contextPolicy=contextPolicy.replace("[LOCATION=","");
        contextPolicy=contextPolicy.replace("]","");
        //Log.i(TAG5, "Location Context Policy Value:"+contextPolicy);
        temp=contextPolicy.split(";");
        double lat1=Double.valueOf(temp[0])/1e6; //this is to convert the location context value from geoPoint to Location
        double lon1=Double.valueOf(temp[1])/1e6;
        double lat2=Double.valueOf(temp[2])/1e6;
        double lon2=Double.valueOf(temp[3])/1e6;

        String[]selectionArg={ContextAssigner.LOCATION};
        Cursor c = CONTEXT_Database.rawQuery("SELECT CONTEXT_VALUE FROM C_DB_TABLE WHERE CONTEXT_NAME=?", selectionArg);
         if(c==null)
        // Log.i(TAG5, "Cursor is null inside checkLocationContext method of PDM 4");
        c.moveToFirst();
            String location=c.getString(c.getColumnIndex(C_DB.CONTEXT_VALUE));
        // Log.i(TAG5, "Current Location value from C_DB:"+location);
            location=location.replace("[","");
            location=location.replace("]","");
            temp=location.split(";");
            double lat=Double.valueOf(temp[0]);
            double lon=Double.valueOf(temp[1]);

        if(getDistanceBetween(lat1, lon1, lat, lon)<=getDistanceBetween(lat1,lon1,lat2,lon2)){
           // Log.i(TAG5, "current location inside the circle;location context is satisfied:");
            contextSatisfied=true;
        }

       return contextSatisfied;
    }

    public float getDistanceBetween(double lat1, double lng1, double lat2, double lng2) {
        float [] dist = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, dist);
        return dist[0];
    }

    private Cursor getRolePermissions(ArrayList<String> appRoles, String requestedPermission) {

        PA_DB dbHelper2 = PA_DB.getInstance(appContext);
        ROLE_PERM_Database = dbHelper2.getWritableDatabase();

       // Log.i(TAG5, "Inside getRolePermissions method of PDM");
        String[]selectionArgs = new String[appRoles.size()];
        selectionArgs = appRoles.toArray(selectionArgs);
       // if(ROLE_PERM_Database==null)
         //   Log.i(TAG5, " APP_ROLE_Database is null inside getAppRoles method of PDM");
       // String[]selectionArgs={appRole,requestedPermission};
       // String[]selectionArgs=(String[])appRoles.toArray();
       // String[]selectionArgs={"MultiMedia","Game"};

        String query = "SELECT ROLE,PERMISSION,CONTEXT,CONTEXT_POLICY_TYPE FROM '"+PA_DB.TABLE_NAME+"'WHERE ROLE IN (" + makePlaceholders(selectionArgs.length) + ")" +"AND PERMISSION = '" + requestedPermission + "'";
        Cursor cursor = ROLE_PERM_Database.rawQuery(query, selectionArgs);
        return  cursor;
    }

    String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    private ArrayList<String> getAppRoles(int appID) {

        AA_DB dbHelper = AA_DB.getInstance(appContext);
        APP_ROLE_Database = dbHelper.getWritableDatabase();

        //Log.i(TAG5, "Inside getAppRoles method of PDM");
        ArrayList<String> appRoles= new ArrayList<String>();
       // if(APP_ROLE_Database==null)
          //  Log.i(TAG5, " APP_ROLE_Database is null inside getAppRoles method of PDM");
        String[]selectionArg={String.valueOf(appID)};
        Cursor c = APP_ROLE_Database.rawQuery("SELECT ROLE FROM AA_DB_TABLE WHERE APP_ID=?", selectionArg);
        //if(c==null)
         //   Log.i(TAG5, "c is null inside getAppRoles method of PDM");
          while(c.moveToNext()){
            String role=c.getString(c.getColumnIndex(ROLE));
            appRoles.add(role);
           // Log.i(TAG5,"AppID:"+appID+" Role:"+role);
        }
    return appRoles;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
