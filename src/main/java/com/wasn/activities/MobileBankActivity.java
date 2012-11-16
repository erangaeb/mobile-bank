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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wasn.application.MobileBankApplication;

/**
 * Main activity class of the application
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class MobileBankActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // activity components
    Button transactionButton;
    Button summaryButton;
    Button settingsButton;
    RelativeLayout logout;
    TextView logoutText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_bank_layout);

        init();
    }

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) MobileBankActivity.this.getApplication();

        transactionButton = (Button) findViewById(R.id.mobile_bank_layout_transaction_button);
        summaryButton = (Button) findViewById(R.id.mobile_bank_layout_summary_button);
        settingsButton = (Button) findViewById(R.id.mobile_bank_layout_settings_button);
        logout = (RelativeLayout) findViewById(R.id.mobile_bank_layout_logout);

        // set custom font
        logoutText = (TextView) findViewById(R.id.mobile_bank_layout_logout_text);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        logoutText.setTypeface(face);
        //logoutText.setTypeface(null, Typeface.BOLD);

        transactionButton.setOnClickListener(MobileBankActivity.this);
        summaryButton.setOnClickListener(MobileBankActivity.this);
        settingsButton.setOnClickListener(MobileBankActivity.this);
        logout.setOnClickListener(MobileBankActivity.this);
    }

    /**
     * Call when click on view
     *
     * @param view
     */
    public void onClick(View view) {
        if(view == transactionButton) {
            // display transaction activity
            startActivity(new Intent(MobileBankActivity.this, TransactionActivity.class));
            MobileBankActivity.this.finish();
        } else if(view == summaryButton) {
            // display transaction list activity
            startActivity(new Intent(MobileBankActivity.this, TransactionListActivity.class));
            MobileBankActivity.this.finish();
        } else if(view == settingsButton) {
            // display transaction activity
            startActivity(new Intent(MobileBankActivity.this, SettingsActivity.class));
            MobileBankActivity.this.finish();
        } else if(view == logout) {
            displayInformationMessageDialog("Are you sure, you want to logout? ");
        }
    }

    /**
     * Display message dialog when user going to logout
     * @param message
     */
    public void displayInformationMessageDialog(String message) {
        final Dialog dialog = new Dialog(MobileBankActivity.this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.information_message_dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_text);
        messageTextView.setText(message);

        // set custom font
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        messageHeaderTextView.setTypeface(face);
        messageHeaderTextView.setTypeface(null, Typeface.BOLD);
        messageTextView.setTypeface(face);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_ok_button);
        okButton.setTypeface(face);
        okButton.setTypeface(null, Typeface.BOLD);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // todo set login state to false
                application.resetFields();

                // back to login activity
                startActivity(new Intent(MobileBankActivity.this, LoginActivity.class));
                MobileBankActivity.this.finish();
                dialog.cancel();
            }
        });

        // cancel button
        Button cancelButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_cancel_button);
        cancelButton.setTypeface(face);
        cancelButton.setTypeface(null, Typeface.BOLD);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        displayInformationMessageDialog("Are you sure, you want to logout? ");
    }
}