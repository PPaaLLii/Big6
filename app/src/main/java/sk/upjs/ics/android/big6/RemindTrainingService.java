package sk.upjs.ics.android.big6;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by Pavol on 28. 4. 2015.
 */
public class RemindTrainingService extends IntentService {

    public static final String WORKER_THREAD_NAME = "sk.upjs.ics.android.big6.RemindTrainingService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RemindTrainingService() {
        super(WORKER_THREAD_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(getClass().getName(), "RemainTrainingService created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(getClass().getName(), "setting reminders...");
        triggerNotification();
    }

    @Override
    public void onDestroy() {

        Log.i(getClass().getName(), "RemainTrainingService destroyed");

        super.onDestroy();
    }

    private void triggerNotification() {

        //http://developer.android.com/training/notify-user/build-notification.html
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Train!")
                .setContentText("You should train now!")
                .setContentIntent(resultPendingIntent)
                .setTicker("Big6")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify("Big6", 0, notification);
    }
}
