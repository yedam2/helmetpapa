package com.example.helmetpapa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//인트로 화면
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(1500); //대기 초 설정
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}