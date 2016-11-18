package com.bk.hica17.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bk.hica17.R;
import com.bk.hica17.callback.OnVoiceDialogCallback;
import com.bk.hica17.callback.SimpleRecognizerListener;
import com.bk.hica17.model.CustomSpeechRecognizerHandler;
import com.cleveroad.audiovisualization.AudioVisualization;

/**
 * Created by Dell on 16-Nov-16.
 */
public class VoiceDialog extends DialogFragment {

    ImageButton mImgBtnVoice;
    TextView mTxtTitle;
    AudioVisualization mAudioVisualization;
    CustomSpeechRecognizerHandler mSpeechHandler;
    Intent recognizerIntent;
    SimpleRecognizerListener mRecognizerListener;
    OnVoiceDialogCallback mOnVoiceCallback;

    private boolean mRecognizing = false;

    public static VoiceDialog newInstance() {
        return new VoiceDialog();
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.voice_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

//        mImgBtnVoice = (ImageButton)dialog.findViewById(R.id.img_btn_voice_dialog);
//        mTxtTitle = (TextView)dialog.findViewById(R.id.title_voice);
//        mAudioVisualization = (AudioVisualization)dialog.findViewById(R.id.visualizer_view);

        initIntent();


        initSpeechHandler();

        mImgBtnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mRecognizing) {
                    startRecognizing();
                } else {
                    stopRecognizing();
                }
                if (mOnVoiceCallback != null) {
                    mOnVoiceCallback.onVoiceClick();
                }
            }
        });

        return dialog;
    }

    public void setOnVoiceCallback(OnVoiceDialogCallback onVoiceCallback) {
        this.mOnVoiceCallback = onVoiceCallback;
    }

    public void setTitle(String title) {

        if (title != null) {
            mTxtTitle.setText(title);
        }
    }

    public void initIntent() {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    public void initSpeechHandler() {

        mSpeechHandler = new CustomSpeechRecognizerHandler(getActivity());
        mSpeechHandler.innerRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

                if (mRecognizerListener != null) {
                    mRecognizerListener.onBeginningOfSpeech();
                }
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                stopRecognizing();
                if (mRecognizerListener != null) {
                    mRecognizerListener.onEndOfSpeech();
                }
            }

            @Override
            public void onError(int i) {
                stopRecognizing();
                if (mRecognizerListener != null) {
                    mRecognizerListener.onError(i);
                }
                Log.e("tuton", "error:" + getErrorText(i));
            }

            @Override
            public void onResults(Bundle bundle) {

                Log.e("tuton", "result : ");
                if (mRecognizerListener != null) {
                    mRecognizerListener.onResults(bundle);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

                if (mRecognizerListener != null) {
                    mRecognizerListener.onPartialResults(bundle);
                }
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        mAudioVisualization.linkTo(mSpeechHandler);
    }

    public void setRecognizerListener(SimpleRecognizerListener recognizerListener) {
        this.mRecognizerListener = recognizerListener;
    }


    public static String getErrorText(int errorCode) {
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

    public void changeIconVoice() {

        if (mRecognizing) {
            mImgBtnVoice.setEnabled(false);
        } else {
            mImgBtnVoice.setEnabled(true);
        }
    }

    public void stopRecognizing() {

        if (mRecognizing) {
            mRecognizing = false;
            mSpeechHandler.stopListening();
            mSpeechHandler.stop();
            changeIconVoice();
        }

    }

    public void startRecognizing() {

        if (!mRecognizing) {
            mRecognizing = true;
            mSpeechHandler.startListening(recognizerIntent);
            changeIconVoice();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mRecognizing) {
            stopRecognizing();
        }
    }

}
