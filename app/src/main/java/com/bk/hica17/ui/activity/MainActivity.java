package com.bk.hica17.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bk.hica17.R;
import com.bk.hica17.dialog.VoiceDialog;
import com.bk.hica17.utils.Util;

public class MainActivity extends AppCompatActivity {

//    RelativeLayout mRlVoice;
    boolean isShowVoice = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.checkRecognizerPermission(this);

        initView();

    }


    public void initView() {

//        mRlVoice = (RelativeLayout)findViewById(R.id.rl_voice);
//        mRlVoice.setVisibility(View.GONE);
    }




    public void onClickVoice(View view) {

//        VoiceDialog dialog = VoiceDialog.newInstance();
//        dialog.show(getFragmentManager(), "VoiceDialog");
//        if (isShowVoice) {
//            Util.animateHideView(mRlVoice);
//        } else {
//            Util.animateShowView(mRlVoice);
//        }
//        isShowVoice = !isShowVoice;

        VoiceDialog dialog = VoiceDialog.newInstance();
        dialog.show(getFragmentManager(), "VoiceDialog");



    }

}
