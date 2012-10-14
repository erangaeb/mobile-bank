package com.wasn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
public class SearchResultListActivity extends Activity {

    MobileBankApplication application;

    ListView searchResultListView;
    ArrayList<Client> clientList;
    ArrayList<Client> filteredClientList = new ArrayList<Client>();
    SearchResultListAdapter adapter;

    EditText filterText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_list_layout);

        application = (MobileBankApplication) SearchResultListActivity.this.getApplication();

        filterText = (EditText) findViewById(R.id.search_result_list_layout_filter_text);

        clientList = new ArrayList<Client>();
        for(int i=0; i<15; i++) {
            Client client = new Client(""+i, "Eranga bnadara heratha" +i, "Test NIC", "3000", "Test bday", "test", "test");
            clientList.add(client);
        }

        // populate list
        searchResultListView = (ListView) findViewById(R.id.search_result_list);
        adapter = new SearchResultListAdapter(SearchResultListActivity.this, clientList);
        searchResultListView.setAdapter(adapter);

        // to prevent initial focus on filter text
        searchResultListView.setFocusableInTouchMode(true);
        searchResultListView.requestFocus();

        // set text change listener
        filterText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                //To change body of implemented methods use File | Settings | File Templates.
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
                adapter = new SearchResultListAdapter(SearchResultListActivity.this, filteredClientList);
                searchResultListView.setAdapter(adapter);
            }

            public void afterTextChanged(Editable editable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        // set click listener
        searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get corresponding client and share in application
                Client client = (Client)adapter.getItem(i);
                application.setClient(client);

                // back to transaction activity


            }
        });

        //set long press listener
        searchResultListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get corresponding client and display client details activity
                Client client= (Client)adapter.getItem(i);
                startActivity(new Intent(SearchResultListActivity.this, TransactionDetailsActivity.class));

                return true;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        // reset list content
        adapter = new SearchResultListAdapter(SearchResultListActivity.this, clientList);
        searchResultListView.setAdapter(adapter);
    }
}
