package com.bk.hica17.ui.activity;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.bk.hica17.R;
import com.yalantis.waves.util.Horizon;

public class HearPhoneActivity extends AppCompatActivity {

    Chronometer mChronometer;
    ImageView mCancelCall;
    GLSurfaceView mGlSurfaceView;
    Horizon mHorizong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hear_phone);

        handleIntent();
        initView();


    }

    public void handleIntent() {
        Intent intent = getIntent();
    }


    public void initView() {
        mChronometer = (Chronometer) findViewById(R.id.outgoing_call_txt_duration);
        mCancelCall = (ImageView) findViewById(R.id.cancel_call);

        mChronometer.start();
        mCancelCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCall();
            }
        });

        initVoiceEffect();
    }

    public void initVoiceEffect() {




    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        cancelCall();
    }

    public void cancelCall(){

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);// tat ung dung tro ve trang chu nhung khong xoa process

        android.os.Process.killProcess(android.os.Process.myPid());//xoa process
    }
}
