package com.example.helmetpapa;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.helmetpapa.tflite.ClassifierWithModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    public static final String TAG = "[IC]CameraActivity";
    public static final int CAMERA_IMAGE_REQUEST_CODE = 1;
    private static final String KEY_SELECTED_URI = "KEY_SELECTED_URI";


    private ClassifierWithModel cls;
    private ImageView imageView;
    private TextView textView;
    //private int mCameraFacing;

    Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //mCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        TextView userid = (TextView) findViewById(R.id.userid);
        userid.setText(userID + " 반갑습니다.");



        //EditText ID=findViewById(R.id.etID);


        //넥스트 버튼 누르면 대여완료 페이지로 이동
        Button next_btn = findViewById(R.id.nextBtn);
        next_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String sendMsg;

                // 전송할 데이터. GET 방식으로 작성 하나의 QueryString로 생성
                sendMsg = "userID=" + userID;

                RentalConnect task = new RentalConnect();
                try {

                    task.execute(sendMsg).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getApplicationContext(), SuccessActivity.class);
                i.putExtra("userID", userID);

                startActivity(i);


            }
        });

        Button takeBtn = findViewById(R.id.takeBtn);
        takeBtn.setOnClickListener(v -> getImageFromCamera());

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);


        cls = new ClassifierWithModel(this);
        try {
            cls.init();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if(savedInstanceState != null) {
            Uri uri = savedInstanceState.getParcelable(KEY_SELECTED_URI);
            if (uri != null)
                selectedImageUri = uri;
        }
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_SELECTED_URI, selectedImageUri);
    }

    private void getImageFromCamera(){
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "picture.jpg");
        selectedImageUri = FileProvider.getUriForFile(this, getPackageName(), file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        //intent.putExtra("android.intent.extra.CAMERA_FACING", 1);
        intent.putExtra("android.intent.extra.LENS_FACING_FRONT", 1);
        //intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        startActivityForResult(intent, CAMERA_IMAGE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK &&
                requestCode == CAMERA_IMAGE_REQUEST_CODE) {

            Bitmap bitmap = null;
            try {
                if(Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source src = ImageDecoder.createSource(
                            getContentResolver(), selectedImageUri);
                    bitmap = ImageDecoder.decodeBitmap(src);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), selectedImageUri);
                }
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to read Image", ioe);
            }

            //% 따라서 넥스트 버튼 활성화
            Button next_btn = findViewById(R.id.nextBtn);

            if(bitmap != null) {

                Pair<String, Float> output = cls.classify(bitmap);
                String resultStr = String.format(Locale.ENGLISH,
                        "class : %s, prob : %.2f%%",
                        output.first, output.second * 100);

                imageView.setImageBitmap(bitmap);
                textView.setText(resultStr);
                next_btn.setEnabled(true);

            }else{

                //% 따라서 넥스트 버튼 비활성화
                next_btn.setEnabled(false);


            }

        }
    }


    @Override
    protected void onDestroy() {
        cls.finish();
        super.onDestroy();
    }

}