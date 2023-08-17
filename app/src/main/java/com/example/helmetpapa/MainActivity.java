package com.example.helmetpapa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    //EditText LID=findViewById(R.id.login_id);
    //EditText LPW=findViewById(R.id.login_pw);
    //private EditText login_id, login_pw;
    //private Button login_btn, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //현재화면이 로그인화면

        EditText LID=findViewById(R.id.login_id);
        EditText LPW=findViewById(R.id.login_pw);

        //회원가입 버튼 누르면 회원가입 화면으로
        Button register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        //로그인 버튼 누르기
        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userID = LID.getText().toString();
                String userPW = LPW.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            //로그인 시 아이디, 비번 어떻게 입력되나 확인하는 팝업
//                            String sendMsg;
//                            sendMsg = "userID=" + userID + "&userPW=" + userPW;
//
//                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                            //dialog.setIcon(R.mipmap.ic_launcher);
//                            dialog.setTitle("알림");
//                            dialog.setMessage("로그인 데이터: " + sendMsg);
//                            dialog.setNegativeButton("확인", null);
//                            dialog.show();


                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {//로그인 성공시

                                String userID = jsonResponse.getString("userID");
                                String userPW = jsonResponse.getString("userPW");

                                Toast.makeText(getApplicationContext(), String.format("%s님 환영합니다.", userID), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), CameraBtnActivity.class); //카메라실행버튼화면으로 이동

                                intent.putExtra("userID", userID);
                                intent.putExtra("userPW", userPW);

                                startActivity(intent);

                            } else {//로그인 실패시
                                Toast.makeText(getApplicationContext(), "존재하지 않는 회원입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //Intent i = new Intent(getApplicationContext(), CameraActivity.class); //로그인 성공 시
                            //startActivity(i);
                        } catch (JSONException e) {
                            Log.e("JSON ERROR==>", e.getMessage()); //로그로 에러메세지 출력
                            e.printStackTrace();

                        }
                    }
                };
                //서버로 Volley를 이용해서 요청
                LoginRequest loginRequest = new LoginRequest( userID, userPW, responseListener );
                RequestQueue queue = Volley.newRequestQueue( MainActivity.this);
                queue.add( loginRequest );
            }
        });
    }
}