package com.bk.hica17;

import android.app.Application;
import android.content.Context;

import com.bk.hica17.utils.Util;

/**
 * Created by Dell on 17-Nov-16.
 */
public class BaseApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Util.registerHeadPhone();
    }

    public static Context getContext() {
        return mContext;
    }
}
