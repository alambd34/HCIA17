package com.bk.hica17.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.adapter.RvContactVoiceAdapter;
import com.bk.hica17.appconstant.AppConstant;
import com.bk.hica17.callback.OnItemContactVoiceClickCallback;
import com.bk.hica17.model.Contact;
import com.bk.hica17.model.ContactLoader;
import com.bk.hica17.model.DataVoice;
import com.bk.hica17.utils.Util;
import com.yalantis.waves.util.Horizon;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoiceActivity extends AppCompatActivity {

    public static final int START_RECOGNIZING = 0;
    public static final int END_RECOGNIZING = 1;
    public static final int RESULT_RECOGNIZING = 2;
    public static final int ERROR_RECOGNIZING = 3;
    public static final int CONTACT_QUERY_LOADER = 0;
    public static final String QUERY_KEY = "query";

    public static final int WRONG_SYNTAX = 1;
    public static final int NO_SPEECH = 2;
    public static final int RIGHT_SYNTAX = 3;

    public static final int CREATE_NEW_ACTIVITY = 1;
    public static final int BACK_FROM_ACTIVITY = 2;

    @Bind(R.id.rl_root_view_voice)
    RelativeLayout mRootView;

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

    @Bind(R.id.ll_result)
    LinearLayout llResult;


    SpeechRecognizer mSpeechRecognizer;
    TextToSpeech mTextToSpeech;
    Intent recognizerIntent;
    Horizon mHorizon;
    ArrayList<byte[]> dataVoice = null;
    RvContactVoiceAdapter mContactAdapter;
    String query = "";

    boolean mEnableTextToSpeech = true;
    int mRecognizing = END_RECOGNIZING;
    int flag = CREATE_NEW_ACTIVITY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ButterKnife.bind(this);
        Util.checkRecognizerPermission(this);

        initTextToSpeech();
        initView();
        initRecognizerIntent();
        initSpeechRecognizer();
    }

    public void initView() {

        mGlSurfaceEffect.setZOrderOnTop(true);
        mGlSurfaceEffect.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceEffect.getHolder().setFormat(PixelFormat.RGBA_8888);
        mGlSurfaceEffect.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        this.mHorizon = new Horizon(
                mGlSurfaceEffect, getResources().getColor(android.R.color.transparent),
                AppConstant.RECORDER_SAMPLE_RATE,
                AppConstant.RECORDER_CHANNELS,
                AppConstant.RECORDER_ENCODING_BIT);
        this.mHorizon.setMaxVolumeDb(120);

        Util.applyAnimation(mTxtWhatHelp, R.anim.translate_alpha);
        Util.applyCustomFont(mTxtWhatHelp, Util.faceRegular);
        Util.applyCustomFont(mTxtAnswer, Util.faceRegular);
        Util.applyCustomFont(mTxtQuestion, Util.faceMini);
        initRv();
//        initBackground();
    }


    public void initTextToSpeech() {

        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    int result = mTextToSpeech.setLanguage(mTextToSpeech.getDefaultVoice().getLocale());
//                    int result = mTextToSpeech.setLanguage(Locale.ENGLISH);

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
                handlePartialResultRecognizing(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        dataVoice = DataVoice.getDataVoice();
    }

    public void initRv() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mContactAdapter = new RvContactVoiceAdapter(this, null);
        mContactAdapter.setItemClickListener(new OnItemContactVoiceClickCallback() {
            @Override
            public void onItemClick(int position) {
                startCallActivity(mContactAdapter.getContacts().get(position));
            }
        });

        mRvResults.setLayoutManager(layoutManager);
        mRvResults.setAdapter(mContactAdapter);
    }


    @OnClick(R.id.img_voice)
    public void onClickVoice(View view) {

        if (mRecognizing != START_RECOGNIZING) {
            new TaskRecognizing().execute();
        }
    }

    private class TaskRecognizing extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            mRecognizing = START_RECOGNIZING;
            mSpeechRecognizer.startListening(recognizerIntent);
            updateVoiceView();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Random rand = new Random();
            while (mRecognizing == START_RECOGNIZING && dataVoice != null) {
                int temp = rand.nextInt(dataVoice.size() -1) + 1;
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
            updateVoiceView();
            if (mRecognizing == ERROR_RECOGNIZING) {
                showResult(null, NO_SPEECH);
            }
        }
    }

    public void updateVoiceView() {

        if (mRecognizing == START_RECOGNIZING) {

            if (mImgVoice.getVisibility() == View.VISIBLE) {
                mImgVoice.setVisibility(View.INVISIBLE);
            }
        } else {

            if (mImgVoice.getVisibility() != View.VISIBLE) {
                mImgVoice.setVisibility(View.VISIBLE);
            }
        }
    }


    public void handleEndRecognizer() {

        if (mRecognizing == START_RECOGNIZING) {
            mRecognizing = END_RECOGNIZING;
            mSpeechRecognizer.stopListening();
        } else {
            mRecognizing = END_RECOGNIZING;
        }
    }

    public void handleErrorRecognizing() {

        if (mRecognizing == START_RECOGNIZING) {
            mRecognizing = ERROR_RECOGNIZING;
            mSpeechRecognizer.stopListening();
        } else {
            mRecognizing = ERROR_RECOGNIZING;
        }
        mSpeechRecognizer.cancel();
        mTxtQuestion.setText("");
        mTxtAnswer.setText(AppConstant.NO_SPEECH);
    }


    public void handleResultRecognizing(ArrayList<String> results) {

        mRecognizing = RESULT_RECOGNIZING;
        String phoneNumber;
        if (results != null && results.size() > 0) {

            String mCurrQuestion = results.get(0);
            String showQuestion = "\"" + mCurrQuestion + "\"";
            mTxtQuestion.setText(showQuestion);
            ArrayList<String> subInputs = processInput(mCurrQuestion);
            if (subInputs.size() > 1 && (subInputs.get(0).equalsIgnoreCase("gọi") || subInputs.get(0).equalsIgnoreCase("goi"))) {
                phoneNumber = getPhoneNumber(subInputs);
                if (phoneNumber != null) {
                    Contact contact = new Contact(AppConstant.UNKNOWN, phoneNumber);
                    ArrayList<Contact> contacts = new ArrayList<>();
                    contacts.add(contact);
                    showResult(contacts, RIGHT_SYNTAX);
                } else {
                    searchPhoneInContract(subInputs);
                }
            } else {
                showResult(null, WRONG_SYNTAX);
            }
        }
        mSpeechRecognizer.cancel();
    }

    public ArrayList<String> processInput(String lineInput) {

        StringTokenizer tokenizer = new StringTokenizer(lineInput, " ");
        ArrayList<String> result = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }
        Log.e("tuton", result.get(0));
        return result;
    }

    public String getPhoneNumber(ArrayList<String> subInputs) {

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < subInputs.size(); i++) {

            String s = subInputs.get(i);
            for (int j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
                    builder.append(c);
                } else {
                    return null;
                }
            }
        }
        return builder.toString();
    }


    public void handlePartialResultRecognizing(ArrayList<String> partialResults) {

        if (partialResults != null && partialResults.size() > 0) {
            mTxtQuestion.setText(partialResults.get(0));
            if (mTxtWhatHelp.getVisibility() == View.VISIBLE) {
                Util.applyAnimation(mTxtWhatHelp, R.anim.fade_out);
                mTxtWhatHelp.setVisibility(View.INVISIBLE);
            }

            if (llResult.getVisibility() == View.VISIBLE) {
                llResult.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void searchPhoneInContract(ArrayList<String> subInputs) {

        if (subInputs == null || subInputs.size() <= 1) {
            return;
        }

        query = "";
        for (int i = 1; i < subInputs.size() - 1; i++) {
            query += subInputs.get(i);
            query += " ";
        }
        query += subInputs.get(subInputs.size() - 1);

        Bundle data = new Bundle();
        data.putString(QUERY_KEY, query);

        ContactLoader loader = new ContactLoader(this);
        loader.setLoadCallback(new ContactLoader.OnLoadFinishCallback() {
            @Override
            public void onLoadFinish(ArrayList<Contact> contacts) {
                showResult(contacts, RIGHT_SYNTAX);
            }
        });

        // start the loader with the new query, and an object that will handle all callbacks.
        getLoaderManager().restartLoader(CONTACT_QUERY_LOADER, data, loader);
    }

    public void showResult(ArrayList<Contact> contacts, int typeShow) {

        if (mTxtWhatHelp.getVisibility() == View.VISIBLE) {
            Util.applyAnimation(mTxtWhatHelp, R.anim.fade_out);
            mTxtWhatHelp.setVisibility(View.INVISIBLE);
        }
        String showAnswer;
        boolean speak = true;
        mContactAdapter.removeAll();
        if (typeShow == WRONG_SYNTAX) {
            showAnswer = AppConstant.WRONG_SYNTAX;
        } else if (typeShow == RIGHT_SYNTAX) {
            if (contacts != null && contacts.size() > 0) {
                showAnswer = AppConstant.TITLE_RESULT;
                mContactAdapter.addAll(contacts);
                if (contacts.size() == 1) {
                    speak = false;
                    startCallActivity(contacts.get(0));
                } else {

                    for (int i = 0; i < contacts.size(); i++) {
                        if (contacts.get(i).getName().equalsIgnoreCase(query)) {
                            startCallActivity(contacts.get(i));
                            speak = false;
                            break;
                        }
                    }
                }
            } else {
                showAnswer = AppConstant.PRE_NO_RESULT + "\"" + query + "\"" + AppConstant.END_NO_RESULT;
            }

        } else {
            showAnswer = AppConstant.NO_SPEECH;
        }
        if (llResult.getVisibility() != View.VISIBLE) {
            llResult.setVisibility(View.VISIBLE);

            mTxtAnswer.setText(showAnswer);
            Util.applyAnimation(llResult, R.anim.translate_alpha2);
        }
        if (speak) {
            speakOut(showAnswer);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mRecognizing == START_RECOGNIZING) {
            mRecognizing = ERROR_RECOGNIZING;
            mSpeechRecognizer.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mRecognizing != START_RECOGNIZING && flag == CREATE_NEW_ACTIVITY) {
            new TaskRecognizing().execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        flag = BACK_FROM_ACTIVITY;
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

    public void startCallActivity(Contact contact) {

        Intent callIntent = new Intent(this, HearPhoneActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(AppConstant.CONTACT, contact);
        data.putInt(AppConstant.FLAG, AppConstant.OUT_COMMING);
        callIntent.putExtra(AppConstant.PACKAGE, data);
        startActivity(callIntent);
    }
}

