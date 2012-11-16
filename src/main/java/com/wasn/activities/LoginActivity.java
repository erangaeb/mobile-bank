package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.User;
import com.wasn.utils.LoginUtils;

/**
 * Activity class correspond to login
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // form components
    EditText usernameText;
    EditText passwordText;
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

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) LoginActivity.this.getApplication();

        usernameText = (EditText) findViewById(R.id.login_layout_username);
        passwordText = (EditText) findViewById(R.id.login_layout_password);
        login = (RelativeLayout) findViewById(R.id.login_layout_login);

        login.setOnClickListener(LoginActivity.this);

        System.out.println("login state " + application.getMobileBankData().getLoginState());
        application.getMobileBankData().setLoginState("1");
        System.out.println("login state " + application.getMobileBankData().getLoginState());
    }

    /**
     * login process
     * validate fields
     * start background thread to authenticate user
     */
    public void login() {
        // get texts and create user
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        User user = new User(username, password);

        // validate form fields
        try {
            LoginUtils.validateFields(user);

            startActivity(new Intent(LoginActivity.this, DownloadActivity.class));
            LoginActivity.this.finish();

            // todo - start background thread to authenticate user
        } catch (IllegalArgumentException e) {
            displayMessageDialog("Error", "Empty fields, make sure not empty username and password");
        }
    }

    /**
     * Display message dialog
     *
     * @param messageHeader message header
     * @param message message to be display
     */
    public void displayMessageDialog(String messageHeader, String message) {
        final Dialog dialog = new Dialog(LoginActivity.this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.message_dialog_layout_message_text);
        messageHeaderTextView.setText(messageHeader);
        messageTextView.setText(message);

        // set custom font
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        messageHeaderTextView.setTypeface(face);
        messageHeaderTextView.setTypeface(null, Typeface.BOLD);
        messageTextView.setTypeface(face);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.message_dialog_layout_yes_button);
        okButton.setTypeface(face);
        okButton.setTypeface(null, Typeface.BOLD);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == login) {
            login();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        // exit
        LoginActivity.this.finish();
    }
}
