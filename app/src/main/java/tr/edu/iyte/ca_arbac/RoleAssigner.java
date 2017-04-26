package tr.edu.iyte.ca_arbac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RoleAssigner extends ActionBarActivity {
    private static final String TAG5 = "Test Message";
    private ListView lView;
    private Button button;
    String[] appListArray;
    List<ApplicationInfo> packages;

    private ArrayList<String> userInstalledAppNames = new ArrayList<String>();
    //private ArrayList<String> userInstalledAppPackages = new ArrayList<String>();
    int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_assigner);
       // Log.i(TAG5, "inside onCreate method of RoleAssigner");

        lView = (ListView) findViewById(R.id.appList);
       final PackageManager pm = getPackageManager();

        packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo appInfo:packages){
            if(isUserInstalledApp(appInfo)){
                //Log.i(TAG5, "App ID:"+appInfo.uid);
                //Log.i(TAG5, "App package:"+appInfo.packageName);
                userInstalledAppNames.add(appInfo.loadLabel(pm).toString());
            }
        }

        appListArray = new String[userInstalledAppNames.size()];
        appListArray = userInstalledAppNames.toArray(appListArray);
        CustomAdapter customAdapter = new CustomAdapter(this,appListArray);
        lView.setAdapter(customAdapter);


   }


    private boolean isUserInstalledApp(ApplicationInfo applicationInfo) {
        if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
        {
            // IS A SYSTEM APP
            return false;
        }

        else if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
        {
            // APP WAS INSTALL AS AN UPDATE TO A BUILD-IN SYSTEM APP
            return false;
        }
        else {
            return true;
        }
        // return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }


    public List<ApplicationInfo> getApplications(){

        List<ApplicationInfo> appsInfo=new ArrayList<>();
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();

        PackageManager pm = getApplicationContext().getPackageManager();

        if(pm!=null)
            appsInfo = pm.getInstalledApplications(0);

        for(ApplicationInfo appInfo : appsInfo) {

            Log.i(TAG5, appInfo.packageName);

        }
        return installedApps;
    }

    class CustomAdapter extends ArrayAdapter<String> {

        Context context;
        String[] installedApps;
        public CustomAdapter(Context c, String[] installedAppList) {
            super(c, R.layout.single_row, R.id.applicationName, installedAppList);
            this.context=c;
            this.installedApps=installedAppList;
        }

        @Override
        public View getView(int position, View view, final ViewGroup parent) {
            //Log.i(TAG5, "inside getView method of RoleAssigner");
            View row=view;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row, parent, false);
            }

            TextView appName= (TextView) row.findViewById(R.id.applicationName);
            Button button =(Button)row.findViewById(R.id.roleAssignButton);


            appName.setText(installedApps[position]);
            //button.setText("Assign");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    assignRoleMethod(view);

                }
            });

            return row;
        }
    }


  public void assignRoleMethod(View v){
      if(v.getId()==R.id.roleAssignButton){
          int appID=0;
          String appName=null;
          final PackageManager pm = getPackageManager();

          View parentRow = (View) v.getParent();
          ListView listView = (ListView) parentRow.getParent();
          int position = listView.getPositionForView(parentRow);
          appName=appListArray[position];
          // TextView t= (TextView) v.findViewById(R.id.applicationName);
          // appName=t.toString();

          for(ApplicationInfo appInfo:packages){
              if(appName.equals(appInfo.loadLabel(pm).toString())){
                  appID=appInfo.uid;
              }
          }
          Intent i= new Intent(this,RoleAssignerHelper.class);
          i.putExtra("APP_ID",String.valueOf(appID));
          i.putExtra("APP_NAME",appName);
          startActivity(i);
         // Log.i(TAG5, "Intent sent to RoleAssignerHelper");
      }

    }

}

