package com.example.manjunathnb.appstats;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.manjunathnb.appstats.database.AppStatConstants;
import com.example.manjunathnb.appstats.database.DBManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by manjunathnb on 7/13/2015.
 */
public class AppMoniterService extends Service {
    public static final String ID = "_id";
    public static final String APP_PACKAGE = "app_package";
    public static final String LAST_APP_LAUNCHED_TIME = "last_app_launched_time";
    public static final String LAST_APP_EXITED_TIME = "last_app_exited_time";

    private static Set<String> packageSet = new HashSet<String>();
    private ActivityManager mActivityManager;
    private AppMoniterThread mAppMoniterThread;
    private String previousPackageName = "";
    private long previousinsertedID = 0;

    @Override
    public void onCreate() {
        startThread();
        mActivityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        Log.i("manju", "onCreate of service");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("manju", "onStartCommand of service");
        return START_STICKY;
    }


    private void stopThread() {
        if (mAppMoniterThread != null) {
            mAppMoniterThread.interrupt();
            mAppMoniterThread = null;
        }
    }

    private void startThread() {
        stopThread();
        mAppMoniterThread = new AppMoniterThread();
        mAppMoniterThread.start();
        mAppMoniterThread.setName("AppMoniterThread");
    }

    private class AppMoniterThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String foregroundTaskPackageName, foregroundTaskActivityName;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    ActivityManager.RunningTaskInfo foregroundTaskInfo = mActivityManager.getRunningTasks(1).get(0);

                    foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
                    foregroundTaskActivityName = foregroundTaskInfo.topActivity.getShortClassName();
                } else {
                    ActivityManager manager = (ActivityManager) AppMoniterService.this.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
                    foregroundTaskPackageName = tasks.get(0).processName;
                    foregroundTaskActivityName = "";
                }

                if (foregroundTaskPackageName.equals(previousPackageName)) {
                /*    Log.i("manju :", " top package is same as previous package: TOP package :" + foregroundTaskPackageName + " and  previous package is" +
                            previousPackageName);*/
                    continue;
                } else {
                    long insertedID = 0;
                    Log.i("manju :", " top package is NOT same as previous package: top package :" + foregroundTaskPackageName + " and  previous package is" +
                            previousPackageName);
                    DBManager db = DBManager.getInstance(getApplicationContext());
                    db.getWritableDB();

                    if ((foregroundTaskPackageName.equalsIgnoreCase("com.facebook.katana")
                            || foregroundTaskPackageName.equalsIgnoreCase("com.whatsapp")
                            || foregroundTaskPackageName.equalsIgnoreCase("com.android.contacts"))
                            ) {
                        ContentValues cv = new ContentValues();
                        cv.put(LAST_APP_LAUNCHED_TIME, System.currentTimeMillis());
                        cv.put(APP_PACKAGE, foregroundTaskPackageName);
                        insertedID = db.insert(AppStatConstants.APP_INFO_TABLE, cv);
                        Log.i("manju", " package inserted is :" + foregroundTaskPackageName + " inserted id is : " + insertedID);
                        Log.i("manju", "app launched is : " + foregroundTaskPackageName + " at time : " + System.currentTimeMillis());
                    }

                    if (previousPackageName.equalsIgnoreCase("com.facebook.katana")
                            || previousPackageName.equalsIgnoreCase("com.whatsapp")
                            || previousPackageName.equalsIgnoreCase("com.android.contacts")) {
                        ContentValues cvU = new ContentValues();
                        cvU.put(LAST_APP_EXITED_TIME, System.currentTimeMillis());
                        int updatedID = db.update(AppStatConstants.APP_INFO_TABLE, cvU, ID + " = " + previousinsertedID /* + " AND " + APP_PACKAGE + " = '" + previousPackageName + "'"*/, null);
                        Log.i("manju ", "where clause was : " + ID + " = " + previousinsertedID + " AND " + APP_PACKAGE + " = '" + previousPackageName + "'");
                        Log.i("manju", "app exited is : " + previousPackageName + " at time :" + System.currentTimeMillis());
                        Log.i("manju :", " return of update  :" + updatedID);
                    }
                    previousinsertedID = insertedID;
                    previousPackageName = foregroundTaskPackageName;
                }


            }

        }

        private void performDBOperations(String foregroundTaskPackageName) {
            long insertedID = 0;
            DBManager db = DBManager.getInstance(getApplicationContext());




            /*if ((foregroundTaskPackageName.equalsIgnoreCase("com.facebook.katana")
                    || foregroundTaskPackageName.equalsIgnoreCase("com.whatsapp")
                    || foregroundTaskPackageName.equalsIgnoreCase("com.android.contacts") )
                    ){

                db.getWritableDB();
                ContentValues cv = new ContentValues();
                cv.put(LAST_APP_LAUNCHED_TIME, System.currentTimeMillis());
                cv.put(APP_PACKAGE, foregroundTaskPackageName);
                insertedID = db.insert(AppStatConstants.APP_INFO_TABLE, cv);
                Log.i("manju", " package inserted is :" + foregroundTaskPackageName + " inserted id is : " + insertedID);
                Log.i("manju", "app launched is : " + foregroundTaskPackageName + " at time : " + System.currentTimeMillis());
                previousinsertedID = insertedID;

            }
            if (previousinsertedID > 0 &&
                    ( previousPackageName.equalsIgnoreCase("com.facebook.katana")
                    || previousPackageName.equalsIgnoreCase("com.whatsapp")
                    || previousPackageName.equalsIgnoreCase("com.android.contacts"))) {

                db.getWritableDB();
                ContentValues cvU = new ContentValues();
                cvU.put(LAST_APP_EXITED_TIME, System.currentTimeMillis());
                Log.i("manju ", "where clause was : " + ID + " = " + previousinsertedID + " AND " + APP_PACKAGE + " = '" + previousPackageName + "'");
                Log.i("manju", "app exited is : " + previousPackageName + " at time :" + System.currentTimeMillis());
                int updatedID = db.update(AppStatConstants.APP_INFO_TABLE, cvU, ID + " = " + previousinsertedID *//* + " AND " + APP_PACKAGE + " = '" + previousPackageName + "'"*//*, null);
                Log.i("manju :", " return of update  :" + updatedID);
                previousinsertedID = -1;
            }
*/
            previousPackageName = foregroundTaskPackageName;
            try {
                PackageInfo p = getApplicationContext().getPackageManager().getPackageInfo(
                        foregroundTaskPackageName, PackageManager.GET_PERMISSIONS);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}


