package com.app.salesdateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

import com.app.fragments.AddFragment;
import com.app.fragments.CountFragment;
import com.app.fragments.FoodSalesFragment;
import com.app.fragments.PrintFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private FoodSalesFragment foodSalesFragment;
    private CountFragment countFragment;
    private PrintFragment printFragment;
    private AddFragment addFragment;

    private String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIntent();
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        Log.e("userid", userid);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#008577")
                .setBarBackgroundColor("#ffffff");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.menu, "Menu"))
                .addItem(new BottomNavigationItem(R.mipmap.count, "Data"))
                .addItem(new BottomNavigationItem(R.mipmap.print, "Export"))
                .addItem(new BottomNavigationItem(R.mipmap.add, "Add"))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();
        setDefaultFragment();

    }

    /**
     * 设置默认导航栏
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        foodSalesFragment = FoodSalesFragment.newInstance(userid);
        transaction.replace(R.id.tb, foodSalesFragment);
        transaction.commit();
    }

    /**
     * 设置导航选中的事件
     */
    @Override
    public void onTabSelected(int position) {
        Log.d(TAG, "onTabSelected() called with: "
                + "position = [" + position + "]");
        FragmentManager fm = this.getFragmentManager(); //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (foodSalesFragment == null) {
                    foodSalesFragment = FoodSalesFragment.newInstance(userid);
                }
                transaction.replace(R.id.tb, foodSalesFragment);
                setTitle("FoodSales");
                break;
            case 1:
                if (countFragment == null) {
                    countFragment = countFragment.newInstance(userid);
                }
                transaction.replace(R.id.tb, countFragment);
                setTitle("Count");
                break;
            case 2:
                if (printFragment == null) {
                    printFragment = PrintFragment.newInstance(userid);
                }
                transaction.replace(R.id.tb, printFragment);
                setTitle("Print");
                break;
            case 3:
                if (addFragment == null) {
                    addFragment = AddFragment.newInstance(userid);
                }
                transaction.replace(R.id.tb, addFragment);
                setTitle("Add Food");
                break;
            default:
                break;
        }
        transaction.commit();// 事务提交
    }

    /**
     * 设置未选中Fragment 事务
     */
    @Override
    public void onTabUnselected(int position) {
    }

    /**
     * 设置释放Fragment 事务
     */
    @Override
    public void onTabReselected(int position) {
    }


}
