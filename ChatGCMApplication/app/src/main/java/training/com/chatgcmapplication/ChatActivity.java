package training.com.chatgcmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import training.com.common.AppConfig;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_send;
    private EditText txt_chat;
    private TableLayout tab_content;
    private String userFullName;
    private String registId;
    private Bundle bundle;
    private SharedPreferences preferences;
    private MessageSender mgsSender;
    private String senderName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("CHAT", 0);


        btn_send = (Button) findViewById(R.id.btn_send);
        txt_chat = (EditText) findViewById(R.id.txt_chat);
        tab_content = (TableLayout) findViewById(R.id.tab_content);


        btn_send.setOnClickListener(this);
        bundle = getIntent().getExtras();
        if (getIntent().getBundleExtra("INFO") != null) {
            userFullName = getIntent().getBundleExtra("INFO").getString("name");
            Log.i("Sender name", userFullName);
            this.setTitle(senderName);

        } else {
            userFullName = bundle.getString("name");
            this.setTitle(userFullName);
        }
        registId = bundle.getString("regId");

        Set<String> set =  preferences.getStringSet("message_set", null);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        if (set.size() > 0) {
            for (String element : set) {
                TableRow tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textView = new TextView(getApplicationContext());
                textView.setTextSize(20);
                textView.setTextColor(Color.parseColor("#0B0719"));
//                Log.i("Sender name", senderName);
                textView.setText(Html.fromHtml("<b>" + userFullName + " : </b>" + element));
                tableRow.addView(textView);
                tab_content.addView(tableRow);
            }
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String sender = intent.getStringExtra("name");

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
        super.onDestroy();

    }

    private static MessageSenderContent createMegContent(String regId, String title, String message) {
        MessageSenderContent mgsContent = new MessageSenderContent();
        mgsContent.addRegId(regId);
        mgsContent.createData(title, message);
        return mgsContent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String message = txt_chat.getText().toString();
                mgsSender = new MessageSender();
                MessageSenderContent mgsContent = createMegContent(registId, userFullName, message);
                mgsSender.sendPost(mgsContent);
                break;
        }
    }
}
