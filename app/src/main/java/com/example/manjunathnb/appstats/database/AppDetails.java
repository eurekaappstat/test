package com.example.manjunathnb.appstats.database;

/**
 * Created by manjunathnb on 7/7/2015.
 */
public class AppDetails {
    private String packageName;
    private long lastUpdatedTime;
    private int appId;

    public String getPackageName() {
        return packageName;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public int getAppId() {
        return appId;
    }
}
