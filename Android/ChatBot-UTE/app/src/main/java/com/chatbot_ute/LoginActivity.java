package com.chatbot_ute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtMSV, edtPW;
    TextView tvError;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        //Lấy giá trị preferences
        edtMSV.setText(preferences.getString("msv",""));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msv = edtMSV.getText().toString().trim();
//                String pw = edtPW.getText().toString().trim();

                if (isNumeric(msv) && msv.length() == 13) {
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                    tvError.setText("");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("msv",msv);
//                    editor.putString("")
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else
                {
                    tvError.setText("Mã Sinh Viên không đúng!! Vui lòng nhập lại");
                }

            }
        });
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        edtMSV = findViewById(R.id.edt_msv);
//        edtPW = findViewById(R.id.edt_pw);
        tvError = findViewById(R.id.tv_error);
    }
    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}