package me.argha.sustproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registartion_layout);
        context=this;
        ButterKnife.bind(this);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
