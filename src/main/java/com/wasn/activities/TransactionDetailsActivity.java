package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.wasn.pojos.Transaction;
import com.wasn.services.backgroundservices.TransactionPrintService;
import com.wasn.utils.PrintUtils;

import java.util.ArrayList;

/**
 * Activity class to display transaction details
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionDetailsActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    public static final int DIALOG_LOADING = 1;

    // previous activity name
    private static String previousActivity;

    // use to populate list
    ListView transactionDetailsListView;
    ArrayList<Attribute> attributesList;
    AttributeListAdapter adapter;

    // form components
    RelativeLayout back;
    RelativeLayout help;
    RelativeLayout print;
    TextView headerText;

    private ProgressDialog progressDialog;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details_list_layout);

        init();
    }

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) TransactionDetailsActivity.this.getApplication();

        // get extra values
        // previous activity name comes with extras
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            previousActivity = extra.getString("ACTIVITY_NAME");
        } else {
            previousActivity = MobileBankActivity.class.getName();
        }

        back = (RelativeLayout) findViewById(R.id.transaction_details_layout_back);
        help = (RelativeLayout) findViewById(R.id.transaction_details_layout_help);
        print = (RelativeLayout) findViewById(R.id.transaction_details_layout_print);

        // set custom font for header text
        headerText = (TextView) findViewById(R.id.transaction_details_list_layout_header_text);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);

        back.setOnClickListener(TransactionDetailsActivity.this);
        help.setOnClickListener(TransactionDetailsActivity.this);
        print.setOnClickListener(TransactionDetailsActivity.this);

        Transaction transaction = application.getTransaction();

        // populate list only have transaction
        if(transaction != null) {
            // fill attribute list from with transaction details
            attributesList = new ArrayList<Attribute>();
            attributesList.add(new Attribute("Branch Id", transaction.getBranchId()));
            attributesList.add(new Attribute("Client Name", transaction.getClinetName()));
            attributesList.add(new Attribute("Client NIC", transaction.getClinetNic()));
            attributesList.add(new Attribute("Account No", transaction.getClientAccountNo()));
            attributesList.add(new Attribute("Transaction Type", transaction.getTransactionType()));
            attributesList.add(new Attribute("Transaction Amount", transaction.getTransactionAmount()));
            attributesList.add(new Attribute("Account Balance", transaction.getCurrentBalance()));
            attributesList.add(new Attribute("Transaction Time", transaction.getTransactionType()));
            attributesList.add(new Attribute("Last Transaction", "3000.00"));
            attributesList.add(new Attribute("Receipt No", transaction.getReceiptId()));

            // populate list
            transactionDetailsListView = (ListView) findViewById(R.id.transaction_details_list);

            // add header and footer
            View headerView = View.inflate(this, R.layout.header, null);
            View footerView = View.inflate(this, R.layout.footer, null);

            transactionDetailsListView.addHeaderView(headerView);
            transactionDetailsListView.addFooterView(footerView);

            adapter = new AttributeListAdapter(TransactionDetailsActivity.this, attributesList);
            transactionDetailsListView.setAdapter(adapter);
        } else {
            // To-Do display empty view
        }
    }

    /**
     * Display message dialog when user going to logout
     * @param message
     */
    public void displayInformationMessageDialog(String message) {
        final Dialog dialog = new Dialog(TransactionDetailsActivity.this);

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
                // printing event need to handle according to previous activity
                if(previousActivity.equals(MobileBankActivity.class.getName())) {
                    dialog.cancel();
                    // print and save transaction in database
                    // print two receipts
                    try {
                        if(PrintUtils.isEnableBluetooth()) {
                            showDialog(DIALOG_LOADING);
                            new TransactionPrintService(TransactionDetailsActivity.this).execute("PRINT");
                        }
                    } catch (BluetoothNotEnableException e) {
                        Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not enabled",Toast.LENGTH_LONG).show();
                    } catch (BluetoothNotAvailableException e) {
                        Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not available",Toast.LENGTH_LONG).show();
                    }
                } else {
                    dialog.cancel();
                    // print only one receipt
                    // reprint
                    try {
                        if(PrintUtils.isEnableBluetooth()) {
                            showDialog(DIALOG_LOADING);
                            new TransactionPrintService(TransactionDetailsActivity.this).execute("RE_PRINT");
                        }


                    } catch (BluetoothNotEnableException e) {
                        Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not enabled",Toast.LENGTH_LONG).show();
                    } catch (BluetoothNotAvailableException e) {
                        Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not available",Toast.LENGTH_LONG).show();
                    }
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
                final Dialog dialog = new Dialog(TransactionDetailsActivity.this, android.R.style.Theme_Translucent);
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

        if(previousActivity.equals(MobileBankActivity.class.getName())) {
            if(status.equals("1")) {
                // clear shared objects
                application.resetFields();
                Toast.makeText(TransactionDetailsActivity.this,"Transaction saved",Toast.LENGTH_LONG).show();

                // need to go back to transaction activity
                startActivity(new Intent(TransactionDetailsActivity.this, TransactionActivity.class));
                TransactionDetailsActivity.this.finish();
            } else if(status.equals("0")) {
                Toast.makeText(TransactionDetailsActivity.this,"Cannot print receipt",Toast.LENGTH_LONG).show();
            } else if(status.equals("-1") | (status.equals("-4"))) {
                Toast.makeText(TransactionDetailsActivity.this,"Cannot connect to printer",Toast.LENGTH_LONG).show();
            } else if(status.equals("-2")) {
                Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not enabled",Toast.LENGTH_LONG).show();
            } else if(status.equals("-3")) {
                Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not available",Toast.LENGTH_LONG).show();
            }
        } else {
            if(status.equals("1")) {
                Toast.makeText(TransactionDetailsActivity.this,"Receipt printed",Toast.LENGTH_LONG).show();

                // back to transaction list
                TransactionDetailsActivity.this.finish();
            } else if(status.equals("0")) {
                Toast.makeText(TransactionDetailsActivity.this,"Cannot print receipt",Toast.LENGTH_LONG).show();
            } else if(status.equals("-1") | (status.equals("-4"))) {
                Toast.makeText(TransactionDetailsActivity.this,"Cannot connect to printer",Toast.LENGTH_LONG).show();
            } else if(status.equals("-2")) {
                Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not enabled",Toast.LENGTH_LONG).show();
            } else if(status.equals("-3")) {
                Toast.makeText(TransactionDetailsActivity.this,"Bluetooth not available",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == back) {
            if(previousActivity.equals(MobileBankActivity.class.getName())) {
                // comes from mobile bank activity
                startActivity(new Intent(TransactionDetailsActivity.this, TransactionActivity.class));
            }

            TransactionDetailsActivity.this.finish();
        } else if(view == help) {

        } else if(view == print) {
            displayInformationMessageDialog("Do you wnt to print the receipt? make sure bluetooth is ON");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        if(previousActivity.equals(MobileBankActivity.class.getName())) {
            // comes from mobile bank activity
            startActivity(new Intent(TransactionDetailsActivity.this, TransactionActivity.class));
        }

        TransactionDetailsActivity.this.finish();
    }

}
