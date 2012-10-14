package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Client;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity class to display search result list
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class ClientListActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // use to populate and filter list
    ListView searchResultListView;
    ArrayList<Client> clientList;
    ArrayList<Client> filteredClientList = new ArrayList<Client>();
    ClientListAdapter adapter;

    // activity components
    EditText filterText;
    Button backButton;
    Button helpButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list_layout);

        init();
    }

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) ClientListActivity.this.getApplication();

        filterText = (EditText) findViewById(R.id.client_layout_filter_text);
        backButton = (Button) findViewById(R.id.client_layout_back_button);
        helpButton = (Button) findViewById(R.id.client_layout_help_button);

        backButton.setOnClickListener(ClientListActivity.this);
        helpButton.setOnClickListener(ClientListActivity.this);

        // temporarily fill elements to list
        clientList = new ArrayList<Client>();
        filteredClientList = new ArrayList<Client>();
        populateClientList();

        // populate list view
        searchResultListView = (ListView) findViewById(R.id.client_list);
        adapter = new ClientListAdapter(ClientListActivity.this, clientList);
        searchResultListView.setAdapter(adapter);

        // use to prevent initial focus on filter text
        searchResultListView.setFocusableInTouchMode(true);
        searchResultListView.requestFocus();

        // set text change listener
        filterText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filter text with regex
                String text = filterText.getText().toString().trim();
                String regex = text;
                Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE| Pattern.MULTILINE);

                int textLength = filterText.getText().length();
                filteredClientList.clear();

                // match pattern
                for (int j = 0; i < clientList.size(); i++) {
                    Client client = clientList.get(i);

                    if (textLength <= client.getName().length()) {
                        Matcher matcher = pattern.matcher(client.getName());

                        if(matcher.find()){
                            filteredClientList.add(client);
                        }
                    }
                }

                // set adapter
                adapter = new ClientListAdapter(ClientListActivity.this, filteredClientList);
                searchResultListView.setAdapter(adapter);
            }

            public void afterTextChanged(Editable editable) {

            }
        });

        // set click listener
        searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get corresponding client and share in application
                Client client = (Client)adapter.getItem(i);
                application.setClient(client);

                // back to transaction activity
                startActivity(new Intent(ClientListActivity.this, TransactionActivity.class));
                ClientListActivity.this.finish();
            }
        });

        //set long press listener
        searchResultListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get corresponding client and share in application
                Client client= (Client)adapter.getItem(i);
                application.setClient(client);
                startActivity(new Intent(ClientListActivity.this, ClientDetailsActivity.class));

                return true;
            }
        });
    }

    /**
     * Temporarily fill items to client list
     */
    public void populateClientList() {
        for(int i=0; i<15; i++) {
            Client client = new Client(""+i, "Eranga bnadara heratha" +i, "Test NIC", "Test bday", "47899" + i, "3000","test");
            clientList.add(client);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        // reset list content
        adapter = new ClientListAdapter(ClientListActivity.this, clientList);
        searchResultListView.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == backButton) {

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ClientListActivity.this, TransactionActivity.class));
        ClientListActivity.this.finish();
    }
}
