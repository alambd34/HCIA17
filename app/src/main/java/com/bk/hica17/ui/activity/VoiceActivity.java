package com.bk.hica17.ui.activity;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.appconstant.AppConstant;
import com.bk.hica17.model.DataVoice;
import com.bk.hica17.utils.Util;
import com.yalantis.waves.util.Horizon;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoiceActivity extends AppCompatActivity {

    private static final int RECORDER_CHANNELS = 1;
    private static final int RECORDER_ENCODING_BIT = 16;
    private static final int RECORDER_SAMPLE_RATE = 44100;
    @Bind(R.id.rl_root_view_voice)
    RelativeLayout mRootView;

    @Bind(R.id.layout_question_answer)
    LinearLayout mViewQuestionAnswer;

    @Bind(R.id.txt_question)
    TextView mTxtQuestion;

    @Bind(R.id.txt_answer)
    TextView mTxtAnswer;

    @Bind(R.id.rl_list_result)
    RecyclerView mRvResults;

    @Bind(R.id.txt_title_what_help)
    TextView mTxtWhatHelp;

    @Bind(R.id.gl_surface_view)
    GLSurfaceView mGlSurfaceEffect;

    @Bind(R.id.img_voice)
    ImageView mImgVoice;

    SpeechRecognizer mSpeechRecognizer;
    TextToSpeech mTextToSpeech;
    Intent recognizerIntent;
    Horizon mHorizon;
    ArrayList<byte[]> dataVoice = null;

    boolean mEnableTextToSpeech = true;
    boolean mRecognizing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);
        Util.checkRecognizerPermission(this);

        initTextToSpeech();
        initView();
        initRecognizerIntent();
        initSpeechRecognizer();
    }

    public void initView() {

        this.mHorizon = new Horizon(
                mGlSurfaceEffect, getResources().getColor(R.color.background_surface),
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_ENCODING_BIT);
        this.mHorizon.setMaxVolumeDb(120);

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
                        Log.e("TTS", "This Language is not supported");
                    } else {
//                        speakOut(AppConstant.WHAT_HELP);
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    public void speakOut(String text) {
        if (!text.isEmpty() && mEnableTextToSpeech) {
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
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

    public void initSpeechRecognizer() {

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
                stopRecognizer();
            }

            @Override
            public void onError(int i) {
                Log.e(AppConstant.LOG_TAG, "error: " + Util.getErrorRecognizer(i));
                stopRecognizer();
            }

            @Override
            public void onResults(Bundle bundle) {
                handleResultRecognizing(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                handlePartialResultRecognizing(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        dataVoice = DataVoice.getDataVoice();
    }

    @OnClick(R.id.img_voice)
    public void onClickVoice(View view) {

        if (!mRecognizing) {
            new TaskRecognizing().execute();
        }
    }

    private class TaskRecognizing extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            mRecognizing = true;
            mSpeechRecognizer.startListening(recognizerIntent);
            mImgVoice.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Random rand = new Random();
            while (mRecognizing && dataVoice != null) {
                Log.e(AppConstant.LOG_TAG, "vao");
                int temp = rand.nextInt(dataVoice.size());
                publishProgress(temp);
                sleepEffect(50);
            }

            for (int i = 0; i < 20; i++) {
                publishProgress(0);
                sleepEffect(30);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mHorizon.updateView(dataVoice.get(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopRecognizer();
        }
    }

    public void stopRecognizer() {

        if (mRecognizing) {
            mRecognizing = false;
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            mImgVoice.setVisibility(View.VISIBLE);

        }
    }

    public void handleResultRecognizing(ArrayList<String> results) {

        stopRecognizer();
        if (results != null && results.size() > 0) {
            mTxtQuestion.setText(results.get(0));
        }
    }

    public void handlePartialResultRecognizing(ArrayList<String> patialResults) {
        if (patialResults != null && patialResults.size() > 0) {
            mTxtQuestion.setText(patialResults.get(0));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecognizing = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mRecognizing) {
            new TaskRecognizing().execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTextToSpeech.stop();
        mTextToSpeech.shutdown();

        mSpeechRecognizer.destroy();
        mGlSurfaceEffect.destroyDrawingCache();
    }

    public void sleepEffect(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

