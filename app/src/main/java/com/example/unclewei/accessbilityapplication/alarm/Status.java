package com.example.unclewei.accessbilityapplication.alarm;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author unclewei
 * @Data 2018/2/12.
 */

public class Status {


    static final public String  friendsGroup = "微信群";
    static final public String snsCricle = "朋友圈";
    static final public String scanGrouperData = "获取群成员信息";


    static public String KEY="";
    static public String IMEI="";
    static public String UIN="";
    static public long snsLastTime=0;
    static public long msgLastTime=0;
    static public long snsLastExportTime=0;
    static public long msgLastExportTime=0;
    static public String FOLDER="";
    static public String CLICK_TEXT="XXXX";
    static public int REFRESH_COUNT=-1;
    static public boolean isRan=false;
    static public boolean isFailed=false;
    static public int failedTimes=0;
    static public int MaxRetryTimes=10;
    public static void saveToFile(String fileName,String... Msg){
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.i("Error", "exception", e);
            }
        }
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            for(String msg:Msg){
                bw.write(msg);
            }
            bw.close();
        } catch (IOException e) {
            Log.i("Error", "exception", e);
        }
    }

}
