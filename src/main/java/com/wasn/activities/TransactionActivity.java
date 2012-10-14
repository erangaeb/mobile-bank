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
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.InvalidAccountException;
import com.wasn.exceptions.InvalidBalanceAmountException;
import com.wasn.pojos.Client;
import com.wasn.pojos.Transaction;
import com.wasn.utils.TransactionUtils;

/**
 * Activity class to do new transaction
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class TransactionActivity extends Activity implements View.OnClickListener {

    MobileBankApplication application;

    // form components
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

        init();
    }

    /**
     * Initialize form components and values
     */
    public void init() {
        application = (MobileBankApplication) TransactionActivity.this.getApplication();

        accountEditText = (EditText)findViewById(R.id.transaction_layout_account_text);
        amountEditText = (EditText)findViewById(R.id.transaction_layout_amount_text);
        doneButton = (Button)findViewById(R.id.transaction_layout_done_button);
        cancelButton = (Button)findViewById(R.id.transaction_layout_cancel_button);
        searchButton = (Button)findViewById(R.id.transaction_layout_search_button);

        doneButton.setOnClickListener(TransactionActivity.this);
        cancelButton.setOnClickListener(TransactionActivity.this);
        searchButton.setOnClickListener(TransactionActivity.this);

        // set values for form fields
        if(application.getTransaction() !=null) {
            // have transaction
            Transaction transaction = application.getTransaction();

            accountEditText.setText(transaction.getClientAccountNo());
            amountEditText.setText(transaction.getTransactionAmount());
        } else {
            if(application.getClient() != null) {
                // no transaction, but have client
                Client client = application.getClient();

                accountEditText.setText(client.getAccountNo());
            }
        }
    }

    /**
     * Initialize new transaction
     */
    public void initTransaction() {
        String accountNo = accountEditText.getText().toString();
        String amount = amountEditText.getText().toString();

        try {
            // validate form fields and get corresponding client to the account
            TransactionUtils.validateFields(accountNo, amount);
            Client client = TransactionUtils.getMatchingClient(accountNo);

            // create transaction and share in application
            Transaction transaction = TransactionUtils.createTransaction("001", accountNo, amount, client);
            application.setTransaction(transaction);

            startActivity(new Intent(TransactionActivity.this, TransactionDetailsActivity.class));
        } catch (NumberFormatException e) {
            displayMessageDialog("Error", "Invalid amount, make sure amount is correct");
        } catch (IllegalArgumentException e) {
            displayMessageDialog("Error", "Empty fields, make sure not empty account and amount");
        } catch (InvalidAccountException e) {
            displayMessageDialog("Error", "Invalid account, make sure account is correct");
        } catch (InvalidBalanceAmountException e) {
            displayMessageDialog("Error", "Invalid balance amount, please recheck corresponding client details");
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
            initTransaction();
        } else if(view == cancelButton) {

        } else if(view == searchButton) {
            // display search list
            startActivity(new Intent(TransactionActivity.this, ClientListActivity.class));
        }
    }
}
