package com.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.DBUtil.MyDBHelper;
import com.app.adaptors.CountAdaptor;
import com.app.model.CountItem;
import com.app.salesdateapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountFragment extends Fragment {
    private MyDBHelper mDBHelper;
    static String userid="";
    private List<CountItem> countList = new ArrayList<CountItem>();

    public static CountFragment newInstance(String param1) {
        CountFragment fragment = new CountFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        userid=param1;
        fragment.setArguments(args);
        return fragment;
    }

    public CountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.count_fragment, container, false);
        Bundle bundle = getArguments();
        mDBHelper = MyDBHelper.getInstance(view.getContext().getApplicationContext());

        initCounts();
        CountAdaptor adapter = new CountAdaptor(view.getContext(), R.layout.count_item, countList);
        ListView foodList = (ListView) view.findViewById(R.id.list_count);
        foodList.setAdapter(adapter);
        return view;
    }

    private void initCounts() {
        countList.clear();
        List<Map> list_food = mDBHelper.queryListMap("select * from food where userid=?", new String[]{userid});
        for (int i = 0; i < list_food.size(); i++) {
            List<Map> list_count = mDBHelper.queryListMap("select * from count where foodid=?", new String[]{list_food.get(i).get("id").toString()});
           if(list_count.size()>0)
           {
               //add period2
               for (int x = 0; x < list_count.size(); x++) {
                   CountItem apple = new CountItem(list_count.get(x).get("date").toString(), list_count.get(x).get("period").toString(), list_food.get(i).get("dbfoodname").toString(), list_food.get(i).get("classification").toString(), list_food.get(i).get("time").toString(), list_count.get(x).get("count").toString());
                   countList.add(apple);
               }

           }
        }
    }

}

