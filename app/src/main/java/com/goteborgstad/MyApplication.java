package com.goteborgstad;

public class MyApplication extends android.app.Application {

    @Override
    protected void attachBaseContext(android.content.Context context) {
        super.attachBaseContext(context);
        android.support.multidex.MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}