package com.oep.interlock_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by: Peter Lewis
 * Date: 6/10/17
 */

public class NotificationReceiver extends Service {
    Intent intent;
    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        return null;
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, HomeScreen.class);
        PendingIntent pi = PendingIntent.getActivity(this, 01234, intent, 0);
        long[] pattern = {0, 300, 0};
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.right_arrow)
                .setContentTitle("Actual time")
                .setContentText("At least one job needs to have the actual time entered.")
                .setVibrate(pattern)
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01234, mBuilder.build());
    }
}
