package me.argha.sustproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.registerNameEt) EditText name;
    @Bind(R.id.registerEmailEt) EditText emailEt;
    @Bind(R.id.registerPasswordEt) EditText passwordEt;
    @Bind(R.id.registerPasswordConfirmEt) EditText passwordConfirmEt;
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start new activity. Change from LoginActivity
                Intent intent= new Intent(context,LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}
