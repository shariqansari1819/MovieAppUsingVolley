package com.shariqansasri.movieapp.app;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static MyApplication getMyApplication(){
        return myApplication;
    }


    public static Context getAppContext(){
        return myApplication.getApplicationContext();
    }

}
