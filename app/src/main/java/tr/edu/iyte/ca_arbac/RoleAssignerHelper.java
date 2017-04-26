package tr.edu.iyte.ca_arbac;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class RoleAssignerHelper extends ListActivity{

    private static final String TAG5 = "Test Message";
    private SQLiteDatabase ROLE_PERM_Database;
    private SQLiteDatabase APP_ROLE_Database;
    String appName;
    String appID;
    TextView text;
    Cursor cursor;
    ArrayList<String> roleList= new ArrayList<String>();
    ListView l;
    ArrayList<String> selectedRoles= new ArrayList<String>();
    String[] roleListArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_assigner_helper);
        //Log.i(TAG5,"Inside onCreate method of RoleAssignerHelper");
       // Log.i(TAG5, "inside onCreate method of RoleAssignerHelper");
        l =getListView();
        roleList=getRoles();
        text= (TextView) findViewById(R.id.appTextView);

        Intent intent=getIntent();
        appID=intent.getStringExtra("APP_ID");
        appName=intent.getStringExtra("APP_NAME");
        text.setText("Assigning role to: "+appName);
        Log.i(TAG5, "inside onCreate method of RoleAssignerHelper 2");
        roleListArray = new String[roleList.size()];
        roleListArray = roleList.toArray(roleListArray);

        l.setChoiceMode(l.CHOICE_MODE_MULTIPLE);
        //--	text filtering
        l.setTextFilterEnabled(true);
     ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked,roleListArray);
        l.setAdapter(adapter);
        // lView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, userInstalledApps));

       }

    private ArrayList<String> getRoles() {
        ArrayList<String> roles= new ArrayList<String>();

        PA_DB dbHelper = PA_DB.getInstance(getApplicationContext());
        ROLE_PERM_Database = dbHelper.getWritableDatabase();
        if(ROLE_PERM_Database==null)
            Log.i(TAG5,"RoleAssignerHelper: ROLE_PERM_DB is null");

        else if(ROLE_PERM_Database!=null){
            //Log.i(TAG5,"RoleAssignerHelper: ROLE_PERM_DB is not null");
            String[] columns= {"ROLE"};
            Cursor cursor=ROLE_PERM_Database.query(true, PA_DB.TABLE_NAME, columns, null, null, null, null, null, null);

           // Log.i(TAG5,String.valueOf(cursor.getCount()));
                while(cursor.moveToNext()){
                String s=cursor.getString(cursor.getColumnIndex("ROLE"));
                roles.add(s);
               Log.i(TAG5, s);
            }

        }
        return roles;
    }

    public void saveRoleToDatabase2(View v){
        long rowID=0;

        AA_DB dbHelper = AA_DB.getInstance(getApplicationContext());
        APP_ROLE_Database = dbHelper.getWritableDatabase();
        if (APP_ROLE_Database== null)
            Log.i(TAG5, "APP_ROLE_Database cannot be created");
        Cursor dbCursor = APP_ROLE_Database.query(AA_DB.TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
        Log.i(TAG5, "Column Names of AA DB"+ Arrays.toString(columnNames));

        if(selectedRoles!=null){
            for(String r:selectedRoles){
                Log.i(TAG5, "Inside saveRoleToDatabase2 method of RoleAssignerHelper");
                ContentValues values = new ContentValues();
                values.put(AA_DB.APP_ID,appID);
                values.put(AA_DB.ROLE,r);
                Log.i(TAG5, "App ID to be inserted" + appID);
                Log.i(TAG5, "Role to be inserted" + r);
                rowID = APP_ROLE_Database.insert(AA_DB.TABLE_NAME,null,values);
                Log.i(TAG5, "RowID from RoleAssignerHelper "+rowID);

            }
        }

        Cursor c=APP_ROLE_Database.rawQuery("SELECT APP_ID, ROLE FROM AA_DB_TABLE",null);
        while(c.moveToNext()){
            Log.i(TAG5,c.getString(c.getColumnIndex(AA_DB.APP_ID))+":"+c.getString(c.getColumnIndex(AA_DB.ROLE)));
        }
        appID=null;
        selectedRoles=null;
        this.finish();
    }

    public void onListItemClick(ListView parent, View v,int position,long id){

        //Log.i(TAG5, "role selected");
        selectedRoles.add(roleListArray[position]);

    }

}
