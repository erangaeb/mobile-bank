package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Settings;
import com.wasn.utils.SettingsUtils;

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
     * Action of save button
     */
    public void save() {
        // get form fields
        String printerAddress = printerAddressEditText.getText().toString();
        String telephoneNo = telephoneNoEditText.getText().toString();
        String branchName = branchNameEditText.getText().toString();
        Settings settings = new Settings(printerAddress, telephoneNo, branchName);

        try {
            SettingsUtils.validatePrinterAddress(settings);

            // save settings attributes in database
            savePrinterAddress(printerAddress);
            saveTelephoneNo(telephoneNo);
            saveBranchName(branchName);

            // back to main activity
            startActivity(new Intent(SettingsActivity.this, MobileBankActivity.class));
            SettingsActivity.this.finish();
            displayToast("Settings saved successfully");
        } catch (IllegalArgumentException e) {
            // empty printer address
            displayMessageDialog("Error", "Empty printer address, make sure not empty printer address");
            e.printStackTrace();
        }
    }

    /**
     * Save printer address in database
     * @param printerAddress printer address
     */
    public void savePrinterAddress(String printerAddress) {
        if(!printerAddress.equals(application.getMobileBankData().getPrinterAddress())) {
            // save only different address
            application.getMobileBankData().setPrinterAddress(printerAddress);
        }
    }

    /**
     * Save telephone no in database
     * @param telephoneNo telephone no
     */
    public void saveTelephoneNo(String telephoneNo) {
        if(!telephoneNo.equals("")) {
            if(!telephoneNo.equals(application.getMobileBankData().getTelephoneNo())) {
                // save only non empty different value
                application.getMobileBankData().setTelephoneNo(telephoneNo);
            }
        }
    }

    /**
     * Save branch name in database
     * @param branchName branch name
     */
    public void saveBranchName(String branchName) {
        if(!branchName.equals("")) {
            if(!branchName.equals(application.getMobileBankData().getBranchName())) {
                // save only non empty different value
                application.getMobileBankData().setBranchName(branchName);
            }
        }
    }

    /**
     * Display message dialog
     *
     * @param messageHeader message header
     * @param message message to be display
     */
    public void displayMessageDialog(String messageHeader, String message) {
        final Dialog dialog = new Dialog(SettingsActivity.this);

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
     * Display toast message
     * @param message message tobe display
     */
    public void displayToast(String message) {
        Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_LONG).show();
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

        } else if(view == save) {
            save();
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
