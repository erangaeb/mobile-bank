package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.wasn.utils.TransactionUtils;

/**
 * Activity class to do new transaction
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionActivity extends Activity implements View.OnClickListener {

    EditText accountEditText;
    EditText amountEditText;
    Button doneButton;
    Button cancelButton;
    Button searchButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_layout);

        accountEditText = (EditText)findViewById(R.id.transaction_layout_account_text);
        amountEditText = (EditText)findViewById(R.id.transaction_layout_amount_text);
        doneButton = (Button)findViewById(R.id.transaction_layout_done_button);
        cancelButton = (Button)findViewById(R.id.transaction_layout_cancel_button);
        searchButton = (Button)findViewById(R.id.transaction_layout_search_button);

        doneButton.setOnClickListener(TransactionActivity.this);
        cancelButton.setOnClickListener(TransactionActivity.this);
        searchButton.setOnClickListener(TransactionActivity.this);

    }

    /**
     * Make new transaction
     */
    public void makeTransaction() {
        String accountNo = accountEditText.getText().toString();
        String amount = amountEditText.getText().toString();

        // validate fields,
        try {
            TransactionUtils.validateFields(accountNo, amount);
        } catch (NumberFormatException e) {
            displayMessageDialog("Error", "Invalid amount, make sure amount tis correct");
        } catch (IllegalArgumentException e) {
            displayMessageDialog("Error", "Empty fields, make sure not empty account and amount");
        }
    }


    /**
     * Display message dialog
     *
     * @param messageHeader message header
     * @param message message to be display
     */
    public void displayMessageDialog(String messageHeader, String message) {
        final Dialog dialog = new Dialog(TransactionActivity.this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.message_dialog_layout_message_text);
        messageHeaderTextView.setText(messageHeader);
        messageTextView.setText(message);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.message_dialog_layout_yes_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * Call when click on view
     * @param view
     */
    public void onClick(View view) {
        if(view == doneButton) {
            // display transaction  details activity
            startActivity(new Intent(TransactionActivity.this, TransactionDetailsActivity.class));
        } else if(view == cancelButton) {

        } else if(view == searchButton) {
            // display search list
            startActivity(new Intent(TransactionActivity.this, SearchResultListActivity.class));
        }
    }
}
