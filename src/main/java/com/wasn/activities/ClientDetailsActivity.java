package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    RelativeLayout back;
    RelativeLayout done;
    TextView headerText;

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

        back = (RelativeLayout) findViewById(R.id.client_details_list_layout_back);
        done = (RelativeLayout) findViewById(R.id.client_details_list_layout_done);
        headerText = (TextView) findViewById(R.id.client_details_list_layout_header_text);

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);

        back.setOnClickListener(ClientDetailsActivity.this);
        done.setOnClickListener(ClientDetailsActivity.this);

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

            // add header and footer
            View headerView = View.inflate(this, R.layout.header, null);
            View footerView = View.inflate(this, R.layout.footer, null);

            clientDetailsListView.addHeaderView(headerView);
            clientDetailsListView.addFooterView(footerView);

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
        if(view == back) {
            // back to ClientListActivity
            ClientDetailsActivity.this.finish();
        } else if(view == done) {
            // back to TransactionActivity
            Intent intent = new Intent(ClientDetailsActivity.this, TransactionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ClientDetailsActivity.this.finish();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        // back to ClientListActivity
        ClientDetailsActivity.this.finish();
    }
}
