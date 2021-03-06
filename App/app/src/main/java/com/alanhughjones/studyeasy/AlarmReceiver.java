package com.alanhughjones.studyeasy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "com.singhajit.notificationDemo.channelId";
    // TODO change above Channel to different name
    public static final String Name = "nofifID";

    @Override
    public void onReceive(Context context, Intent intent) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        SharedPreferences prefs = context.getSharedPreferences("mypref",Context.MODE_PRIVATE);
        String sID = prefs.getString(Name,"0");
        int notifID = Integer.parseInt(sID);

        Intent notificationIntent = new Intent(context, SubjectListActivity.class);
        String subjectName = intent.getStringExtra("subject");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SubjectListActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notifID, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("Study Easy")
                .setContentText("Upcoming task for " + subjectName)
                .setTicker("Upcoming task for " + subjectName)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[] {100,250,100,250})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notifID, notification);
    }
}
