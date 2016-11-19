package com.bk.hica17.ui.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bk.hica17.R;

public class WaitHearPhoneActivity extends AppCompatActivity {

    ImageView cirImgCancel;
    ImageView cirImgYes;
    ImageView cirImgCancelMessage;
    ImageView imgUp;
    Animation animImgCancel;
    Animation animImgYes;
    Animation animImgCancelMessage;
    Animation animationUp;

    float[] X = new float[3];
    float[] Y = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_hear_phone);
        //getSupportActionBar().hide();

        cirImgCancel = (ImageView) findViewById(R.id.cirImgCancel);
        cirImgYes = (ImageView) findViewById(R.id.cirImgYes);
        cirImgCancelMessage = (ImageView) findViewById(R.id.cirImgCancelMessage);
        imgUp = (ImageView) findViewById(R.id.imgUp);

        animImgCancel = AnimationUtils.loadAnimation(this, R.anim.trans_cancel_call);
        animImgYes = AnimationUtils.loadAnimation(this, R.anim.trans_call);
        animImgCancelMessage = AnimationUtils.loadAnimation(this, R.anim.trans_cancel_message);
        animationUp = AnimationUtils.loadAnimation(this, R.anim.up);

        cirImgCancel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw() {
                X[0] = cirImgCancel.getX();
                Y[0] = cirImgCancel.getY();
                cirImgCancel.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        cirImgYes.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw() {
                X[1] = cirImgYes.getX();
                Y[1] = cirImgYes.getY();
                cirImgYes.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        cirImgCancelMessage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
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

    private class PrivateOnTouchListener implements View.OnTouchListener{

        private PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
        private PointF StartPT = new PointF(); // Record Start Position of 'img'
        //private boolean i = true;
        private float y;
        private int flag;

        public PrivateOnTouchListener(int flag){

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

                    int a = (int)(view.getY() - Y[flag]);
                    if (a <= 0){
                        if (a >= -250){
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

                    if (view.getY() - Y[flag] < -250){

                        switch (flag){

                            case 0:
                                WaitHearPhoneActivity.this.finish();
                                break;

                            case 1:
                                view.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(WaitHearPhoneActivity.this, HearPhoneActivity.class);
                                startActivity(intent);
                                break;

                            case 2:
                                Toast.makeText(WaitHearPhoneActivity.this, "Đã gửi tin nhắn hủy cuộc gọi", Toast.LENGTH_LONG).show();
                                WaitHearPhoneActivity.this.finish();
                                break;

                        }

                    }
                    else {

                        view.setX(X[flag]);
                        view.setY(Y[flag]);
                        view.setImageAlpha(250);
                        setStart();
                    }
                    break;
            }

            return true;

        }

        public void setViewTouch(ImageView view){

            switch (flag){

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

        public void setInVisible(ImageView view1, ImageView view2){

            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
            imgUp.setVisibility(View.INVISIBLE);
        }

        public void setStart(){

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
}
