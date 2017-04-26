package tr.edu.iyte.ca_arbac;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class RoleCreator extends ListActivity implements DialogInterface.OnClickListener {
    private static final String TAG5 = "Test Message";
    String[] androidPermissionList;
    private ListView permissionListView;
    private ArrayAdapter<String> listAdapter;
    String[] PermissionList;
    String roleName;
    ArrayList<String> selectedPermissions = new ArrayList<String>();
    EditText mEdit;
    private SQLiteDatabase ROLE_PERM_Database;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_role_creator);

        mEdit = (EditText) findViewById(R.id.roleNameTextField);

        Resources res = getResources();
        androidPermissionList = res.getStringArray(R.array.androidPermissionList);
        //Utility.getAllAndroidPermissions(getApplicationContext());
        //PermissionList = new String[androidPermissionList.size()];
        //PermissionList = androidPermissionList.toArray(PermissionList);
        permissionListView = getListView();
        permissionListView.setChoiceMode(permissionListView.CHOICE_MODE_MULTIPLE);
        permissionListView.setTextFilterEnabled(true);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, androidPermissionList);
        permissionListView.setAdapter(adapter);

        PA_DB dbHelper = PA_DB.getInstance(getApplicationContext());
        ROLE_PERM_Database = dbHelper.getWritableDatabase();
        if (ROLE_PERM_Database == null)
            Log.i(TAG5, "ROLE_PERM_DB cannot be created");
        Cursor dbCursor = ROLE_PERM_Database.query(PA_DB.TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
        Log.i(TAG5, "Column Names of AA DB"+ Arrays.toString(columnNames));


    }

    public void saveRoleToDatabase(View v) {
       // permissionListView.clearChoices();
       // adapter.notifyDataSetChanged();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                RoleCreator.this);
        alertDialog.setTitle("Assign Context...");
        alertDialog.setMessage("Your role has been created" + "\n" + "Do you want to assign context to permissions?");
        alertDialog.setPositiveButton("YES", this);
        alertDialog.setNegativeButton("NO", this);
        alertDialog.show();
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        selectedPermissions.add(androidPermissionList[position]);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        long rowID = 0;
        Log.i(TAG5, "Role creator1 ");
        roleName = mEdit.getText().toString();
        if (which == DialogInterface.BUTTON_POSITIVE) {
               //Log.i(TAG5, "role Name:"+roleName);
            Log.i(TAG5, "Role creator2 ");
            if ((selectedPermissions != null) && (roleName != null)) {
                Log.i(TAG5, "Role creator3 ");
                Bundle b = new Bundle();
                b.putStringArrayList("selectedPermissions", selectedPermissions);
                b.putString("roleName", roleName);
                Intent i = new Intent(this, ContextAssigner.class);
                i.putExtras(b);
                startActivity(i);
                Log.i(TAG5, "Role creator4 ");
            }


        } else {
            Log.i(TAG5, "Role creator5 ");
            if((selectedPermissions != null) && (roleName != null)){
                for(String per:selectedPermissions){
                    ContentValues values = new ContentValues();
                    values.put(PA_DB.ROLE,roleName);
                    values.put(PA_DB.PERMISSION,per);
                    rowID = ROLE_PERM_Database.insert(PA_DB.TABLE_NAME, PA_DB.CONTEXT, values);
                    Log.i(TAG5, "Role creator6 ");
                    Log.i(TAG5, "rowId="+rowID);
                }
            }

        }

        roleName = null;
        selectedPermissions = null;
        this.finish();

    }
}
