package com.wasn.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.wasn.pojos.Attribute;
import com.wasn.pojos.Client;

import java.util.ArrayList;

/**
 * List adapter class to display search result
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class SearchResultListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Client> clientList;

    /**
     * Set context and attribute list
     *
     * @param context
     * @param clientList
     */
    public SearchResultListAdapter(Context context, ArrayList<Client> clientList) {
        this.context = context;
        this.clientList = clientList;
    }

    /**
     * Get size of attribute list
     */
    public int getCount() {
        return clientList.size();
    }

    /**
     * Get specific item from attribute list
     */
    public Object getItem(int i) {
        return clientList.get(i);
    }

    /**
     * Get attribute list item id
     */
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     */
    public View getView(int i, View view, ViewGroup viewGroup) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;

        Client client = (Client) getItem(i);

        if (view == null) {
            //inflate print_list_row layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.search_result_list_row_layout, viewGroup, false);

            //create view holder to store reference to child views
            holder=new ViewHolder();
            holder.clientIcon = (ImageView) view.findViewById(R.id.client_icon);
            holder.clientNameTextView = (TextView) view.findViewById(R.id.client_name);
            holder.clientBirthDateTextView = (TextView) view.findViewById(R.id.search_result_list_row_birth_date);
            holder.clientAccountNoTextView = (TextView) view.findViewById(R.id.client_account_no);
            holder.clientNICTextView = (TextView) view.findViewById(R.id.client_nic);

            view.setTag(holder);
        } else {
            //get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder text view to efficient use
        holder.clientIcon.setImageResource(R.drawable.client_con);
        holder.clientNameTextView.setText(client.getName());
        //holder.clientBirthDateTextView.setText(client.getBirthDate());
        holder.clientAccountNoTextView.setText(client.getAccountNo());
        holder.clientNICTextView.setText(client.getNic());

        // set click listener for call image view
        holder.clientIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(context,
                        "clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {
        ImageView clientIcon;
        TextView clientNameTextView;
        TextView clientBirthDateTextView;
        TextView clientAccountNoTextView;
        TextView clientNICTextView;
    }

}
