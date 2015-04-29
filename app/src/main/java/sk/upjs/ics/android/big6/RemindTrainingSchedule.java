package sk.upjs.ics.android.big6;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Pavol on 28. 4. 2015.
 */
public class RemindTrainingSchedule {

    private static final int SERVICE_REQUEST_CODE = 0;
    private static final int NO_FLAGS = 0;

    public static void schedule(Context context) {

        //http://stackoverflow.com/questions/4562757/alarmmanager-android-every-day
        //http://stackoverflow.com/questions/15677669/how-to-set-one-time-alarm-in-android

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 49);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RemindTrainingService.class);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, SERVICE_REQUEST_CODE, intent, NO_FLAGS);

        System.out.println(calendar.getTimeInMillis());
        System.out.println(AlarmManager.RTC);

        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }
}
