package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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
            // logout
            startActivity(new Intent(MobileBankActivity.this, LoginActivity.class));
            MobileBankActivity.this.finish();
            application.resetFields();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        // back to main activity
        startActivity(new Intent(MobileBankActivity.this, LoginActivity.class));
        MobileBankActivity.this.finish();
    }
}