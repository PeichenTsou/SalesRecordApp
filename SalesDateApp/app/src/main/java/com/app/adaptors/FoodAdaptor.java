package com.app.adaptors;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.DBUtil.ImageUtil;
import com.app.salesdateapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAdaptor extends ArrayAdapter {
    private final int resourceId;
    public FoodAdaptor(Context context, int textViewResourceId, List<Map> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,Object> food_item =  (HashMap<String,Object>) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        ImageView foodImage = (ImageView) view.findViewById(R.id.fooditem_food_image);
        TextView foodName = (TextView) view.findViewById(R.id.fooditem_food_name);
        String foodImagePath = food_item.get("imagepath").toString();
        if (foodImagePath == null){
            foodImagePath = "";
        }
        foodImage.setImageBitmap(ImageUtil.scaleBitmap(BitmapFactory.decodeFile(foodImagePath),300,300));
//        foodImage.setImageBitmap(ImageUtil.scaleBitmap(BitmapFactory.decodeFile(food_item.get("imagepath").toString()),300,300));
        foodName.setText(food_item.get("dbfoodname").toString());
        return view;
    }
}
