package me.argha.sustproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import me.argha.sustproject.helpers.HTTPHelper;
import me.argha.sustproject.helpers.PrefHelper;
import me.argha.sustproject.utils.AppURL;
import me.argha.sustproject.utils.Util;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.registerNameEt) EditText fullNameEt;
    @Bind(R.id.registerUserNameEt) EditText userNameEt;
    @Bind(R.id.registerPasswordEt) EditText passwordEt;
    @Bind(R.id.registerPasswordConfirmEt) EditText passwordConfirmEt;
    @Bind(R.id.registerPhoneEt) EditText phoneEt;
    @Bind(R.id.registerAddressEt) EditText addressEt;
    @Bind(R.id.registerDistrictSpinner)Spinner districtSpinner;
    @Bind(R.id.registerSubmitBtn) Button submitButton;
    Context context;
    PrefHelper prefHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registartion_layout);
        context=this;
        prefHelper=new PrefHelper(this);
        ButterKnife.bind(this);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userNameEt.getText().toString())) {
                    Util.showToast(context, "Please enter a username");
                } else if (TextUtils.isEmpty(fullNameEt.getText().toString())) {
                    Util.showToast(context, "Please enter your fullname");
                } else if (TextUtils.isEmpty(phoneEt.getText().toString())) {
                    Util.showToast(context, "Please enter your phone");
                } else if (TextUtils.isEmpty(addressEt.getText().toString())) {
                    Util.showToast(context, "Please enter an address");
                } else if (TextUtils.isEmpty(passwordEt.getText().toString())) {
                    Util.showToast(context, "Password cannot be vacant");
                } else if (TextUtils.isEmpty(passwordConfirmEt.getText().toString())) {
                    Util.showToast(context, "Please confirm your password");
                } else if (passwordEt.length() < 6) {
                    Util.showToast(context, "Passwords must be of at least 6 characters");
                } else if (!passwordEt.getText().toString().equals(passwordConfirmEt
                        .getText()
                        .toString())) {
                    Util.showToast(context, "Passwords don't match");
                }else if(districtSpinner.getSelectedItemPosition()==0) {
                    Util.showToast(context, "Please Choose a district");
                }else {

                    registerUser(fullNameEt.getText().toString(),
                            userNameEt.getText().toString(),
                            passwordEt.getText().toString(),
                            phoneEt.getText().toString(),
                            addressEt.getText().toString(),
                            getResources().getStringArray(R.array.districtsEnglish)[districtSpinner.getSelectedItemPosition()]);
                }
            }
        });
    }

    private void registerUser(String fullName, String userName, String password, String phone, String address, String district) {
        RequestParams params=new RequestParams();
        params.add("name",fullName);
        params.add("username",userName);
        params.add("password",password);
        params.add("phone",phone);
        params.add("address",address);
        params.add("district",district);

        final ProgressDialog dialog= Util.getProgressDialog(this,"Registering. Please wait...");
        AsyncHttpClient httpClient= HTTPHelper.getHTTPClient();
        httpClient.post(AppURL.REGISTER,params,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Util.printDebug("Register response",response.toString());
                try {
                    if(response.getBoolean("success")){
                        JSONObject resObject=response.getJSONObject("data");
                        prefHelper.saveUserId(resObject.getString("id"));
                        prefHelper.saveUserFullName(resObject.getString("name"));
                        prefHelper.saveUserName(resObject.getString("username"));
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Util.printDebug("Register fail",responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.cancel();
            }

        });
    }
}
