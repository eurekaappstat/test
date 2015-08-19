package com.example.manjunathnb.appstats;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/**
 * Created by manjunathnb on 7/13/2015.
 */
public class AppMoniterService extends Service {
    public static final String ID = "_id";
    public static final String APP_PACKAGE = "app_package";
    public static final String LAST_APP_LAUNCHED_TIME = "last_app_launched_time";
    public static final String LAST_APP_EXITED_TIME = "last_app_exited_time";

    ActivityManager mActivityManager;
    private AppMoniterThread mAppMoniterThread;
    String previousPackageName = "";
    long previousinsertedID = 0;

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
        mAppMoniterThread = new AppMoniterThread(this);
        mAppMoniterThread.start();
        mAppMoniterThread.setName("AppMoniterThread");
    }
}


