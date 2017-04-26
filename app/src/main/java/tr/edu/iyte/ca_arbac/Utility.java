package tr.edu.iyte.ca_arbac;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by john on 06.09.2015.
 */
public class Utility {
    private static final String TAG5 = "Test Message";

    public static ArrayList<String> getAllAndroidPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PermissionInfo> permissions = new ArrayList<>();

        List<PermissionGroupInfo> groupList = pm.getAllPermissionGroups(0);
        groupList.add(null); // ungrouped permissions

        for (PermissionGroupInfo permissionGroup : groupList) {
            String name = permissionGroup == null ? null : permissionGroup.name;
            try {
                permissions.addAll(pm.queryPermissionsByGroup(name, 0));
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        ArrayList<String> permissionList = new ArrayList<>();
        for(PermissionInfo permissionInfo:permissions){
            if(permissionInfo.name.contains("android.permission")){
                String str=permissionInfo.name.replace("android.permission.","");
                permissionList.add(str);

            }

        }
        Collections.sort(permissionList, String.CASE_INSENSITIVE_ORDER);
        for(String str:permissionList)
            Log.i(TAG5, str);
        Log.i(TAG5, String.valueOf(permissionList.size()));
        return permissionList;
    }
}
