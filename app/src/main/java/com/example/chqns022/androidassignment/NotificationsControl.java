package com.example.chqns022.androidassignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationsControl {

    public static void sendNotifications(final Context context, String title, String messaqge){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                .setSmallIcon(R.drawable.ic_baseline_info_24px)
                .setContentTitle(title)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                .setContentText(messaqge);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(001, builder.build());

    }

    public static void createToast(final Context context, String msg){
        Toast.makeText(context,
                msg,
                Toast.LENGTH_SHORT).show();
    }

}
