package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wasn.application.MobileBankApplication;

/**
 * Activity class to display transaction list
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionListActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // activity components
    RelativeLayout back;
    RelativeLayout help;
    RelativeLayout unsyncedTransactionHeader;
    RelativeLayout allTransactionHeader;
    LinearLayout bottomPannel;
    RelativeLayout done;
    TextView headerText;
    TextView doneText;
    TextView unsyncedTransactionHeaderText;
    TextView allTransactionHeaderText;

    // default color of texts
    ColorStateList defaultColors;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_list_layout);

        init();
    }

    /**
     * Initialize layout components
     */
    public void init() {
        application = (MobileBankApplication) TransactionListActivity.this.getApplication();

        // initialize
        back = (RelativeLayout) findViewById(R.id.transaction_list_layout_back);
        help = (RelativeLayout) findViewById(R.id.transaction_list_layout_help);
        unsyncedTransactionHeader = (RelativeLayout) findViewById(R.id.transaction_list_layout_unsynced_transaction_header);
        allTransactionHeader = (RelativeLayout) findViewById(R.id.transaction_list_layout_all_transaction_header);
        bottomPannel = (LinearLayout) findViewById(R.id.transaction_list_layout_bottom_pannel);
        done = (RelativeLayout) findViewById(R.id.transaction_list_layout_done);
        headerText = (TextView) findViewById(R.id.transaction_list_layout_header_text);
        doneText = (TextView) findViewById(R.id.transaction_list_layout_done_text);
        unsyncedTransactionHeaderText = (TextView) findViewById(R.id.transaction_list_layout_unsynced_transaction_header_text);
        allTransactionHeaderText = (TextView) findViewById(R.id.transaction_list_layout_all_transaction_header_text);

        defaultColors = allTransactionHeaderText.getTextColors();

        // set custom font to header text
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);
        unsyncedTransactionHeaderText.setTypeface(face);
        allTransactionHeaderText.setTypeface(face);

        // set click listeners
        back.setOnClickListener(TransactionListActivity.this);
        help.setOnClickListener(TransactionListActivity.this);
        unsyncedTransactionHeader.setOnClickListener(TransactionListActivity.this);
        allTransactionHeader.setOnClickListener(TransactionListActivity.this);

        // initially select unsynced transaction
        selectUnsyncedTransactionHeader();
    }

    /**
     * Set selected state of unsynced transaction header
     */
    public void selectUnsyncedTransactionHeader() {
        unsyncedTransactionHeader.setSelected(true);
        allTransactionHeader.setSelected(false);

        // change font color of text
        unsyncedTransactionHeaderText.setTextColor(Color.parseColor("#ffffff"));
        unsyncedTransactionHeaderText.setTypeface(null, Typeface.BOLD);
        allTransactionHeaderText.setTextColor(defaultColors);
        allTransactionHeaderText.setTypeface(null, Typeface.NORMAL);

        changeDoneButtonText("Sync");

        // todo enable bottom if unsynced transaction available
        disableBottomPannel();
    }

    /**
     * Set selected state of all transaction header
     */
    public void selectAllTransactionHeader() {
        unsyncedTransactionHeader.setSelected(false);
        allTransactionHeader.setSelected(true);

        // change font color of text
        allTransactionHeaderText.setTextColor(Color.parseColor("#ffffff"));
        allTransactionHeaderText.setTypeface(null, Typeface.BOLD);
        unsyncedTransactionHeaderText.setTextColor(defaultColors);
        unsyncedTransactionHeaderText.setTypeface(null, Typeface.NORMAL);

        changeDoneButtonText("Summary");

        // todo enable bottom pannel if transactions available
        enableBottomPannel();
    }

    /**
     * disable bottom pannel
     */
    public void disableBottomPannel() {
        bottomPannel.setVisibility(View.GONE);
    }

    /**
     * display bottom pannel
     */
    public void enableBottomPannel() {
        bottomPannel.setVisibility(View.VISIBLE);
    }

    /**
     * Change text of done button
     * @param text
     */
    public void changeDoneButtonText(String text) {
        doneText.setText(text);
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == back) {
            // back to main activity
            startActivity(new Intent(TransactionListActivity.this, MobileBankActivity.class));
            TransactionListActivity.this.finish();
            application.resetFields();
        } else if(view == help) {

        } else if(view == unsyncedTransactionHeader) {
            // load unsynced list
            selectUnsyncedTransactionHeader();
        } else if(view == allTransactionHeader) {
            // load all transaction list
            selectAllTransactionHeader();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        // back to main activity
        startActivity(new Intent(TransactionListActivity.this, MobileBankActivity.class));
        TransactionListActivity.this.finish();
        application.resetFields();
    }
}
