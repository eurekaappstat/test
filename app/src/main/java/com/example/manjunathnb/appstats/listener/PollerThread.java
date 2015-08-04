package com.example.manjunathnb.appstats.listener;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import java.util.List;

/**
 * Created by manjunathnb on 7/7/2015.
 */
public class PollerThread extends Thread {
    ActivityManager am = null;
    Context context = null;

    public PollerThread(Context con) {
        context = con;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void run() {


        Looper.prepare();

        while (true) {
            // Return a list of the tasks that are currently running,
            // with the most recent being first and older ones after in order.
            // Taken 1 inside getRunningTasks method means want to take only
            // top activity from stack and forgot the olders.
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

            String currentRunningActivityName = taskInfo.get(0).topActivity.getClassName();

            if (currentRunningActivityName.equals("PACKAGE_NAME.ACTIVITY_NAME")) {
                // show your activity here on top of PACKAGE_NAME.ACTIVITY_NAME
            }

            Log.i("manju", "Top activity info is : " + currentRunningActivityName);
        }
        //Looper.loop();
    }

}
