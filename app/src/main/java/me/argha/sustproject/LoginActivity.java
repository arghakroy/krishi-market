package me.argha.sustproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import me.argha.sustproject.utils.AppConst;
import me.argha.sustproject.utils.AppURL;
import me.argha.sustproject.utils.Util;

public class LoginActivity extends AppCompatActivity {


    Context context;
    AsyncHttpClient httpClient;
    @Bind(R.id.loginUserNameEt) EditText mEmailEt;
    @Bind(R.id.loginInputEmailLayout)TextInputLayout mEmailEtLayout;
    @Bind(R.id.loginPasswordEt) EditText mPasswordEt;
    @Bind(R.id.loginRegisterBtn) Button registerBtn;
    @Bind(R.id.loginSubmitBtn) Button loginBtn;

    PrefHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        context=this;
        prefHelper=new PrefHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        httpClient= HTTPHelper.getHTTPClient();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context, RegisterActivity.class), AppConst.REGISTER_REQUEST);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(mEmailEt.getText().toString(),mPasswordEt.getText().toString());
            }
        });
    }

    private void loginUser(String username, String password) {
        RequestParams params=new RequestParams();
        params.add("username",username);
        params.add("password", password);
        final ProgressDialog dialog= Util.getProgressDialog(context,"Please wait...");
        httpClient.post(AppURL.LOGIN,params,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Util.printDebug("Login response",response.toString());
                try {
                    if(response.getBoolean("success")){
                        Util.printDebug("Success","");

                        JSONObject resObject=response.getJSONObject("data");
                        prefHelper.saveUserId(resObject.getString("id"));
                        prefHelper.saveUserFullName(resObject.getString("name"));
                        prefHelper.saveUserName(resObject.getString("username"));
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }else {
                        Util.showToast(context,"Username and Password Doesn't match");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.printDebug("Json erro",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Util.printDebug("Login fail", responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AppConst.REGISTER_REQUEST && resultCode==RESULT_OK){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
