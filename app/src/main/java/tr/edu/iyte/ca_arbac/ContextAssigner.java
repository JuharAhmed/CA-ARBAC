package tr.edu.iyte.ca_arbac;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class ContextAssigner extends ListActivity implements OnItemSelectedListener, DialogInterface.OnClickListener {
    private static final String TAG5 = "Test Message";

    String roleName;
    ArrayList<String> totalPermissionsForTheRole = new ArrayList<>();
    String[] totalPermissionsForTheRoleArray;
    private ListView totalPermissionsForTheRoleListView;
    ArrayList<String> permissionsSelectedForContext = new ArrayList<>();

    String[] contextTypeList;
    ListView contextTypesListView;
    ArrayList<String> selectedContexts= new ArrayList<>();

    String[]contextPolicyTypeList;
    Spinner contextPolicyTypeSpinner;
    String selectedContextPolicyType= new String();

    private Button button;
    ArrayAdapter<String> adapter;

    private boolean locationSelected,timeSelected,callStateSelected;

    public static final String TIME = "TIME";
    public static final int TIME_SELECTOR = 2;

    public static final int CALL_STATE_SELECTOR = 3;
    public static final String CALL_STATE = "CALL_STATE";

    public static final String LOCATION = "LOCATION";
    public static final int LOCATION_SELECTOR = 1;

    public static final String SCREEN_STATE="SCREEN_STATE";
    public static final int SCREEN_STATE_SELECTOR = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG5, "context assigner1");
        setContentView(R.layout.activity_context_assigner);
        Log.i(TAG5, "context assigner2");
        Bundle b = this.getIntent().getExtras();
        totalPermissionsForTheRole = b.getStringArrayList("selectedPermissions");
        roleName = b.getString("roleName");
        Log.i(TAG5, "SelectedPermissions from context Assigner:" + totalPermissionsForTheRole);
        Log.i(TAG5, "roleName from context Assigner:" + roleName);

        totalPermissionsForTheRoleArray = new String[totalPermissionsForTheRole.size()];
        totalPermissionsForTheRoleArray = totalPermissionsForTheRole.toArray(totalPermissionsForTheRoleArray);
        totalPermissionsForTheRoleListView = getListView();
        totalPermissionsForTheRoleListView.setChoiceMode(totalPermissionsForTheRoleListView.CHOICE_MODE_MULTIPLE);
        totalPermissionsForTheRoleListView.setTextFilterEnabled(true);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, totalPermissionsForTheRoleArray);
        totalPermissionsForTheRoleListView.setAdapter(adapter);

        Resources res = getResources();
        contextTypeList = res.getStringArray(R.array.contextTypes);
        contextPolicyTypeList = new String[]{"SELECT POLICY TYPE", "DENY", "ALLOW",};
        contextPolicyTypeSpinner = (Spinner) findViewById(R.id.policyTypeSpinner);
        contextPolicyTypeSpinner.setOnItemSelectedListener(this);
        contextPolicyTypeSpinner.setSelection(0);
        ArrayAdapter dataAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, contextPolicyTypeList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contextPolicyTypeSpinner.setAdapter(dataAdapter2);

        contextTypesListView= (ListView) findViewById(R.id.contextTypesListView);
        CustomAdapter customAdapter = new CustomAdapter(this,contextTypeList);
        contextTypesListView.setAdapter(customAdapter);

    }


    public void onListItemClick(ListView parent, View v, int position, long id) {
        permissionsSelectedForContext.add(totalPermissionsForTheRoleArray[position]);

    }

    //when the save button is clicked, a dialogBox that asks the user if he wants to configure another context is displayed
    // if the user wants to configure more contexts, save the prior configured context to database and
    // refresh the user interface and display the user interface for the next context configuration
    // if the user don't want to configure more contexts, save the configured contexts to the database
    // and close the context assigner activity

    public void saveButtonClickedHandler(View view) {

        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(
                ContextAssigner.this);
        alertDialog1.setTitle("Assign Context...");
        alertDialog1.setMessage("Context successfully saved" + "\n" + "Do you want to assign another context");
        alertDialog1.setPositiveButton("YES", this);
        alertDialog1.setNegativeButton("NO", this);
        alertDialog1.show();

    }

    // the response from the user on Dialog popup is captured in onClick method(it is Yes/No)
    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == DialogInterface.BUTTON_POSITIVE) {
            saveToDatabase();
            refreshUserInterface();

        }
        else{
            saveToDatabase();
            this.finish();
        }




    }

    protected void saveToDatabase(){

        Long rowID;
        String combinedContext=new String();
        PA_DB dbHelper = PA_DB.getInstance(getApplicationContext());
        SQLiteDatabase ROLE_PERM_Database = dbHelper.getWritableDatabase();

        if((!selectedContexts.isEmpty())&&(selectedContextPolicyType!=null) &&(!permissionsSelectedForContext.isEmpty())){
            combinedContext=getCombinedContexts();
            //Log.i(TAG5,"Combined Context inside Context Assigner: "+ "<"+combinedContext+","+selectedContextPolicyType+">");
            for(String per:permissionsSelectedForContext){
                ContentValues values = new ContentValues();
                values.put(PA_DB.ROLE, roleName);
                values.put(PA_DB.PERMISSION, per);
                values.put(PA_DB.CONTEXT, combinedContext);
                values.put(PA_DB.CONTEXT_POLICY_TYPE, selectedContextPolicyType);
                rowID = ROLE_PERM_Database.insert(PA_DB.TABLE_NAME, "", values);
              //  Log.i(TAG5,"Row Id inside Context Assigner:"+String.valueOf(rowID));

            }
        }

    }

    private String getCombinedContexts() {

    String combinedContext= new String();

    if(selectedContexts.size()>1){
        for(String con:selectedContexts) {
            if (combinedContext.isEmpty()) {
                combinedContext = combinedContext.concat("[");
                combinedContext = combinedContext.concat(con);
            }

            else{
                combinedContext = combinedContext.concat("]∧[");
                combinedContext = combinedContext.concat(con);
            }
        }
       combinedContext="("+combinedContext.concat("])");
    }
        else{
        combinedContext="["+selectedContexts.get(0)+"]";
    }

       return combinedContext;
    }

    private void refreshUserInterface(){

        contextPolicyTypeSpinner.setSelection(0);
        permissionsSelectedForContext=new ArrayList<>();
        totalPermissionsForTheRoleListView.clearChoices();
        adapter.notifyDataSetChanged();
        selectedContexts= new ArrayList<>();
        selectedContextPolicyType=new String();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         if (parent.getId() == R.id.policyTypeSpinner)
             selectedContextPolicyType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SELECTOR:
                selectedContexts.add(data.getStringExtra(LOCATION));
               // Log.i(TAG5, "Location Context:" + data.getStringExtra(LOCATION));
                break;
            case TIME_SELECTOR:
                selectedContexts.add(data.getStringExtra(TIME));
               // Log.i(TAG5, "Time Context:" + data.getStringExtra(TIME));
                break;
            case CALL_STATE_SELECTOR:
                selectedContexts.add(data.getStringExtra(CALL_STATE));
               // Log.i(TAG5, "Call State Context:" + data.getStringExtra(CALL_STATE));
                break;
            case SCREEN_STATE_SELECTOR:
                selectedContexts.add(data.getStringExtra(SCREEN_STATE));
              //  Log.i(TAG5, "SCREEN_STATUS Context:" + data.getStringExtra(SCREEN_STATE));
                break;
            default:
                break;
        }
    }


    class CustomAdapter extends ArrayAdapter<String> {

        Context context;
        String[] contextTypeList;
        public CustomAdapter(Context c, String[] contextTypeList) {
            super(c, R.layout.single_row1, R.id.contextName, contextTypeList);
            this.context=c;
            this.contextTypeList=contextTypeList;
        }

        @Override
        public View getView(int position, View view, final ViewGroup parent) {
            //Log.i(TAG5, "inside getView method of RoleAssigner");
            View row=view;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row1, parent, false);
            }

            TextView conName= (TextView) row.findViewById(R.id.contextName);
            Button button =(Button)row.findViewById(R.id.contextSetterButton);


            conName.setText(contextTypeList[position]);
            //button.setText("Assign");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContextMethod(view);

                }
            });

            return row;
        }
    }


    public void setContextMethod(View v){
          if(v.getId()==R.id.contextSetterButton){
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            int position = listView.getPositionForView(parentRow);
           String currentlySelectedContextType=contextTypeList[position];

            Intent i = null;
            switch (currentlySelectedContextType) {
                case LOCATION:
                    i = new Intent(getBaseContext(), LocationSelector.class);
                    startActivityForResult(i, LOCATION_SELECTOR);
                    break;
                case TIME:
                   // Log.i(TAG5,"Case time");
                    i = new Intent(getBaseContext(), TimeSelector.class);
                    startActivityForResult(i, TIME_SELECTOR);
                   // Log.i(TAG5, "Case time2");
                    break;
                case CALL_STATE:
                    i = new Intent(getBaseContext(), CallStateSelector.class);
                    startActivityForResult(i, CALL_STATE_SELECTOR);
                    break;
                case SCREEN_STATE:
                    i = new Intent(getBaseContext(), ScreenStateSelector.class);
                    startActivityForResult(i, SCREEN_STATE_SELECTOR);
                    break;
                default:
                    break;
            }

        }

    }

}

