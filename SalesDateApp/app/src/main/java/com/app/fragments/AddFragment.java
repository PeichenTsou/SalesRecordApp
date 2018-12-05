package com.app.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.DBUtil.ImageUtil;
import com.app.DBUtil.MyDBHelper;
import com.app.salesdateapp.R;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {

    ImageView imageView;
    EditText editText_food_name;
    Spinner spinner_Classification;
    Spinner spinner_category;
    private MyDBHelper mDBHelper;
    View view;
    String picturePath = "";
    static String userid="";
    static public String[] spinnerItems_category = {"Savoury snacks",
            "Sweet Snacks",
            "Sandwiches, rolls and other bread products (eg garlic bread, hot cheese rolls)",
            "Fruit",
            "Yoghurt, icecream, and dairy desserts",
            "Drinks",
            "Hot dishes",
            "Other"}; //add category

    public final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    public static AddFragment newInstance(String param1) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        userid=param1;
        fragment.setArguments(args);
        return fragment;
    }

    public AddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_fragment, container, false);
        Bundle bundle = getArguments();

        mDBHelper = mDBHelper.getInstance(view.getContext().getApplicationContext());
        editText_food_name = (EditText) view.findViewById(R.id.editText_foodname);

        spinner_Classification = (Spinner) view.findViewById(R.id.spinner_Classification);
        String[] spinnerItems = {"Green", "Amber", "Red"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line, spinnerItems);
        spinner_Classification.setAdapter(spinnerAdapter);

        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);
        ArrayAdapter<String> spinnerAdapter_time = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line, spinnerItems_category);
        spinner_category.setAdapter(spinnerAdapter_time);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermission();
            }
        });

        Button button_add = (Button) view.findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText_food_name.getText().toString();
                String empty = "";
                if (!name.contentEquals(empty) ){
                    //name is not empty
                    String classification=spinner_Classification.getSelectedItem().toString();
                    String time=spinner_category.getSelectedItem().toString();
                    mDBHelper.insert("food",new String[]{"userid","dbfoodname","classification","category","imagepath"},new Object[]{userid,name,classification,time,picturePath});
                    Toast.makeText(view.getContext(), "Added", Toast.LENGTH_LONG).show();
                    editText_food_name.setText("");
                    imageView.setImageBitmap(null);
                    spinner_Classification.setSelection(0);
                    spinner_category.setSelection(0);
                }
                else {
                    AlertDialog diaBox = WarningForEmptyName();
                    diaBox.show();
                }
            }
        });
        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void getPermission() {
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请CAMERA的权限
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(view.getContext(), "请打开权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = view.getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(ImageUtil.scaleBitmap(BitmapFactory.decodeFile(picturePath),200,200));

        }
    }

    private AlertDialog WarningForEmptyName()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("The item name can't be empty!")
                .setMessage("Please enter item name.")
                .setIcon(R.drawable.p4_warning_icon)
                .setNegativeButton("Return", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu_addfragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog diaBox = WarningForDeleteWholeMenu();
        diaBox.show();

        return true;
    }

    private AlertDialog WarningForDeleteWholeMenu()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Reset Menu")
                .setMessage("Are you sure you want to delete ALL items in menu? This can not be undone.")
                .setIcon(R.drawable.p4_warning_icon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDBHelper.execSQL("delete from food");
                        dialog.dismiss();
                        Toast.makeText(view.getContext(),"Menu has been reset.",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}

