package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wasn.application.MobileBankApplication;

/**
 * Activity class correspond to settings
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class SettingsActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // activity components
    RelativeLayout back;
    RelativeLayout help;
    RelativeLayout save;
    Button testPrint;
    TextView headerText;
    EditText printerAddressEditText;
    EditText telephoneNoEditText;
    EditText branchNameEditText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        init();
    }

    /**
     * Initialize activity components and values
     */
    public void init() {
        application = (MobileBankApplication) getApplication();

        back = (RelativeLayout) findViewById(R.id.settings_layout_back);
        help = (RelativeLayout) findViewById(R.id.settings_layout_help);
        save = (RelativeLayout) findViewById(R.id.settings_layout_save);
        testPrint = (Button) findViewById(R.id.settings_layout_test_print_button);

        // set custom font to header text
        headerText = (TextView) findViewById(R.id.settings_layout_header_text);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);

        // set text to printer address
        // printer address stored in database
        printerAddressEditText = (EditText) findViewById(R.id.settings_layout_printer_address_text);
        printerAddressEditText.setText(application.getMobileBankData().getPrinterAddress());

        // set text to telephone no
        // stored in database
        telephoneNoEditText = (EditText) findViewById(R.id.settings_layout_telephone_no_text);
        telephoneNoEditText.setText(application.getMobileBankData().getTelephoneNo());

        // set text to branch name
        // stored in database
        branchNameEditText = (EditText) findViewById(R.id.settings_layout_branch_name_text);
        branchNameEditText.setText(application.getMobileBankData().getBranchName());

        // enable test print button if printer address available


        back.setOnClickListener(SettingsActivity.this);
        help.setOnClickListener(SettingsActivity.this);
        save.setOnClickListener(SettingsActivity.this);
        testPrint.setOnClickListener(SettingsActivity.this);
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == back) {
            // back to main activity
            startActivity(new Intent(SettingsActivity.this, MobileBankActivity.class));
            SettingsActivity.this.finish();
        } else if(view == help) {

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        // back to main activity
        startActivity(new Intent(SettingsActivity.this, MobileBankActivity.class));
        SettingsActivity.this.finish();
    }
}
