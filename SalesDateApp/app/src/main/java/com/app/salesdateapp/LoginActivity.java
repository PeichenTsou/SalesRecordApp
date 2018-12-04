package com.app.salesdateapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.DBUtil.MyDBHelper;

import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button tb_signin,button_modify;
    private MyDBHelper mDBHelper;
    private EditText editText_username, editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tb_signin = (Button) findViewById(R.id.button_signin);
        button_modify=(Button) findViewById(R.id.button_modify);
        mDBHelper = MyDBHelper.getInstance(getApplicationContext());
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);

        List<Map> user1 = mDBHelper.queryListMap("select * from user where name=? and password=?", new String[]{"user1", "1111"});
        if (user1.size() <= 0) {
            mDBHelper.insert("user", new String[]{"name", "password"}, new Object[]{"user1", "1111"});
        }
        List<Map> user2 = mDBHelper.queryListMap("select * from user where name=? and password=?", new String[]{"user2", "1111"});
        if (user2.size() <= 0) {
            mDBHelper.insert("user", new String[]{"name", "password"}, new Object[]{"user2", "1111"});
        }

        tb_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Map> user1 = mDBHelper.queryListMap("select * from user where name=? and password=?", new String[]{editText_username.getText().toString(), editText_password.getText().toString()});
                if (user1.size() > 0) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userid", user1.get(0).get("id").toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Invalid username or password",Toast.LENGTH_LONG).show();
                }
            }
        });

        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
