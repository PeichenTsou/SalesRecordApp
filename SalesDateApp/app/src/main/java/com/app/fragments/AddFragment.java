package com.app.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
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
import com.app.salesdateapp.BuildConfig;
import com.app.salesdateapp.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AddFragment extends Fragment {

    ImageView imageView;
    EditText editText_food_name;
    Spinner spinner_Classification;
    Spinner spinner_category;
    private MyDBHelper mDBHelper;
    View view;
    String picturePath = "";
    Uri selectedTxtFileUri;
    static String userid = "";
    static public String[] spinnerItems_category = {"Savoury snacks",
            "Sweet Snacks",
            "Sandwiches, rolls and other bread products (eg garlic bread, hot cheese rolls)",
            "Fruit",
            "Yoghurt, icecream, and dairy desserts",
            "Drinks",
            "Hot dishes",
            "Other"}; //add category

    public final static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    public static AddFragment newInstance(String param1) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        userid = param1;
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

        //Click ADD TO THE MENU button
        Button button_add = (Button) view.findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText_food_name.getText().toString();
                String empty = "";
                if (!name.contentEquals(empty)) {
                    //name is not empty
                    String classification = spinner_Classification.getSelectedItem().toString();
                    String time = spinner_category.getSelectedItem().toString();
                    mDBHelper.insert("food", new String[]{"userid", "dbfoodname", "classification", "category", "imagepath"}, new Object[]{userid, name, classification, time, picturePath});
                    Toast.makeText(view.getContext(), "Added", Toast.LENGTH_LONG).show();
                    editText_food_name.setText("");
                    imageView.setImageBitmap(null);
//                    spinner_Classification.setSelection(0);
//                    spinner_category.setSelection(0);
                    picturePath = "";
                } else {
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
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            //To select pictures
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(view.getContext(), "请打开权限", Toast.LENGTH_LONG).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(view.getContext(), "Permission granted!", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(view.getContext(), "Permission denied!", Toast.LENGTH_LONG).show();
                }
                return;
            }
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

            imageView.setImageBitmap(ImageUtil.scaleBitmap(BitmapFactory.decodeFile(picturePath), 200, 200));
        }
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            selectedTxtFileUri = data.getData();
            ImportMenuFromTextFile();
            Toast.makeText(view.getContext(), "Menu Imported.", Toast.LENGTH_SHORT).show();
        }
    }

    private AlertDialog WarningForEmptyName() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
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
        switch (item.getItemId()) {
            case R.id.reset_menu_option:
                AlertDialog diaBox = WarningForDeleteWholeMenu();
                diaBox.show();
                return true;
            case R.id.export_menu_option:
                String menuStr = ExportMenuToString();
                if (menuStr.equals("Menu is empty.")){
                    AlertDialog diaBoxMenu = WarningForExportingEmptyMenu();
                    diaBoxMenu.show();
                }
                else {
                    SaveMenuToTextFileAndShare(menuStr);
                }
                return true;
            case R.id.import_menu_option:
                checkPermissionToRead();
                //select file
                Intent textIntent = new Intent(Intent.ACTION_GET_CONTENT);
                textIntent.setType("text/*"); // Set MIME type as per requirement
                startActivityForResult(textIntent,1);
                return true;
            default:
                return false;
        }
    }


    private String ExportMenuToString() {
        List<Map> list_menuExport = mDBHelper.queryListMap("select * from food where userid=?", new String[]{userid});
        if (list_menuExport.size() <= 0) {
            return "Menu is empty.";
        }
        String dbfoodnameStr;
        String classificationStr;
        String categoryStr;
        String imagepath;
        String str = "";
        for (int i = 0; i < list_menuExport.size(); i++) {
            dbfoodnameStr = list_menuExport.get(i).get("dbfoodname").toString();
            dbfoodnameStr = list_menuExport.get(i).get("dbfoodname").toString();
            classificationStr = list_menuExport.get(i).get("classification").toString();
            categoryStr = list_menuExport.get(i).get("category").toString();
            imagepath = list_menuExport.get(i).get("imagepath").toString();
            if (imagepath == null || imagepath.equals("")) {
                imagepath = "noPic";
            }
            else {
                Bitmap in = ImageUtil.scaleBitmap(BitmapFactory.decodeFile(imagepath), 200, 200);
                imagepath = bitmapToString(in);
            }
            str = str + dbfoodnameStr + "\t" + classificationStr + "\t" + categoryStr + "\t" + imagepath + "\\";
        }
        return str;
    }

    public void SaveMenuToTextFileAndShare (String menuStr) {
        if (menuStr.equals("")) {
            Toast.makeText(view.getContext(), "menuStr == empty", Toast.LENGTH_LONG).show();
        }
        else {
            checkPermissionToWrite();
            // Create the folder.
            String path = Environment.getExternalStorageDirectory() + File.separator + "FoodMenu";
            File folder = new File(path);
            folder.mkdirs();
            // Create the file.
            String fileName = "Menu";
            String dataToWrite = menuStr; // >> bitmap string
            File fileToWrite = new File(folder, fileName + ".txt");
            boolean deleted = fileToWrite.delete();
            try {
                if (fileToWrite.createNewFile()) {
                    FileOutputStream outPutStream = new FileOutputStream(fileToWrite); //Create a stream to file path
                    OutputStreamWriter outPutStreamWriter = new OutputStreamWriter(outPutStream); //Create Writer to write STream to file Path
                    outPutStreamWriter.append(dataToWrite); // Stream Byte Data to the file
                    outPutStreamWriter.close(); //Close Writer
                    outPutStream.flush(); //Clear Stream
                    outPutStream.close(); //Terminate STream
                } else {
                    Log.e(TAG, "Error: Failed to Create Log File");
                }
                //Share export menu(txt file)
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                Uri photoURI = FileProvider.getUriForFile(getActivity(),BuildConfig.APPLICATION_ID + ".provider", fileToWrite);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
//            sharingIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"user@gmail.com"}); //Receiver address
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Export Menu file");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Attached txt file can be used to import menu to new device.");
                startActivity(Intent.createChooser(sharingIntent, "share file with.."));
            } catch (Exception e)  {
                System.out.println("is exception raises during sending mail"+e);
                Toast.makeText(view.getContext(), "is exception raises during sending mail", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ImportMenuFromTextFile(){
        checkPermissionToWrite();

        Uri uri = selectedTxtFileUri;
        BufferedReader br = null;
        StringBuffer sb = null;
        String picBit = "";
        String imgPath = "";
        try {
            InputStream inputStream = view.getContext().getContentResolver().openInputStream(uri);
            InputStreamReader inputreader = new InputStreamReader(inputStream, "UTF-8");
            br = new BufferedReader(inputreader); //new
            String readline = "";
            sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                sb.append(readline);
            }
            br.close();

            String originalText = sb.toString();
            String separator = "\\";
            String[] items=originalText.replaceAll(Pattern.quote(separator), "\\\\").split("\\\\");

            for (int i = 0; i<items.length; i++){
                String[] itemDetails = items[i].split("\t");

                String itemName =  itemDetails[0];
                picBit = itemDetails[3];
                // Save picturese to local and get path
                String path = Environment.getExternalStorageDirectory() + File.separator + "FoodMenu/itemImages";
                // Create the folder.
                File folder = new File(path);
                folder.mkdirs();
                String fileName = itemName;
                if (picBit.equals("noPic")){
                    imgPath = "";
                }
                else {
                    Bitmap bitmapPic = stringToBitmap(picBit);
                    File filePath = new File(folder, fileName + ".png");
                    if (!filePath.exists() && bitmapPic != null) { // if file doesn't exist
                        FileOutputStream fos = new FileOutputStream(filePath);
                        bitmapPic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    }
                    imgPath = filePath.toString();
                    if (imgPath == null) {
                        imgPath = "";
                    }
                }
                //add to database
                mDBHelper.insert("food", new String[]{"userid", "dbfoodname", "classification", "category", "imagepath"}, new Object[]{userid, itemDetails[0], itemDetails[1], itemDetails[2], imgPath});
            }
            Toast.makeText(view.getContext(), "Import Menu successfully.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public final static String bitmapToString(Bitmap in) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
    }

    public final static Bitmap stringToBitmap(String in) {
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void checkPermissionToWrite() {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }else{
//            Toast.makeText(view.getContext(), "You already have permission to WRITE", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPermissionToRead() {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else{
//            Toast.makeText(view.getContext(), "You already have permission to READ", Toast.LENGTH_SHORT).show();
        }
    }

    private AlertDialog WarningForDeleteWholeMenu() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Reset Menu")
                .setMessage("Are you sure you want to delete ALL items in menu? This can not be undone.")
                .setIcon(R.drawable.p4_warning_icon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDBHelper.execSQL("delete from food");
                        dialog.dismiss();
                        Toast.makeText(view.getContext(), "Menu has been reset.", Toast.LENGTH_SHORT).show();
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

    private AlertDialog WarningForExportingEmptyMenu() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Menu is empy")
                .setMessage("You can not export empty menu.")
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