package com.bk.hica17.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.appconstant.AppConstant;
import com.bk.hica17.callback.OnHeadPhoneCallback;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.DataVoice;
import com.bk.hica17.receiver.HeadPhoneReceiver;
import com.bk.hica17.utils.StringUtil;
import com.bk.hica17.utils.Util;
import com.yalantis.waves.util.Horizon;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HearPhoneActivity extends AppCompatActivity {

    public static final int CANCEL_FROM_VOICE = 100;
    public static final int CANCEL_FROM_PRESS = 200;

    @Bind(R.id.outgoing_call_txt_duration)
    Chronometer mChronometer;

    @Bind(R.id.outgoing_call_txt_caller_name)
    TextView mTxtCallerName;

    @Bind(R.id.outgoing_call_txt_caller_mob_no)
    TextView mTxtPhoneNumber;

    @Bind(R.id.out_img_caller)
    ImageView mImgCaller;

    @Bind(R.id.gl_surface_view)
    GLSurfaceView mGlSurfaceEffect;

    @Bind(R.id.cancel_call)
    ImageView mCancelCall;

    TextToSpeech mTextToSpeech;
    Handler mHandler;
    Horizon mHorizon;
    SpeechRecognizer mSpeechRecognizer;
    Intent recognizerIntent;
    int flag;
    Contact mContact;
    int mRecognizing = AppConstant.END_RECOGNIZING;
    ArrayList<byte[]> dataVoice = DataVoice.getDataVoice();
    BroadcastReceiver.PendingResult pendingResult;
    boolean mEnableTextToSpeech = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hear_phone);
        ButterKnife.bind(this);
//        Util.registerHeadPhone();
        handleIntent();
        initView();
        initInfoCall();
        initTextToSpeech();
        initSpeechRecognizer();
    }

    public void handleIntent() {
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(AppConstant.PACKAGE);
        flag = data.getInt(AppConstant.FLAG);
        mContact = (Contact) data.getSerializable(AppConstant.CONTACT);
    }


    public void initView() {
//        mChronometer.start();
        mCancelCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCall(CANCEL_FROM_PRESS);
            }
        });

        initVoiceEffect();
    }

    public void initVoiceEffect() {

        mGlSurfaceEffect.setZOrderOnTop(true);
        mGlSurfaceEffect.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceEffect.getHolder().setFormat(PixelFormat.RGBA_8888);
        mGlSurfaceEffect.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mHorizon = new Horizon(
                mGlSurfaceEffect, getResources().getColor(android.R.color.transparent),
                AppConstant.RECORDER_SAMPLE_RATE,
                AppConstant.RECORDER_CHANNELS,
                AppConstant.RECORDER_ENCODING_BIT);
        mHorizon.setMaxVolumeDb(120);
        mHorizon.updateView(dataVoice.get(0));
        mHorizon.updateView(dataVoice.get(0));

    }

    public void initInfoCall() {

        if (flag == AppConstant.OUT_COMMING) {
            mChronometer.setText(AppConstant.CALLING);
        }

        mTxtCallerName.setText(mContact.getName());
        mTxtPhoneNumber.setText(mContact.getPhoneNumber());
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mChronometer.setText("00:00");
                mChronometer.start();
            }
        }, 15000);
    }

    public void initHeadPhoneReceiver() {

        HeadPhoneReceiver.handleDynamic = true;
        HeadPhoneReceiver.setHeadPhoneCallback(new OnHeadPhoneCallback() {
            @Override
            public void onHeadPhonePress(BroadcastReceiver.PendingResult pending) {
                Log.e(AppConstant.LOG_TAG, "Register receiver dynamic");
                if (mRecognizing != AppConstant.START_RECOGNIZING) {
                    pendingResult = pending;
                    handleHeadPhonePrees();
                }
            }
        });
    }

    public void handleHeadPhonePrees() {

        if (mRecognizing != AppConstant.START_RECOGNIZING && pendingResult != null) {
            new TaskRecognizing().execute();
        }
    }

    public void initTextToSpeech() {

        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    int result = mTextToSpeech.setLanguage(mTextToSpeech.getDefaultVoice().getLocale());
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        mEnableTextToSpeech = false;
                        Log.e(AppConstant.LOG_TAG, "TextToSpeech : This Language is not supported");
                    } else {
//                        speakOut(AppConstant.WHAT_HELP);
                    }
                } else {
                    Log.e(AppConstant.LOG_TAG, "TextToSpeech : Initilization Failed!");
                }
            }
        });
    }

    public void speakOut(String text) {
        if (!text.isEmpty() && mEnableTextToSpeech) {
//            HashMap<String, String> myHashAlarm = new HashMap();
//            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
//                    String.valueOf(AudioManager.STREAM_ALARM));
            String utteranceId=this.hashCode() + "";
            mTextToSpeech.speak( "" + text, TextToSpeech.QUEUE_ADD, null, utteranceId);
        }
    }

    public void initSpeechRecognizer() {
        initRecognizerIntent();
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e(AppConstant.LOG_TAG, "Begin of speech");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.e(AppConstant.LOG_TAG, "End of Speech");
                handleEndRecognizer();
            }

            @Override
            public void onError(int i) {
                Log.e(AppConstant.LOG_TAG, "error: " + Util.getErrorRecognizer(i));
                handleErrorRecognizing();
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.e(AppConstant.LOG_TAG, "result");
                handleResultRecognizing(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.e(AppConstant.LOG_TAG, "partial result");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    public void initRecognizerIntent() {

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }


    private class TaskRecognizing extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            mRecognizing = AppConstant.START_RECOGNIZING;
            mSpeechRecognizer.startListening(recognizerIntent);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Random rand = new Random();
            while (mRecognizing == AppConstant.START_RECOGNIZING && dataVoice != null) {
                int temp = rand.nextInt(dataVoice.size() - 1) + 1;
                publishProgress(temp);
                sleepEffect(70);
            }

            for (int i = 0; i < 220; i++) {
                publishProgress(0);
                sleepEffect(6);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mHorizon.updateView(dataVoice.get(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pendingResult.finish();
        }
    }

    public void sleepEffect(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handleEndRecognizer() {

        if (mRecognizing == AppConstant.START_RECOGNIZING) {
            mRecognizing = AppConstant.END_RECOGNIZING;
            mSpeechRecognizer.stopListening();
        } else {
            mRecognizing = AppConstant.END_RECOGNIZING;
        }
    }

    public void handleErrorRecognizing() {

        if (mRecognizing == AppConstant.START_RECOGNIZING) {
            mRecognizing = AppConstant.ERROR_RECOGNIZING;
            mSpeechRecognizer.stopListening();
        } else {
            mRecognizing = AppConstant.ERROR_RECOGNIZING;
        }
        mSpeechRecognizer.cancel();
    }


    public void handleResultRecognizing(ArrayList<String> results) {

        mRecognizing = AppConstant.RESULT_RECOGNIZING;
        mSpeechRecognizer.cancel();
        if (results != null && results.size() > 0) {
            String resultLine = StringUtil.removeAccent(results.get(0).trim().toLowerCase());
            if (resultLine.equals(AppConstant.CANCEL_CALL)) {
                cancelCall(CANCEL_FROM_VOICE);
            } else {
                vibrate(1500);
            }
        } else {
            vibrate(1500);
        }
    }

    public void vibrate(long millisecond) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(millisecond);
    }

    public void cancelCall(int cancelFrom) {

//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(startMain);// tat ung dung tro ve trang chu nhung khong xoa process
//
//        android.os.Process.killProcess(android.os.Process.myPid());//xoa process

        if (flag == AppConstant.OUT_COMMING) {

            if (cancelFrom == CANCEL_FROM_VOICE) {
                speakOut("Bạn đã hủy cuộc gọi");
            }
            finish();
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unDynamicRegisterHeadPhone();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelCall(CANCEL_FROM_PRESS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSpeechRecognizer.destroy();
        mGlSurfaceEffect.destroyDrawingCache();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeadPhoneReceiver();
    }

    public void unDynamicRegisterHeadPhone() {
        HeadPhoneReceiver.handleDynamic = false;
        HeadPhoneReceiver.setHeadPhoneCallback(null);
    }
}
