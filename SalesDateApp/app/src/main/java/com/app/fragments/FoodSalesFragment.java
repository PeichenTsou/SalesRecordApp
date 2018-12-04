package com.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.DBUtil.MyDBHelper;
import com.app.adaptors.FoodAdaptor;
import com.app.salesdateapp.AddCountActivity;
import com.app.salesdateapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class FoodSalesFragment extends Fragment {
    private MyDBHelper mDBHelper;
    private List<Map> foodList = new ArrayList<Map>();
//    ListView foodListView;
    GridView foodListView;
    static String userid = "";
    View view;
    FoodAdaptor adapter;

    int item_index = -1;

    public static FoodSalesFragment newInstance(String param1) {
        FoodSalesFragment fragment = new FoodSalesFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        Log.e("FoodSales_userid", param1);
        userid = param1;
        fragment.setArguments(args);
        return fragment;
    }

    public FoodSalesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.foodsales_fragment, container, false);
        mDBHelper = MyDBHelper.getInstance(view.getContext().getApplicationContext());

        initFoodSales();
        adapter = new FoodAdaptor(view.getContext(), R.layout.food_item, foodList);
        foodListView = (GridView) view.findViewById(R.id.food_list);
        foodListView.setAdapter(adapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), AddCountActivity.class);
                intent.putExtra("foodid", foodList.get(i).get("id").toString());
                startActivity(intent);
            }
        });

        this.registerForContextMenu(foodListView);
        return view;
    }

    private void initFoodSales() {
        foodList = mDBHelper.queryListMap("select * from food where userid=?", new String[]{userid});
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // set context menu title
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        item_index = info.position;
        menu.add(0, 1, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                mDBHelper.execSQL("delete from food where id=?", new String[]{foodList.get(item_index).get("id").toString()});
                Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                initFoodSales();
                adapter = new FoodAdaptor(view.getContext(), R.layout.food_item, foodList);
                foodListView.setAdapter(adapter);
                break;
        }
        return super.onContextItemSelected(item);
    }
}

