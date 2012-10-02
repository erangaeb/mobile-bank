package com.wasn.activities;

import android.app.Activity;
import android.os.Bundle;

/**
 * Main activity class of the application
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class MobileBankActivity extends Activity {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_bank_layout);
    }

}