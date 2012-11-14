package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Attribute;
import com.wasn.pojos.Transaction;

import java.util.ArrayList;

/**
 * Activity class to display transaction details
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionDetailsActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // use to populate list
    ListView transactionDetailsListView;
    ArrayList<Attribute> attributesList;
    AttributeListAdapter adapter;

    // form components
    RelativeLayout back;
    RelativeLayout help;
    RelativeLayout print;

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

        back = (RelativeLayout) findViewById(R.id.transaction_details_layout_back);
        help = (RelativeLayout) findViewById(R.id.transaction_details_layout_help);
        print = (RelativeLayout) findViewById(R.id.transaction_details_layout_print);

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
            adapter = new AttributeListAdapter(TransactionDetailsActivity.this, attributesList);
            transactionDetailsListView.setAdapter(adapter);
        } else {
            // To-Do display empty view
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == back) {

        } else if(view == help) {

        } else if(view == print) {
            // print and save transaction
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        // back to TransactionActivity
        startActivity(new Intent(TransactionDetailsActivity.this, TransactionActivity.class));
        TransactionDetailsActivity.this.finish();
    }

}
