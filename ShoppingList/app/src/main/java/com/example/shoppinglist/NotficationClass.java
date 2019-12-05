package com.example.shoppinglist;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class NotficationClass extends BroadcastReceiver {

    int count = 0;

    Context sendContext;

    private void notification() {

        Date currentDate = Calendar.getInstance().getTime();
        Log.i("date", String.valueOf(currentDate));


        Cursor cursor = MainActivity.shoppingList.rawQuery("SELECT * FROM mainList WHERE reminder = ? ", new String[]{"true"});

        int listNameInd = cursor.getColumnIndex("name");
        int categoryInd = cursor.getColumnIndex("category");
        int monthInd = cursor.getColumnIndex("month");
        int dateInd = cursor.getColumnIndex("date");
        int dayInd = cursor.getColumnIndex("day");
        int timeInd = cursor.getColumnIndex("time");

        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {

            String listName = cursor.getString(listNameInd);
            String category = cursor.getString(categoryInd);
            String month = cursor.getString(monthInd);
            String date = cursor.getString(dateInd);
            String day = cursor.getString(dayInd);
            String time = cursor.getString(timeInd);

            int timeInt = Integer.parseInt(time.substring(0, 2));

            if ((timeInt == currentDate.getHours()) && (currentDate.getMinutes() == 0)) {

                switch (category) {

                    case "Monthly":
                        if (currentDate.getDate() == Integer.parseInt(date)) {
                            sendNotification(listName);
                        }
                        break;

                    case "Weekly":
                        int dayInt = caluculateDay(day);

                        if (currentDate.getDay() == dayInt) {
                            sendNotification(listName);
                        }
                        break;

                    case "Daily":
                        sendNotification(listName);
                        break;

                    case "One Time":
                        if (currentDate.getDate() == Integer.parseInt(date)) {

                            int monthInt = caluculateMonth(month);

                            if ((currentDate.getMonth() == monthInt)) {
                                sendNotification(listName);
                            }
                        }

                        break;
                }

            }

            cursor.moveToNext();
        }
    }

    private int caluculateDay(String day) {

        switch (day) {
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
            case "Saturday":
                return 6;
            case "Sunday":
                return 7;
            default:
                return 0;
        }
    }

    private int caluculateMonth(String month) {

        switch (month) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
            default:
                return 12;
        }
    }

    private void sendNotification(String listName) {

        String CHANNEL_ID = "id";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(sendContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_shopping_cart)
                .setContentTitle("Its time to shop! - " + listName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification";
            String description = "notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) sendContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(MainActivity.notificationId, builder.build());
            MainActivity.notificationId = MainActivity.notificationId + 1;
        }

        updateDB(listName);
    }

    private void updateDB(String listName) {

        ContentValues cv = new ContentValues();
        cv.put("strike", "false");

        MainActivity.shoppingList.update("itemList", cv, "listName = ?", new String[]{listName});

    }


    @Override
    public void onReceive(Context context, Intent intent) {

            sendContext = context;
            notification();
    }
}
