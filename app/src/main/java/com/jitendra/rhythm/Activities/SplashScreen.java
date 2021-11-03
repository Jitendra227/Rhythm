package com.jitendra.rhythm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jitendra.rhythm.R;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    private static int WELCOME_TIMEOUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: <----Entered Successfully---Splash Screen--->");
                Intent intent = new Intent(SplashScreen.this, LoginScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }, WELCOME_TIMEOUT);
    }
}
