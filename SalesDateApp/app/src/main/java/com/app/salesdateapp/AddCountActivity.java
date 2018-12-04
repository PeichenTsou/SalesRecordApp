package com.app.salesdateapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.DBUtil.ImageUtil;
import com.app.DBUtil.MyDBHelper;

import java.util.List;
import java.util.Map;

public class AddCountActivity extends AppCompatActivity {
    String foodid = "";
    TextView textView_count, textView_food;
    int num1;
    ImageView imageView_addcount;
    Button button;
    private MyDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_count);

        imageView_addcount = (ImageView) findViewById(R.id.imageView_addcount);
        textView_food = (TextView) findViewById(R.id.textView_food);
        textView_count = (TextView) findViewById(R.id.textView_count);
        button = (Button) findViewById(R.id.button_add_count);
        mDBHelper = MyDBHelper.getInstance(getApplicationContext());

        getIntent();
        Intent intent = getIntent();
        foodid = intent.getStringExtra("foodid");
        Log.e("foodid", foodid);
        List<Map> map = mDBHelper.queryListMap("select * from food where id=?", new String[]{foodid});
        textView_food.setText(map.get(0).get("name").toString());
        imageView_addcount.setImageBitmap(ImageUtil.scaleBitmap(BitmapFactory.decodeFile(map.get(0).get("imagepath").toString()), 300, 300));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Map> map_count = mDBHelper.queryListMap("select * from count where foodid=?", new String[]{foodid});
                if (map_count.size() > 0) {
                    int old_count = Integer.valueOf(map_count.get(0).get("count").toString());
                    int new_count = old_count + Integer.valueOf(textView_count.getText().toString());
                    mDBHelper.update("count", new String[]{"count"}, new Object[]{new_count},
                            new String[]{"id"}, new String[]{map_count.get(0).get("id").toString()});
                } else {
                    int new_count = Integer.valueOf(textView_count.getText().toString());
                    mDBHelper.insert("count", new String[]{"foodid", "count"}, new Object[]{foodid, new_count});
                }
                finish();
            }
        });
    }


    public void iv_1(View view) {
        textView_count = (TextView) findViewById(R.id.textView_count);
        num1 = Integer.parseInt(textView_count.getText().toString());
        if (num1 > 1) {
            num1 -= 1;
        }
        textView_count.setText(Integer.toString(num1));
    }

    public void iv_2(View view) {
        textView_count = (TextView) findViewById(R.id.textView_count);
        num1 = Integer.parseInt(textView_count.getText().toString());
        if (num1 < 999) {
            num1 += 1;
        }
        textView_count.setText(Integer.toString(num1));
    }
}
