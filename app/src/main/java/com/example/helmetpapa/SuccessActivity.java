package com.example.helmetpapa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        TextView userid = (TextView) findViewById(R.id.userid);
        userid.setText(userID + " 님 대여 완료 되었습니다.");




    }
}
