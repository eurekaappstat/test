package com.example.manjunathnb.appstats;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.manjunathnb.appstats.database.AppStatConstants;
import com.example.manjunathnb.appstats.database.DBManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("manju :MainActiity" ,  "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("manju :MainActiity" ,  "onStart");
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd : hh:mm:ss");

        startService(new Intent(this , AppMoniterService.class));

        DBManager db = DBManager.getInstance(this);
        db.getReadableDB();
        Cursor cursor = db.query(AppStatConstants.APP_INFO_TABLE,null, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()  && cursor.getCount() > 0){
            do {
                TextView valueTV = new TextView(this);
                valueTV.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                int rowId = cursor.getInt(0);
                long startTime = cursor.getLong(1);
                long endTime = cursor.getLong(2);
                String packageName = cursor.getString(3);
               /* Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startTime);*/
                Date ds =  new Date(startTime);
                Date de =  new Date(endTime);
                long tDiffInsec = ((endTime - startTime) /1000 );


                String startDate = sdf.format(ds);
                String endDate = sdf.format(de);

                valueTV.setText(rowId + "| "
                        + startDate + " | "
                        + endDate + " | "
                        + packageName+ " | "
                        + tDiffInsec + " secs ");
                mainLayout.addView(valueTV);


                TextView tv1 = new TextView(this);
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                tv1.setText("\n");
                mainLayout.addView(tv1);
            } while (cursor.moveToNext());
        }else {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setText("--please launch an app(fb , whatsapp or contacts to find the stats)");
            mainLayout.addView(tv);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
