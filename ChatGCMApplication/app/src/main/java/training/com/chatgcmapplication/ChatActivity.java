package training.com.chatgcmapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import training.com.services.MessageService;

public class ChatActivity extends AppCompatActivity {
    private Button btn_send;
    private EditText txt_chat;
    private TableLayout tab_content;
    private Bundle bundle;

    private WakefulBroadcastReceiver wakefulBroadcastReceiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_send = (Button) findViewById(R.id.btn_send);
        txt_chat = (EditText) findViewById(R.id.txt_chat);
        tab_content = (TableLayout) findViewById(R.id.tab_content);
        bundle = getIntent().getBundleExtra("INFO");

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter());
//
//        if(bundle.getString("message") != null) {
//            TableRow tableRow  = new TableRow(getApplicationContext());
//            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
//            TextView textView = new TextView(getApplicationContext());
//            textView.setTextSize(20);
//            textView.setTextColor(Color.parseColor("#A901DB"));
//            textView.setText(Html.fromHtml("<b>" + bundle.getString("from") + " : </b>" + bundle.getString("message")));
//            tableRow.addView(textView);
//            tab_content.addView(tableRow);
//        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getBundleExtra("INFO");
                if (bundle.getString("message") != null) {
                    Log.i("Chat Activity", bundle.getString("message"));
                }

            }
        });
    }




    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String sender = intent.getStringExtra("from");
            Log.i("In Chat Activity", message);
            TableRow tableRow = new TableRow(getApplicationContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml("<b>" + sender + " : </b>" + message));
            tableRow.addView(textview);
            tab_content.addView(tableRow);
        }
    };




}
