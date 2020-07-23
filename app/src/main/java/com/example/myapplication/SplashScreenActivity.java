package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getWindow().setStatusBarColor(Color.BLACK);

        new CountDownTimer(1000, 60){

            @Override
            public void onTick(long ms){
                counter++;
            }

            @Override
            public void onFinish(){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        SplashScreenActivity.this.startActivity(intent);
                        SplashScreenActivity.this.finish();

                    }
                }, 900);
            }
        }.start();

    }

}
