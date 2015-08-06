package com.example.manjunathnb.appstats.database;

import com.example.manjunathnb.appstats.listener.StatSqliteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by manjunathnb on 7/7/2015.
 */
public class DBManager {



    private static DBManager dbManager;
    private SQLiteDatabase sqlDB;

    private DBManager(Context context) {
        mStatSqliteHelper =  new StatSqliteHelper(context);
    }
    public static  DBManager getInstance(Context context)
    {
        if(dbManager == null ) {
            synchronized (DBManager.class){
                if(dbManager == null ) {
                    Log.d("manju :DBManager", "creating DB manager instance");
                    dbManager =  new DBManager(context);
                }
            }
        }
        return dbManager;
    }
    private StatSqliteHelper mStatSqliteHelper;

    public void getWritableDB() {
        sqlDB =  mStatSqliteHelper.getWritableDatabase();
    }

    public void getReadableDB() {
        sqlDB =  mStatSqliteHelper.getReadableDatabase();
    }
    
    public void beginTransaction() {
    	sqlDB.beginTransaction();
    }
    
    public void endTransaction() {
    	sqlDB.endTransaction();
    }

    public long insert(String tableName, ContentValues values) {
        Log.d("manju :DBManager","insert");
        long ret =  sqlDB.insert(tableName, null, values );
        return ret;
    }

    public Cursor query(String tableName, String[] projection, String selection, String[] selectionArgs,
                        String groupBy, String having,String orderBy) {
            Log.d("manju :DBManager","query");
      //  String where  = AppStatConstants.APP_PACKAGE + " IN ('com.facebook.katana' ,  'com.whatsapp' , 'com.android.contacts' )";
        //Log.d("manju :DBManager where clause is : ", where);
        Cursor retCursor =  sqlDB.query(tableName, projection, null, selectionArgs, groupBy, having, orderBy) ;
        return retCursor;
    }

    public int update (String tableName, ContentValues cv , String whereClause , String [] whereArgs){
       int ret =  sqlDB.update(tableName,cv, whereClause , whereArgs );
        return ret;
    }
}
