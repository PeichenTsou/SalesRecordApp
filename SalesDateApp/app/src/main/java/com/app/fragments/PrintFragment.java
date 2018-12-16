package com.app.fragments;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.DBUtil.MyDBHelper;
import com.app.model.CountItem;
import com.app.salesdateapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PrintFragment extends Fragment {
    private List<CountItem> countList = new ArrayList<CountItem>();
    private MyDBHelper mDBHelper;
    TextView tv;
    static String userid = "";

    public static PrintFragment newInstance(String param1) {
        PrintFragment fragment = new PrintFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        userid = param1;
        fragment.setArguments(args);
        return fragment;
    }

    public PrintFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.print_fragment, container, false);
        mDBHelper = MyDBHelper.getInstance(view.getContext().getApplicationContext());

        tv = (TextView) view.findViewById(R.id.container);
        tv.setText(initCounts());

        Button button_copy = (Button) view.findViewById(R.id.button_copy);
        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData myClip;
                String text = tv.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                ClipboardManager myClipboard;
                myClipboard = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(view.getContext(),"Copied",Toast.LENGTH_SHORT).show();
            }
        });
        Button button_clear = (Button) view.findViewById(R.id.button_clear);
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add delete data message
                AlertDialog diaBox = AskOptionToDeleteData(view);
                diaBox.show();
            }
        });
        return view;
    }

    //add delete data message
    private void deleteData(View view) {
        List<Map> list_food = mDBHelper.queryListMap("select * from food where userid=?", new String[]{userid});
        Log.e("delete",list_food.size()+"");
        for (int i = 0; i < list_food.size(); i++) {
            Log.e("delete",list_food.get(i).get("id").toString()+"   foodid");
//            mDBHelper.execSQL("delete From count where foodid=?", new String[]{list_food.get(i).get("id").toString()});
            //new
            mDBHelper.execSQL("delete From add_record where add_foodid=?", new String[]{list_food.get(i).get("id").toString()});
        }
        Toast.makeText(view.getContext(),"Cleared",Toast.LENGTH_SHORT).show();
        tv.setText("");
    }

    //add delete data message
    private AlertDialog AskOptionToDeleteData(final View view)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete the data? This can not be undone.")
                .setIcon(R.drawable.p3_delete_icon)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteData(view);
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

    private String initCounts() {

//        //old print fragment

//        countList.clear();
//        List<Map> list_food = mDBHelper.queryListMap("select * from food where userid=?", new String[]{userid});
//        for (int i = 0; i < list_food.size(); i++) {
//            List<Map> list_count = mDBHelper.queryListMap("select * from count where foodid=?", new String[]{list_food.get(i).get("id").toString()});
//            if (list_count.size() > 0) {
//                //add period & date
//                if (list_count.size() > 0) {
//                    for (int x = 0; x < list_count.size(); x++) {
//                        CountItem apple = new CountItem(list_count.get(x).get("date").toString(), list_count.get(x).get("period").toString(), list_food.get(i).get("dbfoodname").toString(), list_food.get(i).get("classification").toString(), list_food.get(i).get("category").toString(), list_count.get(x).get("count").toString());
//                        countList.add(apple);
//                    }
//                }
//            }
//        }

        countList.clear();
        List<Map> list_food = mDBHelper.queryListMap("select * from food where userid=?", new String[]{userid});
        for (int i = 0; i < list_food.size(); i++) {
            String a = list_food.get(i).get("id").toString();
            List<Map> list_count = mDBHelper.queryListMap("select SUM(add_count), add_date, add_period from add_record where add_foodid=? group by add_period, add_date", new String[]{list_food.get(i).get("id").toString()});
            if (list_count.size() > 0 && list_count.get(0).get("SUM(add_count)") != null) {
                for (int x = 0; x < list_count.size(); x++) {
                    CountItem apple1 = new CountItem(list_count.get(x).get("add_date").toString(),
                            list_count.get(x).get("add_period").toString(),
                            list_food.get(i).get("dbfoodname").toString(),
                            list_food.get(i).get("classification").toString(),
                            list_food.get(i).get("category").toString(),
                            list_count.get(x).get("SUM(add_count)").toString());
                    countList.add(apple1);
                }
            }
        }

        String str = "";
        for (int i = 0; i < countList.size(); i++) {
            //add period & date
            str = str + countList.get(i).getDate() + "\t" + countList.get(i).getPeriod() + "\t" + countList.get(i).getName() + "\t" + countList.get(i).getClassification() + "\t" + countList.get(i).getTime() + "\t" + countList.get(i).getCount() + "\n";
        }

        return str;
    }
}

