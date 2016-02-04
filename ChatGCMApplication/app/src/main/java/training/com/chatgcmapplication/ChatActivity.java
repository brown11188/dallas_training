package training.com.chatgcmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import training.com.common.AppConfig;
import training.com.common.TimeUtil;
import training.com.database.DatabaseHelper;
import training.com.model.Message;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_send;
    private EditText txt_chat;
    private TableLayout tab_content;
    private String registId;
    private Bundle bundle;
    private String chatTitle;
    private MessageSender mgsSender;
    private ScrollView scrollView;
    private int userId;
    private DatabaseHelper databaseHelper;
    private TimeUtil timeUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btn_send = (Button) findViewById(R.id.btn_send);
        txt_chat = (EditText) findViewById(R.id.txt_chat);
        tab_content = (TableLayout) findViewById(R.id.tab_content);
        scrollView = (ScrollView) findViewById(R.id.scroll_chat);
        timeUtil = new TimeUtil();
        forceScrollViewToBottom();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        btn_send.setOnClickListener(this);
        bundle = getIntent().getExtras();
        chatTitle = bundle.getString("titleName");
        if (getIntent().getBundleExtra("INFO") != null) {
            chatTitle = getIntent().getBundleExtra("INFO").getString("name");
            this.setTitle(chatTitle);
        } else {
            this.setTitle(chatTitle);
        }
        registId = bundle.getString("regId");
        List<Message> messages = databaseHelper.getMessges(AppConfig.USER_ID,databaseHelper.getUser(chatTitle).getUserId() );
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        if (messages.size() > 0) {
            for (Message element : messages) {
                displayMessage(databaseHelper.getUserByUserId(element.getUserId()).getUserName(), element.getMessage(), element.getUserId());
            }
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String sender = intent.getStringExtra("name");
            displayMessage(sender, message, databaseHelper.getUser(sender).getUserId());
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
                databaseHelper = new DatabaseHelper(getApplicationContext());
                mgsSender = new MessageSender();
                MessageSenderContent mgsContent = createMegContent(registId, AppConfig.USER_NAME, message);
                mgsSender.sendPost(mgsContent);
                userId = databaseHelper.getUser(chatTitle).getUserId();
                databaseHelper.addMessage(message, timeUtil.getCurrentTime(), userId, AppConfig.USER_ID);
                txt_chat.setText("");
                displayMessage(AppConfig.USER_NAME, message, AppConfig.USER_ID);
                break;
        }
    }


    private void displayMessage(String username, String message, int user_id) {
        TableRow tableRow = new TableRow(getApplicationContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView textview = new TextView(getApplicationContext());
        textview.setTextSize(20);
        textview.setMaxLines(2);
        if(user_id == AppConfig.USER_ID) {
            textview.setTextColor(Color.parseColor("#0066ff"));
        } else {
            textview.setTextColor(Color.parseColor("#000000"));
        }
        textview.setText(Html.fromHtml("<b>" + username + " : </b>" + message));
        tableRow.addView(textview);
        tab_content.addView(tableRow);
    }

    private void forceScrollViewToBottom() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

}
