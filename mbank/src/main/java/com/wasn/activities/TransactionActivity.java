package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wasn.application.MobileBankApplication;
import com.wasn.exceptions.EmptyFieldsException;
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
    EditText noteEditText;

    RelativeLayout back;
    RelativeLayout search;
    RelativeLayout done;

    TextView headerText;

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
        noteEditText = (EditText) findViewById(R.id.transaction_layout_note_text);

        back = (RelativeLayout) findViewById(R.id.transaction_layout_back);
        search = (RelativeLayout) findViewById(R.id.transaction_layout_search);
        done = (RelativeLayout) findViewById(R.id.transaction_layout_help);
        //done = (RelativeLayout) findViewById(R.id.transaction_layout_done);

        // set done keyboard option with note text
        noteEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == keyEvent.ACTION_DOWN && i==keyEvent.KEYCODE_ENTER) {
                    // transaction done event
                    initTransaction();
                }

                return false;
            }
        });

        // set custom font to header text
        headerText = (TextView) findViewById(R.id.transaction_layout_header_text);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);

        back.setOnClickListener(TransactionActivity.this);
        search.setOnClickListener(TransactionActivity.this);
        done.setOnClickListener(TransactionActivity.this);
        //done.setOnClickListener(TransactionActivity.this);

        if(application.getTransaction() !=null) {
            // have transaction
            // set values for form fields
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

            // get matching client and share in application
            Client client = application.getMobileBankData().getClient(accountNo);
            application.setClient(client);

            // get receipt no
            // database stored previous receipt no
            // receipt no equals to transaction id
            int transactionId = Integer.parseInt(application.getMobileBankData().getReceiptNo()) + 1;

            // get branch id
            // database stored branch id as well
            String branchId = application.getMobileBankData().getBranchId();

            // create transaction and share in application
            Transaction transaction = TransactionUtils.createTransaction(branchId, transactionId, amount, client);
            application.setTransaction(transaction);

            startActivity(new Intent(TransactionActivity.this, TransactionDetailsActivity.class));
            TransactionActivity.this.finish();
        } catch (NumberFormatException e) {
            displayMessageDialog("Error", "Invalid amount, make sure amount is correct");
        } catch (EmptyFieldsException e) {
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

        // set custom font
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        messageHeaderTextView.setTypeface(face);
        messageHeaderTextView.setTypeface(null, Typeface.BOLD);
        messageTextView.setTypeface(face);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.message_dialog_layout_yes_button);
        okButton.setTypeface(face);
        okButton.setTypeface(null, Typeface.BOLD);
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
        if(view == back) {
            // back to main activity
            startActivity(new Intent(TransactionActivity.this, MobileBankActivity.class));
            TransactionActivity.this.finish();
            application.resetFields();
        }else if(view == search) {
            // display search list
            startActivity(new Intent(TransactionActivity.this, ClientListActivity.class));
            TransactionActivity.this.finish();
        } else if(view == done) {
            initTransaction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        // back to main activity
        startActivity(new Intent(TransactionActivity.this, MobileBankActivity.class));
        TransactionActivity.this.finish();
        application.resetFields();
    }
}
