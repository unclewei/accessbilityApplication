package com.example.unclewei.accessbilityapplication.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 *
 * @author zzb
 * @date 2017/12/22
 */

public class UploadAlarmUtil {

    public static void invokeRefreshAlarmManager(Context context){
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY,7);
        //1分钟
        int interval=1000*60*1;
        long triggerAtTime = System.currentTimeMillis();
        Intent i = new Intent(context, RefreshRecevier.class);
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,interval, pi);
    }
    public static void cancleRefreshAlarmManager(Context context){
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent i = new Intent(context,RefreshRecevier.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        manager.cancel(pi);
    }


}
