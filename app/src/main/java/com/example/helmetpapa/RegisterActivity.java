package com.example.helmetpapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //이름,id,password 주소값 가져오기.
        EditText ID=findViewById(R.id.etID);
        EditText PW=findViewById(R.id.etPW);
        DatePicker BIRTH=findViewById(R.id.dataPicker);
        EditText NAME=findViewById(R.id.etNAME);
        EditText PHONE=findViewById(R.id.etPHONE);

        //회원가입버튼 실행시 name,id,password 등의 데이터를 putExtra로 담음.
        Button signup_btn=findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePicker datePicker = (DatePicker) findViewById(R.id.dataPicker);
                int dayOfMonth = datePicker.getDayOfMonth();
                int month = datePicker.getMonth()+1;
                int year = datePicker.getYear();


                final String userID = ID.getText().toString();
                final String userBIRTH = year + "-" + month + "-" + dayOfMonth;
                final String userPW = PW.getText().toString();
                final String userNAME = NAME.getText().toString();
                final String userPHONE = PHONE.getText().toString();


                //이름,id,password의 입력칸이 하나라도 비었을시 알림이 뜸.=
                if(userID.equals("")||userPW.equals("")||userNAME.equals("")||userPHONE.equals("")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    //dialog.setIcon(R.mipmap.ic_launcher);
                    dialog.setTitle("알림");
                    dialog.setMessage("입력하지 않은 항목이 있습니다.");
                    dialog.setNegativeButton("확인", null);
                    dialog.show();
                }else{

                    String str;
                    String sendMsg, receiveMsg;
                    String result;

                    // 전송할 데이터. GET 방식으로 작성 하나의 QueryString로 생성
                    sendMsg = "userID=" + userID + "&userPW=" + userPW + "&userNAME=" + userNAME
                            + "&userBIRTH=" + userBIRTH + "&userPHONE=" + userPHONE;

                    /*AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    //dialog.setIcon(R.mipmap.ic_launcher);
                    dialog.setTitle("알림");
                    dialog.setMessage("회원 가입 데이터: " + sendMsg);
                    dialog.setNegativeButton("확인", null);
                    dialog.show();*/

                    RdbConnect task = new RdbConnect();
                    try {
                        result = task.execute(sendMsg).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //메인페이지로 넘어가기
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userPW", userPW);
                    intent.putExtra("userBIRTH", userBIRTH);
                    intent.putExtra("userNAME", userNAME);
                    intent.putExtra("userPHONE", userPHONE);
                    startActivityForResult(intent, RESULT_OK);

                }
            }
        });


    }


}