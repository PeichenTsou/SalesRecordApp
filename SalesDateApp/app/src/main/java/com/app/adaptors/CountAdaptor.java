package com.app.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.salesdateapp.R;
import com.app.model.CountItem;

import java.util.List;

public class CountAdaptor extends ArrayAdapter {
    private final int resourceId;

    public CountAdaptor(Context context, int textViewResourceId, List<CountItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CountItem countItem = (CountItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView count_item_date = (TextView) view.findViewById(R.id.count_item_date);
        TextView count_item_period = (TextView) view.findViewById(R.id.count_item_period);
        TextView count_item_name = (TextView) view.findViewById(R.id.count_item_name);
        TextView count_item_classification = (TextView) view.findViewById(R.id.count_item_classification);
        TextView count_item_time = (TextView) view.findViewById(R.id.count_item_time);
        TextView count_item_count = (TextView) view.findViewById(R.id.count_item_count);

        count_item_date.setText(countItem.getDate());
        count_item_period.setText(countItem.getPeriod());
        count_item_name.setText(countItem.getName());
        count_item_classification.setText(countItem.getClassification());
        count_item_time.setText(countItem.getTime());
        count_item_count.setText(countItem.getCount()+"");
        return view;
    }
}