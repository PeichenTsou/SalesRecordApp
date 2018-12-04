package com.app.salesdateapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.app.DBUtil.MyDBHelper;

import java.util.List;
import java.util.Map;

public class ModifyPasswordActivity extends AppCompatActivity {

    private EditText editText_username_m_o,editText_username_m_n, editText_password_m_o, editText_password_m_n;
    private Button button_modify_m;
    private MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        myDBHelper = MyDBHelper.getInstance(getApplicationContext());
        editText_username_m_o = (EditText) findViewById(R.id.editText_username_m_o);
        editText_username_m_n = (EditText) findViewById(R.id.editText_username_m_n);
        editText_password_m_o = (EditText) findViewById(R.id.editText_password_m_o);
        editText_password_m_n = (EditText) findViewById(R.id.editText_password_m_n);
        button_modify_m = (Button) findViewById(R.id.button_modify_m);
        button_modify_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Map> list = myDBHelper.queryListMap("select * from user where name=? and password=?", new String[]{editText_username_m_o.getText().toString(), editText_password_m_o.getText().toString()});
                if (list.size() > 0) {
                    String userid = list.get(0).get("id").toString();
                    myDBHelper.update("user", new String[]{"name","password"}, new Object[]{editText_username_m_n.getText().toString(),editText_password_m_n.getText().toString()}, new String[]{"id"}, new String[]{userid});
                    Toast.makeText(ModifyPasswordActivity.this,"Modify Success",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ModifyPasswordActivity.this,"Invalid username or password",Toast.LENGTH_LONG).show();
            }
        });
    }
}
