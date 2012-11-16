package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;

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
    ListView clientListView;
    ArrayList<Client> clientList;
    ArrayList<Client> filteredClientList = new ArrayList<Client>();
    ClientListAdapter adapter;

    // activity components
    EditText filterText;
    RelativeLayout back;
    RelativeLayout help;

    // to handle empty view
    ViewStub emptyView;

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

        filterText = (EditText) findViewById(R.id.client_list_layout_filter_text);
        back = (RelativeLayout) findViewById(R.id.client_list_layout_back);
        help = (RelativeLayout) findViewById(R.id.client_list_layout_help);

        back.setOnClickListener(ClientListActivity.this);
        help.setOnClickListener(ClientListActivity.this);

        // populate list view
        clientListView = (ListView) findViewById(R.id.client_list);
        emptyView = (ViewStub) findViewById(R.id.client_list_layout_empty_view);

        // add header and footer
        View headerView = View.inflate(this, R.layout.header, null);
        View footerView = View.inflate(this, R.layout.footer, null);
        clientListView.addHeaderView(headerView);
        clientListView.addFooterView(footerView);

        // fill lists
        clientList = application.getMobileBankData().getAllClients();
        populateClientList();
        filteredClientList = new ArrayList<Client>();
        adapter = new ClientListAdapter(ClientListActivity.this, clientList);

        // handle empty view display
        if(clientList.size() > 0) {
            clientListView.setAdapter(adapter);
        } else {
            displayEmptyView();
            disableFilterText();
        }

        // use to prevent initial focus on filter text
        clientListView.setFocusableInTouchMode(true);
        clientListView.requestFocus();

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

                // reload adapter
                adapter.reloadAdapter(filteredClientList);
            }

            public void afterTextChanged(Editable editable) {

            }
        });

        // set click listener
        clientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("click " + adapter.getCount() + "index " + i);
                // get corresponding client and share in application
                Client client = (Client)adapter.getItem(i-1);
                application.setClient(client);

                // back to transaction activity
                startActivity(new Intent(ClientListActivity.this, TransactionActivity.class));
                ClientListActivity.this.finish();
            }
        });

        //set long press listener
        clientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("long click " + adapter.getCount() + "index " + i);
                // get corresponding client and share in application
                Client client= (Client)adapter.getItem(i-1);
                application.setClient(client);

                // got to client details activity
                startActivity(new Intent(ClientListActivity.this, ClientDetailsActivity.class));
                ClientListActivity.this.finish();

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
     * Display empty view when no clients
     */
    public void displayEmptyView() {
        adapter = new ClientListAdapter(ClientListActivity.this, new ArrayList<Client>());
        clientListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        clientListView.setEmptyView(emptyView);
    }

    /**
     * Disable filter text
     */
    public void disableFilterText() {
        filterText.setEnabled(false);
        filterText.setClickable(false);
        filterText.setFocusable(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
        filterText.setText("");

        // reset list content
        adapter.reloadAdapter(clientList);
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == back) {
            startActivity(new Intent(ClientListActivity.this, TransactionActivity.class));
            ClientListActivity.this.finish();
        } else if(view == help) {

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
