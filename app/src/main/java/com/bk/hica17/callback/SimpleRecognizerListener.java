package com.bk.hica17.callback;

import android.os.Bundle;

/**
 * Created by Dell on 16-Nov-16.
 */
public interface SimpleRecognizerListener {

    public void onBeginningOfSpeech();

    public void onEndOfSpeech();

    public void onError(int i);

    public void onResults(Bundle bundle);

    public void onPartialResults(Bundle bundle);

}
