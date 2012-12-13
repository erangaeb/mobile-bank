package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.BluetoothNotAvailableException;
import com.wasn.exceptions.BluetoothNotEnableException;
import com.wasn.pojos.Attribute;
import com.wasn.pojos.Summary;
import com.wasn.services.backgroundservices.SummaryPrintService;
import com.wasn.utils.PrintUtils;
import com.wasn.utils.TransactionUtils;

import java.util.ArrayList;

/**
 * Activity class to display summary
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class SummaryDetailsActivity extends Activity implements View.OnClickListener{

    MobileBankApplication application;

    public static final int DIALOG_LOADING = 1;

    // use to populate list
    ListView summaryDetailsListView;
    ArrayList<Attribute> attributesList;
    AttributeListAdapter adapter;

    // form components
    LinearLayout bottomPannel;
    RelativeLayout back;
    RelativeLayout help;
    RelativeLayout print;
    TextView headerText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_details_list_layout);

        init();
    }

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) SummaryDetailsActivity.this.getApplication();

        bottomPannel = (LinearLayout) findViewById(R.id.summary_details_layout_bottom_pannel);
        back = (RelativeLayout) findViewById(R.id.summary_details_layout_back);
        help = (RelativeLayout) findViewById(R.id.summary_details_layout_help);
        print = (RelativeLayout) findViewById(R.id.summary_details_layout_print);

        // set custom font for header text
        headerText = (TextView) findViewById(R.id.summary_details_list_layout_header_text);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);

        back.setOnClickListener(SummaryDetailsActivity.this);
        help.setOnClickListener(SummaryDetailsActivity.this);
        print.setOnClickListener(SummaryDetailsActivity.this);

        // populate list
        Summary summary = TransactionUtils.getSummary(application.getTransactionList());
        attributesList = new ArrayList<Attribute>();
        attributesList.add(new Attribute("Time", summary.getTime()));
        attributesList.add(new Attribute("Branch ID", summary.getBranchId()));
        attributesList.add(new Attribute("Transaction Count", summary.getTransactionCount()));
        attributesList.add(new Attribute("Total Amount", summary.getTotalTransactionAmount()));
        attributesList.add(new Attribute("Last Receipt ID", summary.getLastReceiptId()));
        summaryDetailsListView = (ListView) findViewById(R.id.summary_details_list);

        // need to disable print if un synced transaction available
        if(TransactionUtils.getUnSyncedTransactionList(application.getTransactionList()).size()>0) {
            disableBottomPannel();
        } else {
            enableBottomPannel();
        }

        // add header and footer
        View headerView = View.inflate(this, R.layout.header, null);
        View footerView = View.inflate(this, R.layout.footer, null);

        summaryDetailsListView.addHeaderView(headerView);
        summaryDetailsListView.addFooterView(footerView);

        adapter = new AttributeListAdapter(SummaryDetailsActivity.this, attributesList);
        summaryDetailsListView.setAdapter(adapter);
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
     * Display message dialog when user going to logout
     * @param message
     */
    public void displayInformationMessageDialog(String message) {
        final Dialog dialog = new Dialog(SummaryDetailsActivity.this);

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
                dialog.cancel();

                // print summary
                try {
                    if(PrintUtils.isEnableBluetooth()) {
                        showDialog(DIALOG_LOADING);
                        new SummaryPrintService(SummaryDetailsActivity.this).execute("SUMMARY");
                    }
                } catch (BluetoothNotEnableException e) {
                    Toast.makeText(SummaryDetailsActivity.this,"Bluetooth not enabled",Toast.LENGTH_LONG).show();
                } catch (BluetoothNotAvailableException e) {
                    Toast.makeText(SummaryDetailsActivity.this,"Bluetooth not available",Toast.LENGTH_LONG).show();
                }
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_LOADING:
                // set layout of progress dialog
                final Dialog dialog = new Dialog(SummaryDetailsActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.custom_progress_dialog_layout);
                dialog.setCancelable(true);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub

                    }
                });
                return dialog;

            default:
                return null;
        }
    };

    /**
     * Close progress dialog
     */
    public void closeProgressDialog() {
        dismissDialog(DIALOG_LOADING);
    }

    /**
     * Execute after printing task
     * @param status print status
     */
    public void onPostPrint(String status) {
        // close progress dialog
        closeProgressDialog();

        if(status.equals("1")) {
            Toast.makeText(SummaryDetailsActivity.this,"Receipt printed",Toast.LENGTH_LONG).show();

            // back to transaction list
            SummaryDetailsActivity.this.finish();
        } else if(status.equals("0")) {
            Toast.makeText(SummaryDetailsActivity.this,"Cannot print receipt",Toast.LENGTH_LONG).show();
        } else if(status.equals("-1") | (status.equals("-4"))) {
            Toast.makeText(SummaryDetailsActivity.this,"Cannot connect to printer",Toast.LENGTH_LONG).show();
        } else if(status.equals("-2")) {
            Toast.makeText(SummaryDetailsActivity.this,"Bluetooth not enabled",Toast.LENGTH_LONG).show();
        } else if(status.equals("-3")) {
            Toast.makeText(SummaryDetailsActivity.this,"Bluetooth not available",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == back) {
            SummaryDetailsActivity.this.finish();
        } else if(view == help) {

        } else if(view == print) {
            displayInformationMessageDialog("Do you want to print the summary? After printing summary all the transaction will be deleted. make sure bluetooth is ON");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        SummaryDetailsActivity.this.finish();
    }
}
