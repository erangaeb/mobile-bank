package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.*;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Transaction;
import com.wasn.services.backgroundservices.ClientDataDownloadService;
import com.wasn.services.backgroundservices.TransactionSyncService;
import com.wasn.utils.NetworkUtil;
import com.wasn.utils.TransactionUtils;

import java.util.ArrayList;

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

    // use to populate list
    ListView transactionListView;
    TransactionListAdapter adapter;
    ViewStub emptyView;

    ArrayList<Transaction> allTransactionList;
    ArrayList<Transaction> unSyncedTransactionList;

    // default color of texts
    ColorStateList defaultColors;

    // display when syncing
    public ProgressDialog progressDialog;

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
        done.setOnClickListener(TransactionListActivity.this);
        unsyncedTransactionHeader.setOnClickListener(TransactionListActivity.this);
        allTransactionHeader.setOnClickListener(TransactionListActivity.this);

        // populate list view
        transactionListView = (ListView) findViewById(R.id.transaction_list);
        emptyView = (ViewStub) findViewById(R.id.transaction_list_layout_empty_view);

        allTransactionList = application.getTransactionList();

        // add header and footer
        View headerView = View.inflate(this, R.layout.header, null);
        View footerView = View.inflate(this, R.layout.footer, null);
        transactionListView.addHeaderView(headerView);
        transactionListView.addFooterView(footerView);

        //set long press listener
        transactionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get corresponding transaction and share in application
                Transaction transaction = (Transaction)adapter.getItem(i-1);
                application.setTransaction(transaction);

                // start new activity
                Intent intent = new Intent(TransactionListActivity.this, TransactionDetailsActivity.class);
                intent.putExtra("ACTIVITY_NAME", TransactionListActivity.class.getName());
                startActivity(intent);

                return true;
            }
        });

        // initially select unsynced transaction
        selectUnsyncedTransactionHeader();
        displayUnsyncedTransactionList();
        changeDoneButtonText("Sync");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        if(unsyncedTransactionHeader.isSelected()) {
            // display un synced list
            displayUnsyncedTransactionList();
        } else {
            // all transactions
            displayAllTransactionList();
        }
    }

    /**
     * Display un synced transaction list
     */
    public void displayUnsyncedTransactionList() {
        // find un synced transactions and display in list
        unSyncedTransactionList = TransactionUtils.getUnSyncedTransactionList(allTransactionList);

        if(unSyncedTransactionList.size()>0) {
            // have un synced transaction
            adapter = new TransactionListAdapter(TransactionListActivity.this, unSyncedTransactionList);
            transactionListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            enableBottomPannel();
        } else {
            disableBottomPannel();
            displayEmptyView();
        }
    }

    /**
     * Display all transaction list
     */
    public void displayAllTransactionList() {
        allTransactionList = application.getTransactionList();

        if(allTransactionList.size()>0) {
            // have transaction
            adapter = new TransactionListAdapter(TransactionListActivity.this, allTransactionList);
            transactionListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            enableBottomPannel();
        } else {
            disableBottomPannel();
            displayEmptyView();
        }
    }

    /**
     * Display empty view when no clients
     */
    public void displayEmptyView() {
        adapter = new TransactionListAdapter(TransactionListActivity.this, new ArrayList<Transaction>());
        transactionListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        transactionListView.setEmptyView(emptyView);
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
     * Sync transactions to bank server
     */
    public void syncTransaction() {
        // sync, if only available network connection
        if(NetworkUtil.isAvailableNetwork(TransactionListActivity.this)) {
            // start background thread to sync
            progressDialog = ProgressDialog.show(TransactionListActivity.this, "", "Syncing transactions, please wait...");
            new TransactionSyncService(TransactionListActivity.this).execute("SYNC");
        } else {
            displayToast("No network connection");
        }
    }

    /**
     * Execute after sync transactions
     * @param status sync status
     */
    public void onPostSync(int status) {
        closeProgressDialog();

        // display toast according to sync status
        if (status > 0) {
            // sync success
            // no un synced transaction now
            allTransactionList = application.getMobileBankData().getAllTransactions(application.getMobileBankData().getBranchId());
            application.setTransactionList(allTransactionList);
            displayEmptyView();

            displayToast("Synced " + status + "transactions ");
        } else if(status == -1) {
            displayToast("Sync fail, error in synced record");
        } else if(status == -2) {
            displayToast("Sync fail, server response error");
        } else if(status == -3) {
            displayToast("Server response error");
        } else {
            displayToast("Sync fail");
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
     * Display message dialog when user going to logout
     * @param message
     */
    public void displayInformationMessageDialog(String message) {
        final Dialog dialog = new Dialog(TransactionListActivity.this);

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
                syncTransaction();
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
     * Display toast message
     * @param message message tobe display
     */
    public void displayToast(String message) {
        Toast.makeText(TransactionListActivity.this, message, Toast.LENGTH_LONG).show();
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
            // load un synced list
            selectUnsyncedTransactionHeader();
            displayUnsyncedTransactionList();
            changeDoneButtonText("Sync");
        } else if(view == allTransactionHeader) {
            // load all transaction list
            selectAllTransactionHeader();
            displayAllTransactionList();
            changeDoneButtonText("Summary");
        } else if(view == done) {
            if(doneText.getText().toString().equals("Sync")) {
                // sync transactions
                displayInformationMessageDialog("Are you sure you want to sync transactions? ");
            } else if(doneText.getText().toString().equals("Summary")) {
                // display summary activity
                startActivity(new Intent(TransactionListActivity.this, SummaryDetailsActivity.class));
            }
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
