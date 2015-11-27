package me.argha.sustproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import me.argha.sustproject.helpers.HTTPHelper;
import me.argha.sustproject.helpers.MediaHelper;
import me.argha.sustproject.helpers.PrefHelper;
import me.argha.sustproject.utils.AppConst;
import me.argha.sustproject.utils.AppURL;
import me.argha.sustproject.utils.Util;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class MyProfileFragment extends Fragment {

    @Bind(R.id.myProfileNameEt) EditText nameEt;
    @Bind(R.id.myProfileUserNameEt) EditText userNameEt;
    @Bind(R.id.myProfilePasswordEt) EditText passwordEt;
    @Bind(R.id.myProfileConfirmPassword) EditText confirmPassEt;
    @Bind(R.id.myProfilePhoneEt) EditText phoneEt;
    @Bind(R.id.myProfileAddressEt) EditText addressEt;
    @Bind(R.id.myProfileDistrictSpinner)Spinner districtSpinner;
    @Bind(R.id.myProfileUpdateBtn) Button updateBtn;
    @Bind(R.id.changePictureImgBtn) ImageButton profilePicBtn;

    PrefHelper prefHelper;


    Uri fileUri;
    String filePath;
    AsyncHttpClient httpClient;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.my_profile_layout,container,false);
        ButterKnife.bind(this, root);
        prefHelper=new PrefHelper(getActivity());
        httpClient= HTTPHelper.getHTTPClient();

        profilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooseDialog();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateUserProfile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        getUserDetailsData();
        return root;
    }

    private void updateUserProfile() throws FileNotFoundException {
        RequestParams params=new RequestParams();
        params.add("user_id",prefHelper.getUserId());
        params.add("name",nameEt.getText().toString());
        params.add("username",userNameEt.getText().toString());
        params.add("password",passwordEt.getText().toString());
        params.add("phone",phoneEt.getText().toString());
        params.add("address",phoneEt.getText().toString());
        params.add("district",getResources().getStringArray(R.array.districtsEnglish)[districtSpinner.getSelectedItemPosition()]);
        if(filePath!=null && filePath.length()>2){
            params.put("photo",new File(filePath));
        }

        final ProgressDialog dialog=Util.getProgressDialog(getActivity(),"Updating. Please wait...");

        httpClient.post(AppURL.UPDATE_PROFILE,params,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Util.printDebug("Update response",response.toString());
                try {
                    if(response.getBoolean("success")){
                        Util.showToast(getActivity(),"Profile Update Successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Util.printDebug("Response error",responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }

    private void getUserDetailsData() {
        RequestParams params=new RequestParams();
        params.add("user_id", prefHelper.getUserId());
        final ProgressDialog dialog= Util.getProgressDialog(getActivity(),"Getting Details. Please wait...");
        httpClient.post(AppURL.USER_PROFILE, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Util.printDebug("User details", response.toString());
                try {
                    if(response.getBoolean("success")){
                        JSONObject data=response.getJSONObject("data");
                        nameEt.setText(data.getString("name"));
                        userNameEt.setText(data.getString("username"));
                        phoneEt.setText(data.getString("phone"));
                        addressEt.setText(data.getString("address"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.printDebug("Json error",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Util.printDebug("User fail", responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
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
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            profilePicBtn.setImageBitmap(myBitmap);
        }
    }
}
