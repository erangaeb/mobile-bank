package com.wasn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wasn.application.MobileBankApplication;
import com.wasn.pojos.Attribute;
import com.wasn.services.backgroundservices.SummaryPrintService;
import com.wasn.utils.TransactionUtils;

import java.util.ArrayList;

/**
 * Activity class to display summary
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class SummaryDetailsActivity extends Activity implements View.OnClickListener{

    MobileBankApplication application;

    // use to populate list
    ListView summaryDetailsListView;
    ArrayList<Attribute> attributesList;
    AttributeListAdapter adapter;

    // form components
    RelativeLayout back;
    RelativeLayout help;
    RelativeLayout print;
    TextView headerText;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_details_list_layout);

        init();
    }

    /**
     * Initialize activity components
     */
    public void init() {
        application = (MobileBankApplication) SummaryDetailsActivity.this.getApplication();

        back = (RelativeLayout) findViewById(R.id.summary_details_layout_back);
        help = (RelativeLayout) findViewById(R.id.summary_details_layout_help);
        print = (RelativeLayout) findViewById(R.id.summary_details_layout_print);

        // set custom font for header text
        headerText = (TextView) findViewById(R.id.summary_details_list_layout_header_text);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        headerText.setTypeface(face);
        headerText.setTypeface(null, Typeface.BOLD);

        back.setOnClickListener(SummaryDetailsActivity.this);
        help.setOnClickListener(SummaryDetailsActivity.this);
        print.setOnClickListener(SummaryDetailsActivity.this);

        // populate list
        attributesList = TransactionUtils.getSummary(application.getTransactionList());
        summaryDetailsListView = (ListView) findViewById(R.id.summary_details_list);

        // add header and footer
        View headerView = View.inflate(this, R.layout.header, null);
        View footerView = View.inflate(this, R.layout.footer, null);

        summaryDetailsListView.addHeaderView(headerView);
        summaryDetailsListView.addFooterView(footerView);

        adapter = new AttributeListAdapter(SummaryDetailsActivity.this, attributesList);
        summaryDetailsListView.setAdapter(adapter);
    }

    /**
     * Display message dialog when user going to logout
     * @param message
     */
    public void displayInformationMessageDialog(String message) {
        final Dialog dialog = new Dialog(SummaryDetailsActivity.this);

        //set layout for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.information_message_dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // set dialog texts
        TextView messageHeaderTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_header_text);
        TextView messageTextView = (TextView) dialog.findViewById(R.id.information_message_dialog_layout_message_text);
        messageTextView.setText(message);

        // set custom font
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        messageHeaderTextView.setTypeface(face);
        messageHeaderTextView.setTypeface(null, Typeface.BOLD);
        messageTextView.setTypeface(face);

        //set ok button
        Button okButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_ok_button);
        okButton.setTypeface(face);
        okButton.setTypeface(null, Typeface.BOLD);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
                new SummaryPrintService(SummaryDetailsActivity.this).execute("SUMMARY");
            }
        });

        // cancel button
        Button cancelButton = (Button) dialog.findViewById(R.id.information_message_dialog_layout_cancel_button);
        cancelButton.setTypeface(face);
        cancelButton.setTypeface(null, Typeface.BOLD);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /**
     * Execute after printing task
     */
    public void onPostPrint() {
        SummaryDetailsActivity.this.finish();
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View view) {
        if(view == back) {
            SummaryDetailsActivity.this.finish();
        } else if(view == help) {

        } else if(view == print) {
            displayInformationMessageDialog("Do you want to print the summary? make sure bluetooth is ON");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        SummaryDetailsActivity.this.finish();
    }
}
