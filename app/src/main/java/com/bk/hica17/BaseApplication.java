package com.bk.hica17;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dell on 17-Nov-16.
 */
public class BaseApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
