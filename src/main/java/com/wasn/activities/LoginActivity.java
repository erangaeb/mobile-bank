package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.User;
import com.wasn.services.backgroundservices.UserAuthenticateService;
import com.wasn.utils.LoginUtils;
import com.wasn.utils.NetworkUtil;

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

    // display when authenticating
    public ProgressDialog progressDialog;

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

            // connect if only available network connection
            if(NetworkUtil.isAvailableNetwork(LoginActivity.this)) {
                // start background thread to authenticate user
                progressDialog = ProgressDialog.show(LoginActivity.this, "", "Authenticating user, Please wait");
                new UserAuthenticateService(LoginActivity.this).execute(username, password);
            } else {
                displayToast("No network connection");
            }
        } catch (IllegalArgumentException e) {
            displayMessageDialog("Error", "Empty fields, make sure not empty username and password");
        }
    }

    /**
     * Close progress dialog
     */
    public void closeProgressDialog() {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }

    /**
     * execute after login
     * @param status login status
     */
    public void onPostLogin(String status) {
        closeProgressDialog();

        // display toast according to download status
        if(status.equals("1")) {
            // login success
            startActivity(new Intent(LoginActivity.this, DownloadActivity.class));
            LoginActivity.this.finish();
        } else if(status.equals("0")) {
            // login fail
            displayToast("Authentication fails");
        } else if(status.equals("-2")) {
            // error in server response
            displayToast("Server response error");
        } else {
            // cannot process request
            displayToast("Cannot process request");
        }
    }

    /**
     * Display toast message
     * @param message message tobe display
     */
    public void displayToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
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
