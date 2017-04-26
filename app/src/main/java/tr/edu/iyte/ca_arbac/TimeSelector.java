package tr.edu.iyte.ca_arbac;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Calendar;
import android.app.Activity;
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Bundle;

import java.util.ArrayList;

public class TimeSelector extends ListActivity{

    ListView weekDays;
    String []weekDaysList;
    ArrayAdapter<String> adapter;
    ArrayList<String> selectedDays= new ArrayList<>();
    String startTime = new String();
    String endTime= new String();
    private static final String TAG5 = "Test Message";
    Calendar calendar = Calendar.getInstance();

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG5, "Inside Time selector");
        setContentView(R.layout.activity_time_selector);
       weekDays=getListView();
       Resources r=getResources();
       weekDaysList=r.getStringArray(R.array.weekDays);

       weekDays.setChoiceMode(weekDays.CHOICE_MODE_MULTIPLE);
       weekDays.setTextFilterEnabled(true);
       adapter = new ArrayAdapter<String>(this,
             android.R.layout.simple_list_item_checked, weekDaysList);
        weekDays.setAdapter(adapter);
        Log.i(TAG5, "Inside Time selector1");

         Button startTimeBtn = (Button) findViewById(R.id.timeButton1);
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(TimeSelector.this, startTimeListener, calendar
                        .get(Calendar.HOUR_OF_DAY), calendar
                        .get(Calendar.MINUTE), true).show();
            }
        });

        Button endTimeBtn = (Button) findViewById(R.id.timeButton2);
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(TimeSelector.this, endTimeListener, calendar
                        .get(Calendar.HOUR_OF_DAY), calendar
                        .get(Calendar.MINUTE), true).show();
            }
        });
    }

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startTime=String.valueOf(hourOfDay)+String.valueOf(minute);
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endTime=String.valueOf(hourOfDay)+String.valueOf(minute);
        }
    };


    public void onListItemClick(ListView parent, View v, int position, long id) {
        selectedDays.add(weekDaysList[position]);

    }

   public void returnSelectedTime(View view){
       String timeContext=new String();
       String selectedWeekdays=new String();
       int counter=0;
       for(String s:selectedDays){
           counter++;
           if(counter==selectedDays.size()){
            selectedWeekdays=selectedWeekdays + s;
           }
           else
            selectedWeekdays=selectedWeekdays + s + ",";


       }

         timeContext= ContextAssigner.TIME +"="+startTime+";"+endTime+";"+selectedWeekdays;
         Intent returnIntent = new Intent();
         returnIntent.putExtra(ContextAssigner.TIME,timeContext);
         setResult(ContextAssigner.TIME_SELECTOR, returnIntent);
        this.finish();
    }

}
