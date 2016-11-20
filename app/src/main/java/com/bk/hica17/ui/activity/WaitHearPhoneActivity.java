package com.bk.hica17.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class WaitHearPhoneActivity extends AppCompatActivity {

    public static final int CANCEL_FROM_VOICE = 100;
    public static final int CANCEL_FROM_PRESS = 200;

    ImageView cirImgCancel;
    ImageView cirImgYes;
    ImageView cirImgCancelMessage;
    ImageView imgUp;
    Animation animImgCancel;
    Animation animImgYes;
    Animation animImgCancelMessage;
    Animation animationUp;
    GLSurfaceView mGlSurfaceEffect;
    TextView mTxtCallerName;
    TextView mTxtPhoneNumber;

    float[] X = new float[3];
    float[] Y = new float[3];

    TextToSpeech mTextToSpeech;
    Horizon mHorizon;
    SpeechRecognizer mSpeechRecognizer;
    Intent recognizerIntent;
    Contact mContact;
    int mRecognizing = AppConstant.END_RECOGNIZING;
    ArrayList<byte[]> dataVoice = DataVoice.getDataVoice();
    BroadcastReceiver.PendingResult pendingResult;
    boolean mEnableTextToSpeech = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_hear_phone);
        //getSupportActionBar().hide();
        handleIntent();
        initView();
        initInfoCall();
        initVoiceEffect();
        initTextToSpeech();
        initSpeechRecognizer();

    }

    public void handleIntent() {

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra(AppConstant.PHONE_NUMBER_TAG);
        mContact = new Contact("Trung bk2", phoneNumber);
    }

    public void initView() {
        cirImgCancel = (ImageView) findViewById(R.id.cirImgCancel);
        cirImgYes = (ImageView) findViewById(R.id.cirImgYes);
        cirImgCancelMessage = (ImageView) findViewById(R.id.cirImgCancelMessage);
        imgUp = (ImageView) findViewById(R.id.imgUp);

        animImgCancel = AnimationUtils.loadAnimation(this, R.anim.trans_cancel_call);
        animImgYes = AnimationUtils.loadAnimation(this, R.anim.trans_call);
        animImgCancelMessage = AnimationUtils.loadAnimation(this, R.anim.trans_cancel_message);
        animationUp = AnimationUtils.loadAnimation(this, R.anim.up);

        cirImgCancel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                X[0] = cirImgCancel.getX();
                Y[0] = cirImgCancel.getY();
                cirImgCancel.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        cirImgYes.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                X[1] = cirImgYes.getX();
                Y[1] = cirImgYes.getY();
                cirImgYes.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        cirImgCancelMessage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                X[2] = cirImgCancelMessage.getX();
                Y[2] = cirImgCancelMessage.getY();
                cirImgCancelMessage.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });


        animImgCancelMessage.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cirImgCancel.startAnimation(animImgCancel);
                cirImgYes.startAnimation(animImgYes);
                cirImgCancelMessage.startAnimation(animImgCancelMessage);
                imgUp.startAnimation(animationUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cirImgCancel.startAnimation(animImgCancel);
        cirImgYes.startAnimation(animImgYes);
        cirImgCancelMessage.startAnimation(animImgCancelMessage);
        imgUp.startAnimation(animationUp);

        cirImgCancel.setOnTouchListener(new PrivateOnTouchListener(0));
        cirImgYes.setOnTouchListener(new PrivateOnTouchListener(1));
        cirImgCancelMessage.setOnTouchListener(new PrivateOnTouchListener(2));

//        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
//
//        long[] pattern = {0, 1500, 1000};
//        v.vibrate(pattern, 0);
    }

    public void initInfoCall() {

        mTxtCallerName = (TextView) findViewById(R.id.txtCaller);
        mTxtPhoneNumber = (TextView) findViewById(R.id.txtNumberPhone);
        mTxtPhoneNumber.setText(mContact.getPhoneNumber());
        mTxtCallerName.setText(mContact.getName());
    }

    public void initVoiceEffect() {

        mGlSurfaceEffect = (GLSurfaceView) findViewById(R.id.gl_surface_view);
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

    public void handleHeadPhonePrees() {

        if (mRecognizing != AppConstant.START_RECOGNIZING) {
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
            String utteranceId = this.hashCode() + "";
            mTextToSpeech.speak("" + text, TextToSpeech.QUEUE_ADD, null, utteranceId);
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
            } else if (resultLine.equals(AppConstant.ACCEPT_CALL)) {
                startHearActivity();
            } else {
                vibrate(1000);
            }

        } else {
            vibrate(1000);
        }
    }

    public void vibrate(long millisecond) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(millisecond);
    }

    public void cancelCall(int cancelFrom) {

        if (cancelFrom == CANCEL_FROM_VOICE) {
            speakOut("Bạn đã hủy cuộc gọi");
        }
        finish();
    }

    public void startHearActivity() {

        Intent hearIntent = new Intent(this, HearPhoneActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(AppConstant.CONTACT, mContact);
        data.putInt(AppConstant.FLAG, AppConstant.IN_COMMING);
        hearIntent.putExtra(AppConstant.PACKAGE, data);
        startActivity(hearIntent);

        // dang lam gio xu ly khi co cuoc goi den, chua xu ly giong noi
    }


    private class PrivateOnTouchListener implements View.OnTouchListener {

        private PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
        private PointF StartPT = new PointF(); // Record Start Position of 'img'
        //private boolean i = true;
        private float y;
        private int flag;

        public PrivateOnTouchListener(int flag) {

            this.flag = flag;
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            ImageView view = (ImageView) v;
            setViewTouch(view);
//            if (i){
//                y = view.getY();
//                i = false;
//            }

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_MOVE:

                    int a = (int) (view.getY() - Y[flag]);
                    if (a <= 0) {
                        if (a >= -250) {
                            view.setImageAlpha(250 + a);
                        }

                        PointF mv = new PointF(event.getX() - DownPT.x, event.getY() - DownPT.y);
                        view.setX((int) StartPT.x);
                        view.setY((int) (StartPT.y + mv.y));
                        StartPT.set(view.getX(), view.getY());
                    }

                    break;

                case MotionEvent.ACTION_DOWN:
                    DownPT.set(event.getX(), event.getY());
                    StartPT.set(view.getX(), view.getY());
                    break;

                case MotionEvent.ACTION_UP:

                    if (view.getY() - Y[flag] < -250) {

                        switch (flag) {

                            case 0:
                                cancelCall(CANCEL_FROM_PRESS);
                                break;

                            case 1:
                                startHearActivity();
                                break;

                            case 2:
                                Toast.makeText(WaitHearPhoneActivity.this, "Đã gửi tin nhắn hủy cuộc gọi", Toast.LENGTH_LONG).show();
                                cancelCall(CANCEL_FROM_PRESS);
                                break;

                        }

                    } else {

                        view.setX(X[flag]);
                        view.setY(Y[flag]);
                        view.setImageAlpha(250);
                        setStart();
                    }
                    break;
            }

            return true;

        }

        public void setViewTouch(ImageView view) {

            switch (flag) {

                case 0:
                    setInVisible(cirImgYes, cirImgCancelMessage);
                    break;

                case 1:
                    setInVisible(cirImgCancel, cirImgCancelMessage);
                    break;

                case 2:
                    setInVisible(cirImgCancel, cirImgYes);
                    break;

            }

            cirImgCancel.clearAnimation();
            cirImgYes.clearAnimation();
            cirImgCancelMessage.clearAnimation();
            imgUp.clearAnimation();

        }

        public void setInVisible(ImageView view1, ImageView view2) {

            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
            imgUp.setVisibility(View.INVISIBLE);
        }

        public void setStart() {

            cirImgCancel.setVisibility(View.VISIBLE);
            cirImgYes.setVisibility(View.VISIBLE);
            cirImgCancelMessage.setVisibility(View.VISIBLE);
            imgUp.setVisibility(View.VISIBLE);

            cirImgCancel.startAnimation(animImgCancel);
            cirImgYes.startAnimation(animImgYes);
            cirImgCancelMessage.startAnimation(animImgCancelMessage);
            imgUp.startAnimation(animationUp);

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
}
