package sk.upjs.ics.android.big6;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pavol on 28. 4. 2015.
 */
public class RemindTrainingSchedule {

    private static final int SERVICE_REQUEST_CODE = 0;
    private static final int NO_FLAGS = 0;
    private static PendingIntent[] pendingIntent = new PendingIntent[7];
    private static Context context;

    public static void schedule(Context context) {

        //http://stackoverflow.com/questions/4562757/alarmmanager-android-every-day
        //http://stackoverflow.com/questions/15677669/how-to-set-one-time-alarm-in-android

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isEnabled = sharedPreferences.getBoolean("enableReminders", false);
        if (!isEnabled){
            unSchedule(context);
            return;
        }

        boolean[] alarmsForDays = new boolean[7];

        Set<String> days = sharedPreferences.getStringSet("selectedDays", null);
        if (days != null) {
            for (String day : days) {
                if (!day.equals("7")) { //older indexing: Monday=1, Sunday=7 new:Monday=0, Sunday=6
                    alarmsForDays[Integer.parseInt(day)] = true;
                }
            }
        }

        int hour = sharedPreferences.getInt("reminderTime.hour", 0);
        int minute = sharedPreferences.getInt("reminderTime.minute", 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        unSchedule(context);

        Log.i("", "scheduling!!! ");
        for(int i=0; i<alarmsForDays.length; i++){
            if(alarmsForDays[i]){
                Calendar calendar = Calendar.getInstance();
                //Log.d("TIME: ", String.valueOf(calendar.getTime().toString()));
                int dayOfTheWeekToday = calendar.get(Calendar.DAY_OF_WEEK);
                int diff = (getWantedDayOfTheWeek(i) - dayOfTheWeekToday);
                if(diff < 0){
                    diff+=7;
                }
                //Log.d("diff: ", String.valueOf(diff));
                calendar.add(Calendar.DATE, diff);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Date date = calendar.getTime();
                Log.d("callendar time: ", date.toString());
                Long millis = calendar.getTimeInMillis();
                Intent intent = new Intent(context, RemindTrainingService.class);
                pendingIntent[i] = PendingIntent.getService(context, SERVICE_REQUEST_CODE, intent, NO_FLAGS);
                long weekMillis = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, millis, weekMillis, pendingIntent[i]);
            }
        }
    }

    public static void unSchedule(Context context){
        Log.i("" , "unScheduling old alarms!!!");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i=0; i<pendingIntent.length; i++){
            if(pendingIntent[i] != null){
                alarmManager.cancel(pendingIntent[i]);
                pendingIntent[i]= null;
            }
        }
    }

    public static int getWantedDayOfTheWeek(int day){
        switch(day){
            case 0:
                return Calendar.MONDAY;
            case 1:
                return Calendar.TUESDAY;
            case 2:
                return Calendar.WEDNESDAY;
            case 3:
                return Calendar.THURSDAY;
            case 4:
                return Calendar.FRIDAY;
            case 5:
                return Calendar.SATURDAY;
            case 6:
                return Calendar.SUNDAY;
        }
        return 0;
    }
}
