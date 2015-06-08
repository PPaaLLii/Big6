package sk.upjs.ics.android.big6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Pavol on 8. 6. 2015.
 */

//http://www.jjoe64.com/2011/06/autostart-service-on-device-boot.html
public class BootCompletedIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            RemindTrainingSchedule.schedule(context, sharedPreferences);
        }
    }
}
