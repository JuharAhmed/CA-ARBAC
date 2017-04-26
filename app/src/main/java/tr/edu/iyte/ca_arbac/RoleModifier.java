package tr.edu.iyte.ca_arbac;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class RoleModifier extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] databaseNameList;
    Spinner databaseListSpinner;
    private String selectedDatabase;
    SQLiteDatabase databaseToBeDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_modifier);

        databaseNameList = new String[]{"SELECT THE DATABASE TO DELETE", "AA_DB", "PA_DB","C_DB"};
        databaseListSpinner = (Spinner) findViewById(R.id.modifyRoleSpinner);
        databaseListSpinner.setOnItemSelectedListener(this);
        databaseListSpinner.setSelection(0);
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, databaseNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        databaseListSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.modifyRoleSpinner)
            selectedDatabase = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }

    public void modifyRoleMethod(View view){
        if(view.getId()==R.id.deleteDatabase){
            if(selectedDatabase.equals("AA_DB")){
                AA_DB dbHelper = AA_DB.getInstance(getApplicationContext());
                databaseToBeDeleted = dbHelper.getWritableDatabase();
                databaseToBeDeleted.delete(AA_DB.TABLE_NAME,null,null);
                Toast.makeText(getBaseContext(), "AA_DB successfully deleted", Toast.LENGTH_LONG).show();
                this.finish();
            }
            else if(selectedDatabase.equals("PA_DB")){
                PA_DB dbHelper = PA_DB.getInstance(getApplicationContext());
                databaseToBeDeleted = dbHelper.getWritableDatabase();
                databaseToBeDeleted.delete(PA_DB.TABLE_NAME,null,null);
                Toast.makeText(getBaseContext(),"PA_DB successfully deleted",Toast.LENGTH_LONG).show();
                this.finish();
            }
            else if(selectedDatabase.equals("C_DB")){
                C_DB dbHelper = C_DB.getInstance(getApplicationContext());
                databaseToBeDeleted = dbHelper.getWritableDatabase();
                databaseToBeDeleted.delete(C_DB.TABLE_NAME,null,null);
                Toast.makeText(getBaseContext(),"C_DB successfully deleted",Toast.LENGTH_LONG).show();
                this.finish();
            }
            else if(selectedDatabase.equals("SELECT THE DATABASE TO BE MODIFIED")){
                Toast.makeText(getBaseContext(),"No database is selected for deletion",Toast.LENGTH_LONG).show();
                this.finish();

            }
        }

    }
}
