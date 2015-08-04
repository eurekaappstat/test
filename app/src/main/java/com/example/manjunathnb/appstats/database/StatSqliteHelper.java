package com.example.manjunathnb.appstats.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

/**
 * Created by manjunathnb on 7/7/2015.
 */
public class StatSqliteHelper extends SQLiteOpenHelper {
    //Column name
    public static final String ID = "_id";
    public static final String APP_PACKAGE = "app_package";
    public static final String LAST_APP_LAUNCHED_TIME = "last_app_launched_time";
    public static final String LAST_APP_EXITED_TIME = "last_app_exited_time";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + AppStatConstants.APP_INFO_TABLE + "("
            + ID + " integer primary key autoincrement, "
            + LAST_APP_LAUNCHED_TIME + " long, "
            + LAST_APP_EXITED_TIME + " long, "
            + APP_PACKAGE + " text not null);";

    private static final String DATABASE_NAME = "appstatinfo.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;


    public StatSqliteHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("manju :StatSqliteHelper", "super(ctx, DATABASE_NAME, null, DATABASE_VERSION)");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        Log.d("manju :StatSqliteHelper", "onConfigure");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(StatSqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + AppStatConstants.APP_INFO_TABLE);
        onCreate(db);
    }
}
