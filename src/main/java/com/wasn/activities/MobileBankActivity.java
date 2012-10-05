package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main activity class of the application
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class MobileBankActivity extends Activity implements View.OnClickListener {

    Button transactionButton;
    Button summaryButton;
    Button settingsButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_bank_layout);

        transactionButton = (Button) findViewById(R.id.mobile_bank_layout_transaction_button);
        summaryButton = (Button) findViewById(R.id.mobile_bank_layout_summary_button);
        settingsButton = (Button) findViewById(R.id.mobile_bank_layout_settings_button);

        transactionButton.setOnClickListener(MobileBankActivity.this);
        summaryButton.setOnClickListener(MobileBankActivity.this);
        settingsButton.setOnClickListener(MobileBankActivity.this);
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
        } else if(view == summaryButton) {

        } else if(view == settingsButton) {

        }
    }
}