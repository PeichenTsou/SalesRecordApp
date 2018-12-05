package com.app.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    private List<Map> foodList_Category = new ArrayList<Map>();
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
        adapter = new FoodAdaptor(view.getContext(), R.layout.food_item, foodList_Category);
        foodListView = (GridView) view.findViewById(R.id.food_list);
        foodListView.setAdapter(adapter);

        //activate when click any item in menu
        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), AddCountActivity.class);
                intent.putExtra("foodid", foodList_Category.get(i).get("id").toString());
                startActivity(intent);
            }
        });

        this.registerForContextMenu(foodListView);
        return view;
    }

    //be called whenever initalizing or clicking "menu" on navigation
    private void initFoodSales() {
        foodList = mDBHelper.queryListMap("select * from food where userid=?", new String[]{userid});
        //make the menu be ordered by category
        String[] categoryNameArray = com.app.fragments.AddFragment.spinnerItems_category;
        for (int i=0; i< categoryNameArray.length; i++)
        {
            String categoryName = categoryNameArray[i];
            List<Map> resultByCategory = mDBHelper.queryListMap("select * from food where userid=? and category=?", new String[]{userid, categoryName});
            if(i == 0){
                foodList_Category = resultByCategory;
            }
            else {
                foodList_Category.addAll(resultByCategory);
            }
        }
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

                //add delete item message
                AlertDialog diaBox = AskOptionToDeleteItem();
                diaBox.show();

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem() {
        mDBHelper.execSQL("delete from food where id=?", new String[]{foodList.get(item_index).get("id").toString()});
        //
        String itemname = foodList.get(item_index).get("dbfoodname").toString();
        String messageText = "\"" + itemname + "\" was deleted" ;
        Toast.makeText(view.getContext(), messageText, Toast.LENGTH_SHORT).show();
        initFoodSales();
        adapter = new FoodAdaptor(view.getContext(), R.layout.food_item, foodList);
        foodListView.setAdapter(adapter);
    }

    //add delete item message
    private AlertDialog AskOptionToDeleteItem()
    {
        String itemname = foodList.get(item_index).get("dbfoodname").toString();
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete item \"" + itemname + "\"? This can not be undone.")
                .setIcon(R.drawable.p3_delete_icon)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteItem();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }


}

