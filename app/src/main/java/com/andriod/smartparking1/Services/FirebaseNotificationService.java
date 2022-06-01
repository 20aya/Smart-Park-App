package com.andriod.smartparking1.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.andriod.smartparking1.R;
import com.bumptech.glide.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FirebaseNotificationService extends FirebaseMessagingService {
FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
String userid= firebaseAuth.getUid();

//everytime we recive anotifacation from firebase this method called
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage message) {
        super.onMessageReceived(message);
        //to let the notifcation even when the app is not closed ot in the bacckground
   String title = message.getNotification().getTitle();
   String body = message.getNotification().getBody();
   final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
        NotificationChannel channel= new NotificationChannel(CHANNEL_ID ,
                "MyNotification",
                NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
//now its linked we need to build a notifcation builder
        Notification.Builder notification =  new Notification.Builder(this , CHANNEL_ID)
                .setContentTitle(title).setContentText(body).setSmallIcon(R.drawable.drive).setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, notification.build());



    }

    @Override
    public void onNewToken(@NonNull @NotNull String token) { // will be called when new token
                                                          // is change or generate then we update that token in userd data.
        updateToken(token);

        super.onNewToken(token);
    }

    private void updateToken(String token){
        if (userid!= null) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
            Map<String, Object> map = new HashMap<>();

            map.put("token", token);
            databaseReference.updateChildren(map);
        }
    }//end updatetoken
}//end class
