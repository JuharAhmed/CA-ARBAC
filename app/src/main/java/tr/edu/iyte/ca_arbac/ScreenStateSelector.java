package tr.edu.iyte.ca_arbac;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class ScreenStateSelector extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] screenStateListArray = {"SELECT SCREEN STATE", "SCREEN_STATE_ON", "SCREEN_STATE_OFF"};
    ListView screenStateListView;
    String selectedScreenState = new String();
    Spinner screenStateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_state_selector);
        screenStateSpinner = (Spinner) findViewById(R.id.screenStateSpinner);
        screenStateSpinner.setOnItemSelectedListener(this);
        screenStateSpinner.setSelection(0);
        ArrayAdapter dataAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, screenStateListArray);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        screenStateSpinner.setAdapter(dataAdapter2);
    }

    public void saveScreenStateMethod(View view) {
        String callStateContext = new String();
        callStateContext = ContextAssigner.SCREEN_STATE + "=" +selectedScreenState;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ContextAssigner.SCREEN_STATE, callStateContext);
        setResult(ContextAssigner.SCREEN_STATE_SELECTOR, returnIntent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.screenStateSpinner)
            selectedScreenState = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected (AdapterView < ? > parent){
        parent.setSelection(0);
    }
}
