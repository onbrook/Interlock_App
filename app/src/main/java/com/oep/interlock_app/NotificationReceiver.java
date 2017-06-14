package com.oep.interlock_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
                .setContentTitle("Actual time")
                .setContentText("Tap to enter the actual time for jobs.")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.clock)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("There are pending jobs that need to have the actual time entered" +
                                " to improve future estimations.\nTap to enter."));

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01234, mBuilder.build());
    }
}
