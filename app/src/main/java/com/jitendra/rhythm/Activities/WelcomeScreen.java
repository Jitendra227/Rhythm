package com.jitendra.rhythm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jitendra.rhythm.R;

public class WelcomeScreen extends AppCompatActivity {

    private static final String TAG = "WelcomeScreen";
    private FirebaseUser firebaseUser;
    private Button joinNowBtn,signInBtn;

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        Log.d(TAG, "onStart: <---Last Logged in user checking--->>");
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        joinNowBtn = findViewById(R.id.join_btn);
        signInBtn = findViewById(R.id.sign_in_button);

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinNowIntent = new Intent(WelcomeScreen.this, RegisterScreenActivity.class);
                startActivity(joinNowIntent);
                finish();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(WelcomeScreen.this, LoginScreenActivity.class);
                startActivity(signInIntent);
                finish();
            }
        });
    }
}
