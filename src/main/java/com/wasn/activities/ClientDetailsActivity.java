package com.wasn.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Attribute;
import com.wasn.pojos.Client;

import java.util.ArrayList;

/**
 * Activity class to display Client details
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class ClientDetailsActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // use to populate list
    ListView clientDetailsListView;
    ArrayList<Attribute> attributesList;
    AttributeListAdapter adapter;

    // form components
    Button backButton;
    Button helpButton;
    Button doneButton;
    Button cancelButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_details_list_layout);

        init();
    }

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) ClientDetailsActivity.this.getApplication();

        backButton = (Button) findViewById(R.id.client_details_list_layout_back_button);
        helpButton = (Button) findViewById(R.id.client_details_list_layout_help_button);
        doneButton = (Button) findViewById(R.id.client_details_list_layout_done_button);
        cancelButton = (Button) findViewById(R.id.client_details_list_layout_cancel_button);

        backButton.setOnClickListener(ClientDetailsActivity.this);
        helpButton.setOnClickListener(ClientDetailsActivity.this);
        doneButton.setOnClickListener(ClientDetailsActivity.this);
        cancelButton.setOnClickListener(ClientDetailsActivity.this);

        Client client = application.getClient();

        // populate list only if have client
        if(client!=null) {
            // fill attribute list with client details
            attributesList = new ArrayList<Attribute>();
            attributesList.add(new Attribute("Id", client.getId()));
            attributesList.add(new Attribute("Name", client.getName()));
            attributesList.add(new Attribute("NIC No", client.getNic()));
            attributesList.add(new Attribute("Birth Date", client.getBirthDate()));
            attributesList.add(new Attribute("Account No", client.getAccountNo()));
            attributesList.add(new Attribute("Account Balance", client.getBalanceAmount()));
            attributesList.add(new Attribute("Last Transaction", client.getPreviousTransaction()));

            // populate list
            clientDetailsListView = (ListView) findViewById(R.id.client_details_list);
            adapter = new AttributeListAdapter(ClientDetailsActivity.this, attributesList);
            clientDetailsListView.setAdapter(adapter);
        } else {
            // To-Do display empty view
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == backButton) {

        } else if(view == helpButton) {

        } else if(view == doneButton) {
            // back to TransactionActivity
        } else if(view == cancelButton) {
            // back to ClientListActivity
        }
    }
}
