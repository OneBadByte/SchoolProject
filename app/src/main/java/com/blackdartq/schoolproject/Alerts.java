package com.blackdartq.schoolproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

class Alerts extends DBUtils {
    NotificationChannel notificationChannel;
    NotificationManager notificationManager;
    Context context;
    Alerts(Context context, NotificationManager notificationManager){
        this.context = context;
        this.notificationManager = notificationManager;
        notificationChannel = new NotificationChannel("80085", "FOLLOWER", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationChannel.enableVibration(true);
        System.out.println("Dates Below");
        checkCurrentDateAgainstAssignmentDueDates();
    }

    String getCurrentDateInCorrectFormat(){
        DateFormat df = new SimpleDateFormat("M/d/yyyy");
        String output = df.format(Calendar.getInstance().getTime());
        return output;
    }

    void checkCurrentDateAgainstAssignmentDueDates(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String currentDate = getCurrentDateInCorrectFormat();
                    HashMap<String, String> assignmentDates = getAssignmentDates();
                    for(String id: assignmentDates.keySet()){
                        if(currentDate.equals(assignmentDates.get(id))){
                            String name = getAssignmentNameById(Integer.parseInt(id));
                            sendNotification("Assignment is due today!", name + " is due today");
                        }
                    }
                    try {
                        Thread.sleep(120*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        thread.start();
    }


    void sendNotification(String title, String text){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context, "80085")
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new Notification.BigTextStyle().bigText(text))
                .setSmallIcon(R.mipmap.moblie_application_class_icon)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent, true)
                .build();
        notificationManager.notify(1, notification);

    }
}
