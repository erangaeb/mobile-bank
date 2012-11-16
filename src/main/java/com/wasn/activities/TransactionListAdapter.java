package com.wasn.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;

import java.util.ArrayList;

/**
 * List adapter class to display transaction list
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Transaction> transactionList;

    /**
     * Set context and attribute list
     *
     * @param context
     * @param transactionList
     */
    public TransactionListAdapter(Context context, ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    /**
     * Add filtered client to list
     */
    public void reloadAdapter(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    /**
     * Get size of attribute list
     */
    public int getCount() {
        return transactionList.size();
    }

    /**
     * Get specific item from attribute list
     */
    public Object getItem(int i) {
        return transactionList.get(i);
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
        // to findViewById() on each row
        final ViewHolder holder;

        Transaction transaction = (Transaction) getItem(i);

        if (view == null) {
            //inflate print_list_row layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.transaction_list_row_layout, viewGroup, false);

            //create view holder to store reference to child views
            holder=new ViewHolder();
            holder.transactionIcon = (ImageView) view.findViewById(R.id.transaction_list_row_layout_client_icon);
            holder.clientNameTextView = (TextView) view.findViewById(R.id.transaction_list_row_layout_client_name);
            //holder.clientBirthDateTextView = (TextView) view.findViewById(R.id.client_list_row_layout_birth_date);
            holder.accountNoTextView = (TextView) view.findViewById(R.id.transaction_list_row_layout_account_no);
            holder.transactionAmountTextView = (TextView) view.findViewById(R.id.transaction_list_row_layout_amount);

            view.setTag(holder);
        } else {
            //get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder text view to efficient use
        holder.transactionIcon.setImageResource(R.drawable.client_con);
        holder.clientNameTextView.setText(transaction.getClinetName());
        //holder.clientBirthDateTextView.setText(client.getBirthDate());
        holder.accountNoTextView.setText(transaction.getClientAccountNo());
        holder.transactionAmountTextView.setText(transaction.getTransactionAmount());

        // set backgrounds of list view rows
        if(i == 0) {
            // top item
            view.setBackgroundResource(R.drawable.invoice_list_top_item_background);
        } else if(i == transactionList.size()-1) {
            // middle items
            view.setBackgroundResource(R.drawable.invoice_list_bottom_item_background);
        } else {
            // bottom items
            view.setBackgroundResource(R.drawable.invoice_list_item_background);
        }

        return view;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {
        ImageView transactionIcon;
        TextView clientNameTextView;
        //TextView clientBirthDateTextView;
        TextView accountNoTextView;
        TextView transactionAmountTextView;
    }

}
