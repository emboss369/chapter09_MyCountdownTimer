package com.example.username.mycountdowntimer;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mTimerText;
    SoundPool mSoundPool;
    int mSoundResId;
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            long minute = millisUntilFinished / 1000 / 60;
            long second = millisUntilFinished / 1000 % 60;
            mTimerText.setText(String.format("%1d:%2$02d",minute, second));
        }
        @Override
        public void onFinish() {
            mTimerText.setText("0:00");
            mSoundPool.play(mSoundResId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }
    MyCountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mTimerText = (TextView) findViewById(R.id.timerText);
        mTimer = new MyCountDownTimer(3 * 60 * 1000, 1000);
    }
    public void onStartButtonTapped(View view) {
        mTimer.start();
    }
    public void onStopButtonTapped(View view) {
        mTimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        }
        else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        mSoundResId = mSoundPool.load(this, R.raw.bellsound, 1);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSoundPool.release();
    }
}
