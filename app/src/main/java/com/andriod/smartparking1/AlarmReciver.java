package com.andriod.smartparking1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReciver extends BroadcastReceiver {


    //whenever a particular notif is pushed this method will work
    @Override
    public void onReceive(Context context, Intent intent) {

        // if the user tapped on the notifaction there should be a new activity shown
        Intent i = new Intent(context , LeavingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0, i ,0);

     // create notifaction :
        androidx.core.app.NotificationCompat.Builder builder = new NotificationCompat.Builder( context , "foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("foxandroid alaemmm").setContentText("lolollolololololololo")
                .setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
        /// we finished creation notifaction

    }
}// end of class
