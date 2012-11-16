package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.*;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;

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

        // populate list view
        transactionListView = (ListView) findViewById(R.id.transaction_list);
        emptyView = (ViewStub) findViewById(R.id.transaction_list_layout_empty_view);

        allTransactionList = new ArrayList<Transaction>();
        populateList();
        // todo get al transactions from database

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
     * Display unsynced transaction list
     */
    public void displayUnsyncedTransactionList() {
        // find unsynced transactions and display in list
        unSyncedTransactionList = new ArrayList<Transaction>();

        if(unSyncedTransactionList.size()>0) {
            // have unsynced transaction
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

    public void populateList() {
        for(int i=0; i<15; i++) {
            Transaction transaction = new Transaction("1", "Test name " +i, "NIC", "88799" +i, "345", "45", "400", "TIME", "45", "34", "DEPOSIT", "CHECK_NO", "des");
            allTransactionList.add(transaction);
        }
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
            displayUnsyncedTransactionList();
            changeDoneButtonText("Sync");
        } else if(view == allTransactionHeader) {
            // load all transaction list
            selectAllTransactionHeader();
            displayAllTransactionList();
            changeDoneButtonText("Summary");
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
