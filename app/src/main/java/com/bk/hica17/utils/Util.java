package com.bk.hica17.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bk.hica17.BaseApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dell on 16-Nov-16.
 */
public class Util {

    public static Typeface faceRegular = Typeface.createFromAsset(BaseApplication.getContext().getAssets(), "fonts/Roboto-Regular.ttf");
    public static Typeface faceMini = Typeface.createFromAsset(BaseApplication.getContext().getAssets(), "fonts/Roboto-Thin.ttf");

    public static void checkRecognizerPermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS)) {

            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{
                                Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS,
                                Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.PROCESS_OUTGOING_CALLS},
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

    public static void applyAnimation(View view, int idAnim) {

        Animation animation = AnimationUtils.loadAnimation(BaseApplication.getContext(), idAnim);
        view.startAnimation(animation);
    }

    public static void animateTranslateY(View view, int toY) {
        view.animate()
                .translationY(-toY)
                .setInterpolator(new LinearInterpolator())
                .setDuration(200);
    }

    public static ArrayList<byte[]> readVoice() {

        ArrayList<byte[]> data = new ArrayList<>();
        int length = 1024;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new InputStreamReader(BaseApplication.getContext().getAssets().open("voice.txt")));
            while (scanner.hasNext()) {

                int j = 0;
                byte[] buffer = new byte[length];
                while (scanner.hasNext() && j < length) {
                    buffer[j++] = (byte) scanner.nextInt();
                }
                data.add(buffer);
            }
        } catch (IOException e) {
            Log.e("tuton", "loi doc file");
        }
        return data;
    }

    public static void applyCustomFont(TextView textView, Typeface face) {
        textView.setTypeface(face);
    }

    public static String getErrorRecognizer(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public static void hideSoftKeyboard(Activity activity) {

        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void setHideSoftInput(EditText editSearch, boolean status) {
        if (Build.VERSION.SDK_INT >= 21) {
            editSearch.setShowSoftInputOnFocus(!status);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            editSearch.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editSearch.setTextIsSelectable(status);
        } else {
            editSearch.setRawInputType(InputType.TYPE_NULL);
            editSearch.setFocusable(status);
        }
    }
}
