package com.marliao.smartschool;

import android.app.Application;
import android.content.Context;


public class AppClient extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

}
