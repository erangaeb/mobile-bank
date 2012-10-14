package com.wasn.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.wasn.pojos.Attribute;
import com.wasn.pojos.Transaction;

import java.util.ArrayList;

/**
 * Activity class to display transaction details
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionDetailsActivity extends Activity {

    ListView transactionDetailsListView;
    ArrayList<Attribute> attributesList;
    AttributeListAdapter adapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details_list_layout);

        // get current transaction from application
        Transaction transaction = new Transaction("Test nanme", "test Addre", "test", "test", "test", "test", "test", "test", "test" , "test", "test", "test", "test");

        // fill attribute list from with transaction details
        attributesList = new ArrayList<Attribute>();
        attributesList.add(new Attribute("Id", "008"));
        attributesList.add(new Attribute("Name", "Eranga Bandara"));
        attributesList.add(new Attribute("Account", "09799834"));
        attributesList.add(new Attribute("Address", "NO pullair kovil rias"));
        attributesList.add(new Attribute("NIC No", "873643030V"));
        attributesList.add(new Attribute("B'day", "1987/12/29"));
        attributesList.add(new Attribute("Previous balance", "12500.00"));
        attributesList.add(new Attribute("Last Transaction", "3000.00"));
        /*attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
        attributesList.add(new Attribute("test", "test"));
*/
        // populate list
        transactionDetailsListView = (ListView) findViewById(R.id.transaction_details_list);
        adapter = new AttributeListAdapter(TransactionDetailsActivity.this, attributesList);
        transactionDetailsListView.setAdapter(adapter);
    }

}
