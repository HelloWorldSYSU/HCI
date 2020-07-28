package com.sysumach.voiceindentify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class SplashActivity extends Activity {

    public static class mHandler extends Handler {

        private WeakReference<SplashActivity> mActivty;

        public mHandler(SplashActivity splashActivity) {
            mActivty = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mActivty.get().finish();
            mActivty.get().startActivity(new Intent(mActivty.get(), MainActivity.class));

        }
    }

    private mHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        handler = new mHandler(this);
        handler.sendMessageDelayed(Message.obtain(), 2000);
    }
}
