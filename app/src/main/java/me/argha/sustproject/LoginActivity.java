package me.argha.sustproject;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    Context context;
    @Bind(R.id.loginUserNameEt) EditText mEmailEt;
    @Bind(R.id.loginInputEmailLayout)TextInputLayout mEmailEtLayout;
    @Bind(R.id.loginPasswordEt) EditText mPasswordEt;
    @Bind(R.id.loginRegisterBtn) Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,RegisterActivity.class));
            }
        });
    }

}
