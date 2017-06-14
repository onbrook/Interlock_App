package com.oep.interlock_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by: Peter Lewis
 * Date: 6/10/17
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent1){
        Intent intent = new Intent(context, DatabaseManagement.class);
        intent.putExtra("getTime", true);
        PendingIntent pi = PendingIntent.getActivity(context, 01234, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.clock)
                .setContentTitle("Actual time")
                .setContentText("At least one job needs to have the actual time entered.")
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01234, mBuilder.build());
    }
}
