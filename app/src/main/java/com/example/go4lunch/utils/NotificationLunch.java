package com.example.go4lunch.utils;

import static com.example.go4lunch.ui.restaurantdetail.RestaurantDetailActivity.SHARED_PREFERENCES_RESTAURANT_ID;
import static com.example.go4lunch.utils.IntentHelper.RESTAURANT_CLICKED;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.ui.coworker.CoworkerFragment;
import com.example.go4lunch.ui.restaurantdetail.RestaurantDetailActivity;
import com.google.gson.Gson;

import java.util.Objects;


public class NotificationLunch extends BroadcastReceiver {
    public final static Integer notificationID = 1;
    public final static String channelID = "channel1";
    public final static String titleExtra = "titleExtra";
    public final static String messageExtra = "messageExtra";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences =  context.getSharedPreferences(SHARED_PREFERENCES_RESTAURANT_ID, Context.MODE_PRIVATE);
        String restaurantJson = preferences.getString(SHARED_PREFERENCES_RESTAURANT_ID, null);
        Gson gson = new Gson();
        RestaurantEntity restaurant = gson.fromJson(restaurantJson, RestaurantEntity.class);
        Intent resultIntent;
        if(restaurant != null) {
            resultIntent = new Intent(context, RestaurantDetailActivity.class);
        } else {
            resultIntent = new Intent(context, CoworkerFragment.class);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if(restaurant!=null){
            Objects.requireNonNull(stackBuilder.editIntentAt(0)).putExtra(RESTAURANT_CLICKED, restaurant);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.baseline_restaurant_24)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);


        Intent notifyIntent = new Intent(context, RestaurantDetailActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, notification.build());
    }
}
