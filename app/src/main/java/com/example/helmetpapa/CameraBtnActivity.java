package com.example.helmetpapa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CameraBtnActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button galleryBtn = findViewById(R.id.galleryBtn);
//        galleryBtn.setOnClickListener(view -> {
//            Intent i = new Intent(MainActivity.this, GalleryActivity.class);
//            startActivity(i);
//        });

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        TextView userid = (TextView) findViewById(R.id.userid);
        userid.setText(userID + " 반갑습니다.");

        Button cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(view -> {

            Intent i = new Intent(CameraBtnActivity.this, CameraActivity.class);
            i.putExtra("userID", userID);
            startActivity(i);
        });
    }

}
