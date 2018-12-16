package com.app.salesdateapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.DBUtil.ImageUtil;
import com.app.DBUtil.MyDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        final Context context = this;

        getIntent();
        Intent intent = getIntent();
        foodid = intent.getStringExtra("foodid");
        Log.e("foodid", foodid);
        List<Map> map = mDBHelper.queryListMap("select * from food where id=?", new String[]{foodid});
        textView_food.setText(map.get(0).get("dbfoodname").toString());
        imageView_addcount.setImageBitmap(ImageUtil.scaleBitmap(BitmapFactory.decodeFile(map.get(0).get("imagepath").toString()), 300, 300));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add period
                int from = 1130; //lunch time *need to check
                int to = 1330;
                Calendar c = Calendar.getInstance();
                int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
                boolean isBetweenLunchTime = to > from && t >= from && t <= to || to < from && (t >= from || t <= to);

                String currentPeriod = "Recess";
                if(isBetweenLunchTime){
                    currentPeriod = "Lunch";
                }
                //add date
                Date currentDate = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(currentDate);

                // New count record for each adding action
//                SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                String formattedTime = tf.format(currentDate);

                //add period & date
//                List<Map> map_count = mDBHelper.queryListMap("select * from count where foodid=? and period=? and date=?", new String[]{foodid, currentPeriod, formattedDate});
                List<Map> map_count = mDBHelper.queryListMap("select SUM(add_count), add_date, add_period from add_record where add_foodid=? group by add_period, add_date", new String[]{foodid});
                //show how many items user buys
                List<Map> map_count_getName = mDBHelper.queryListMap("select dbfoodname from food where id=?", new String[]{foodid});
                String foodName = map_count_getName.get(0).get("dbfoodname").toString();

                int new_count = Integer.valueOf(textView_count.getText().toString());
                if (map_count.size() > 0 && map_count.get(0).get("SUM(add_count)") != null) {
                    int old_count = Integer.valueOf(map_count.get(0).get("SUM(add_count)").toString());
                    int update_count = old_count + new_count;
                    if (update_count >= 0){
//                        mDBHelper.update("count", new String[]{"count"}, new Object[]{update_count}, new String[]{"id"}, new String[]{map_count.get(0).get("id").toString()});
                        Toast.makeText(context,"You have added " + foodName + "*" + new_count + ".",Toast.LENGTH_LONG).show();
                        // New count record for each adding action
                        mDBHelper.insert("add_record", new String[]{"add_foodid", "add_count", "add_period", "add_date", "add_time"}, new Object[]{foodid, new_count, currentPeriod, formattedDate, formattedTime});
                        finish();
                    }
                    else {
                        AlertDialog diaBox = WarningForMinorRecord();
                        diaBox.show();
                    }
                } else { // if (map_count.size() < 0)
                    if (new_count > 0){
//                        mDBHelper.insert("count", new String[]{"foodid", "count", "period", "date"}, new Object[]{foodid, new_count, currentPeriod, formattedDate});
                        //show how many items user buys
                        Toast.makeText(context,"You have added " + foodName + "*" + new_count + ".",Toast.LENGTH_LONG).show();
                        // New count record for each adding action
                        mDBHelper.insert("add_record", new String[]{"add_foodid", "add_count", "add_period", "add_date", "add_time"}, new Object[]{foodid, new_count, currentPeriod, formattedDate, formattedTime});
                        finish();
                    }
                    else {
                        AlertDialog diaBox = WarningForMinorRecord();
                        diaBox.show();
                    }
                }
            }
        });
    }

    public void iv_1(View view) {
        textView_count = (TextView) findViewById(R.id.textView_count);
        num1 = Integer.parseInt(textView_count.getText().toString());
        if (num1 == 1) {
            num1 -= 2;
        }
        else {
            num1 -= 1;
        }
        textView_count.setText(Integer.toString(num1));
    }

    public void iv_2(View view) {
        textView_count = (TextView) findViewById(R.id.textView_count);
        num1 = Integer.parseInt(textView_count.getText().toString());
        if (num1 < 999) {
            if (num1 == -1){
                num1 += 2;
            }
            else {
                num1 += 1;
            }
        }
        textView_count.setText(Integer.toString(num1));
    }

    private AlertDialog WarningForMinorRecord()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("You can not have minor amount of item in the record!")
                .setMessage("Nothing was added.")
                .setIcon(R.drawable.p4_warning_icon)
                .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}