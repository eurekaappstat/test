package com.example.manjunathnb.appstats.database;

import android.net.Uri;

/**
 * Created by manjunathnb on 7/12/2015.
 */
public class AppStatConstants {
    //provider
    private static final String PROVIDER_NAME = "com.example.manjunathnb.appstats.AppStatProvider";
    //Table name
    public static final String APP_INFO_TABLE = "app_info_table";
    public static final String ID = "_id";
    public static final String APP_PACKAGE = "app_package";
    public static final String LAST_APP_LAUNCHED_TIME = "last_app_launched_time";
    public static final String LAST_APP_EXITED_TIME = "last_app_exited_time";
    public static final Uri CONTENT_URI =
            Uri.parse("content://"+ PROVIDER_NAME + APP_INFO_TABLE);
}
