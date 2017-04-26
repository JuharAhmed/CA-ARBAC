package tr.edu.iyte.ca_arbac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by john on 07.06.2015.
 */
public class ContextManagerStarter extends BroadcastReceiver {
    private static final String TAG5 = "Test Message";
    @Override
    public void onReceive(Context context, Intent intent) {

       if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
           Intent i = new Intent(context, tr.edu.iyte.ca_arbac.ContextManager.class);
            context.startService(i);
          // Log.i(TAG5,"Inside onReceive method of ContextManagerStarter");
        }
    }

}
