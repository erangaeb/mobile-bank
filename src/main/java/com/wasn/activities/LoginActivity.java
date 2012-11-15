package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created with IntelliJ IDEA.
 * User: eranga
 * Date: 11/15/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    RelativeLayout login;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        init();
    }

    public void init() {
        login = (RelativeLayout) findViewById(R.id.login_layout_login);
        login.setOnClickListener(LoginActivity.this);
    }

    public void onClick(View view) {
        if(view == login) {
            startActivity(new Intent(LoginActivity.this, MobileBankActivity.class));
            LoginActivity.this.finish();
        }
    }
}
