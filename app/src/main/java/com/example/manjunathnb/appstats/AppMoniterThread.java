package com.example.manjunathnb.appstats;

import java.util.List;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.manjunathnb.appstats.database.AppStatConstants;
import com.example.manjunathnb.appstats.database.DBManager;

    class AppMoniterThread extends Thread {

        private final AppMoniterService appMonitorService;

        AppMoniterThread(AppMoniterService appMoniterService) {
            appMonitorService = appMoniterService;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String foregroundTaskPackageName;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    ActivityManager.RunningTaskInfo foregroundTaskInfo = appMonitorService.mActivityManager.getRunningTasks(1).get(0);
                    foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
                } else {
                    ActivityManager manager = (ActivityManager) appMonitorService.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
                    foregroundTaskPackageName = tasks.get(0).processName;
                }

                if (foregroundTaskPackageName.equals(appMonitorService.previousPackageName)) {
				/*    Log.i("manju :", " top package is same as previous package: TOP package :" + foregroundTaskPackageName + " and  previous package is" +
                        previousPackageName);*/
                    continue;
                } else {
                    updateDatabase(foregroundTaskPackageName);
                }
            }
        }

        private void updateDatabase(String foregroundTaskPackageName) {
            long insertedID = 0;
            Log.i("manju :", " top package is NOT same as previous package: top package :" + foregroundTaskPackageName + " and  previous package is" +
                    appMonitorService.previousPackageName);
            DBManager db = DBManager.getInstance(appMonitorService.getApplicationContext());
            db.getWritableDB();

            if (isRequiredApp(foregroundTaskPackageName)) {
                ContentValues cv = new ContentValues();
                cv.put(AppMoniterService.LAST_APP_LAUNCHED_TIME, System.currentTimeMillis());
                cv.put(AppMoniterService.APP_PACKAGE, foregroundTaskPackageName);
                insertedID = db.insert(AppStatConstants.APP_INFO_TABLE, cv);
                Log.i("manju", " package inserted is :" + foregroundTaskPackageName + " inserted id is : " + insertedID);
                Log.i("manju", "app launched is : " + foregroundTaskPackageName + " at time : " + System.currentTimeMillis());
            }
            if (isRequiredApp(appMonitorService.previousPackageName)) {
                ContentValues cvU = new ContentValues();
                cvU.put(AppMoniterService.LAST_APP_EXITED_TIME, System.currentTimeMillis());
                long updatedID = db.update(AppStatConstants.APP_INFO_TABLE, cvU, AppMoniterService.ID + " = " + appMonitorService.previousinsertedID, null);
                Log.i("manju ", "where clause was : " + AppMoniterService.ID + " = " + appMonitorService.previousinsertedID + " AND " + AppMoniterService.APP_PACKAGE + " = '" + appMonitorService.previousPackageName + "'");
                Log.i("manju", "app exited is : " + appMonitorService.previousPackageName + " at time :" + System.currentTimeMillis());
                Log.i("manju :", " return of update  :" + updatedID);
            }
            appMonitorService.previousinsertedID = insertedID;
            appMonitorService.previousPackageName = foregroundTaskPackageName;
        }


        private boolean isRequiredApp(String appPackage) {
       /* return appPackage.equalsIgnoreCase("com.facebook.katana")
                || appPackage.equalsIgnoreCase("com.whatsapp")
                || appPackage.equalsIgnoreCase("com.android.contacts");*/
            return (!appPackage.equalsIgnoreCase("appstats.com.appstats"));
        }
}