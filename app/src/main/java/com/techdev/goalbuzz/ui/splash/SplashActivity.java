package com.techdev.goalbuzz.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.techdev.goalbuzz.R;
import com.techdev.goalbuzz.ui.main.MainActivity;

import static com.techdev.goalbuzz.util.Constant.SPLASH_DISPLAY_LENGTH;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
