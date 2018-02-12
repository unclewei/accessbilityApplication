package com.example.unclewei.accessbilityapplication.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author zzb
 * @date 2017/12/26
 */

public class RefreshRecevier extends BroadcastReceiver {
    String TAG = "RefreshRecevier";
    public static int CLEAR_RECOED_INTERVAL = 14400;
    public static int ADD_FRIENDS_INTERVAL = 1440;
    public static int RED_ENVELOPE_INTERVAL = 240;
    public static int INTERVAL = 15;

    /**
     * 不同时段执行不同脚本
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Status.isFailed = false;
        Status.REFRESH_COUNT++;
        Log.e("william", "现在的时间数为 ： " + Status.REFRESH_COUNT % INTERVAL);
        Status.failedTimes = 0;
        switch (Status.REFRESH_COUNT % INTERVAL) {
            case 14:
                Log.i(TAG, "微信信息上传");
                Toast.makeText(context, "微信信息上传", Toast.LENGTH_LONG);
                //TODO:上传操作
                break;
            case 13:
                startWeChat(context, Status.scanGrouperData);
                break;
            case 3:
                startWeChat(context, Status.snsCricle);
                break;
            case 1:
                startWeChat(context, Status.friendsGroup);
                break;
            default:
                break;
        }
    }

    void startWeChat(Context context, String TaskName) {
        Log.i(TAG, "执行：  " + TaskName);
        Status.isRan = false;
        Status.CLICK_TEXT = TaskName;
        RefreshUtil.startWeChat(context);
    }

}
