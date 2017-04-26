package tr.edu.iyte.ca_arbac;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity{
   Button roleCreatorButton;
   Button roleAssignerButton;
    private static final String TAG5 = "Test Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //roleCreatorButton= (Button) findViewById(R.id.createRoleButton);
       // roleAssignerButton = (Button) findViewById(R.id.assignRoleButton);
       // roleCreatorButton.setOnClickListener(this);
       // roleAssignerButton.setOnClickListener(this);
    }


    public void buttonHandleMethod(View v) {


        if(v.getId()==R.id.createRoleButton){
            Intent i= new Intent(this,RoleCreator.class);
            startActivity(i);
           // Log.i(TAG5, "intent sent to role creator");
        }
        else if(v.getId()==R.id.assignRoleButton){
         Intent intent= new Intent(this,RoleAssigner.class);
           startActivity(intent);
           // Log.i(TAG5, "intent sent to role assigner");
        }
        else if(v.getId()==R.id.modifyRoleButton){
            Intent intent= new Intent(this,RoleModifier.class);
            startActivity(intent);
           // Log.i(TAG5, "intent sent to role assigner");
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
