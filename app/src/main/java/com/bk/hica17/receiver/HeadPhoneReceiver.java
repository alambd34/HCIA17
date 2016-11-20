package com.bk.hica17.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.bk.hica17.callback.OnHeadPhoneCallback;
import com.bk.hica17.ui.activity.VoiceActivity;

/**
 * Created by Dell on 10-Nov-16.
 */
public class HeadPhoneReceiver extends BroadcastReceiver {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static boolean handleDynamic = false;
    private static OnHeadPhoneCallback mHeadPhoneCallback = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {

            KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.ACTION_DOWN == event.getAction()) {

                if (!handleDynamic) {
                    launchingVoiceActivity(context);
                } else if (mHeadPhoneCallback != null) {
                    PendingResult pendingResult = goAsync();
                    mHeadPhoneCallback.onHeadPhonePress(pendingResult);
                }
            }
        }
    }

    public void launchingVoiceActivity(Context context) {
        Intent intent = new Intent(context, VoiceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void setHeadPhoneCallback(OnHeadPhoneCallback headPhoneCallback) {
        mHeadPhoneCallback = headPhoneCallback;
    }

}
