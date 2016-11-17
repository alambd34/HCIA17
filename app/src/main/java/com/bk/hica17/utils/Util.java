package com.bk.hica17.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Dell on 16-Nov-16.
 */
public class Util {

    public static void checkRecognizerPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS)) {

            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS,
                                Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE},
                        10
                );
            }
        }
    }

    public static void animateShowView(View showView) {
        showView.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180);
    }

    public static void animateHideView(View hideView) {
        hideView.animate()
                .translationY(-hideView.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180);
    }

}
