package me.argha.sustproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import me.argha.sustproject.helpers.MediaHelper;
import me.argha.sustproject.models.Category;
import me.argha.sustproject.utils.AppConst;
import me.argha.sustproject.utils.AppURL;
import me.argha.sustproject.utils.Util;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class AddNewItemFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.addItemExpireCheckBox)CheckBox hasExpire;
    @Bind(R.id.addItemSelectPhotoBtn)Button selectImageButton;
    @Bind(R.id.addItemSaveBtn)Button saveBtn;
    @Bind(R.id.addItemSelectImageNameTv)TextView selectImageName;
    @Bind(R.id.addItemMainCategorySpinner)Spinner mainCatSpinner;
    @Bind(R.id.addItemSubCategorySpinner) Spinner subCatSpinner;

    Uri fileUri;
    String filePath;
    String expireDate=null;
    AsyncHttpClient httpClient;
    ArrayList<Category> allCategories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.add_item_layout, container, false);
        ButterKnife.bind(this, root);
        selectImageButton.setOnClickListener(this);
        hasExpire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    showDateDialog();
                }
            }
        });
        httpClient=new AsyncHttpClient();
        getAllCategories();
        return root;
    }

    private void getAllCategories() {
        final ProgressDialog dialog=Util.getProgressDialog(getActivity(), "Loading Categories");
        httpClient.get(AppURL.CATEGORIES,null,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Util.printDebug("Category response",response.toString());
                try {
                    if(response.getBoolean("success")){
                        allCategories=new ArrayList<Category>();
                        JSONArray categoriesArray=response.getJSONArray("data");
                        for (int i=0;i<categoriesArray.length();i++){
                            JSONObject categoryObject=categoriesArray.getJSONObject(i);
                            Category mainCat=new Category();
                            mainCat.setBnName(categoryObject.getString("bn_name"));
                            mainCat.setEnName(categoryObject.getString("eng_name"));
                            ArrayList<Category> subCatList=new ArrayList<Category>();
                            JSONArray subCatArray=categoryObject.getJSONArray("sub_cat");
                            for (int j=0;j<subCatArray.length();j++){
                                JSONObject subCatObj=subCatArray.getJSONObject(j);
                                Category subCat=new Category();
                                subCat.setBnName(subCatObj.getString("bn_name"));
                                subCat.setEnName(subCatObj.getString("eng_name"));
                                subCatList.add(subCat);
                            }
                            mainCat.setSubCategory(subCatList);
                            allCategories.add(mainCat);
                        }

                        mainCatSpinner.setAdapter(new ArrayAdapter<Category>(getActivity(),android.R.layout.simple_spinner_item,allCategories));
                        mainCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                subCatSpinner.setAdapter(new ArrayAdapter<Category>(getActivity(),android.R.layout.simple_spinner_item,allCategories.get(i).getSubCategory()));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.printDebug("Category json excep",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Util.printDebug("category fail response",responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.cancel();
            }
        });
    }

    private void showDateDialog() {
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.form_date_time_picker_layout, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    expireDate=Util.parseDateTime(datePicker.getDayOfMonth(),datePicker.getMonth(),datePicker.getYear(),0,0);
                    hasExpire.setText("Expire: "+expireDate);
                }
            })
            .create();
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addItemSelectPhotoBtn:
                showImageChooseDialog();
                break;
        }
    }

    int select;
    public void showImageChooseDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        select=0;
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent takePicture = null;
                if (select == 0) {
                    openCamera();
                } else if (select == 1) {
                    takePicture = new Intent(Intent.ACTION_PICK);
                    takePicture.setType("image/*");
                    startActivityForResult(takePicture, AppConst.SELECT_PHOTO_GALLERY);
                }
                dialogInterface.dismiss();
            }
        });
        String choices[]={"Take a Photo","Select from Gallery"};
        alertDialog.setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                select = i;
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = MediaHelper.getOutputMediaFileUri(AppConst.SELECT_PHOTO_CAMERA);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, AppConst.SELECT_PHOTO_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case AppConst.SELECT_PHOTO_CAMERA:
                    filePath=fileUri.getPath();
                    break;
                case AppConst.SELECT_PHOTO_GALLERY:
                    fileUri=data.getData();
                    filePath=MediaHelper.getRealPathFromURI(getActivity(), fileUri);
                    break;
            }
            selectImageName.setText(filePath);
        }
    }
}
