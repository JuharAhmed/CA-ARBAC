package tr.edu.iyte.ca_arbac;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CallStateSelector extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] callStateListArray = {"SELECT CALL STATE", "CALL_STATE_RINGING", "CALL_STATE_OFFHOOK", "CALL_STATE_IDLE"};
    ListView callStateListView;
    String selectedCallState = new String();
    Spinner callStateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_state_selector);

        callStateSpinner = (Spinner) findViewById(R.id.callStateSpinner);
        callStateSpinner.setOnItemSelectedListener(this);
        callStateSpinner.setSelection(0);
        ArrayAdapter dataAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, callStateListArray);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        callStateSpinner.setAdapter(dataAdapter2);
    }

    public void saveCallStateMethod(View view) {
        String callStateContext = new String();
        callStateContext = ContextAssigner.CALL_STATE + "=" +selectedCallState;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ContextAssigner.CALL_STATE, callStateContext);
        setResult(ContextAssigner.CALL_STATE_SELECTOR, returnIntent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.callStateSpinner)
            selectedCallState = parent.getItemAtPosition(position).toString();
     }

    @Override
    public void onNothingSelected (AdapterView < ? > parent){
        parent.setSelection(0);
    }

}
