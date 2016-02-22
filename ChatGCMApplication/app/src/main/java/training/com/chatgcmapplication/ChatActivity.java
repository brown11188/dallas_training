package training.com.chatgcmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import training.com.adapter.MessageAdapter;
import training.com.common.AppConfig;
import training.com.common.TimeUtil;
import training.com.database.DatabaseHelper;
import training.com.model.Message;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_send;
    private static EditText txt_chat;
    private String registId;
    private Bundle bundle;
    private String chatTitle;
    private MessageSender mgsSender;
    private int userId;
    private DatabaseHelper databaseHelper;
    private TimeUtil timeUtil;
    private MessageAdapter messageAdapter;
    private ListView lv_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btn_send = (Button) findViewById(R.id.btn_send);
        txt_chat = (EditText) findViewById(R.id.txt_chat);
        lv_message = (ListView) findViewById(R.id.listMessage);
        timeUtil = new TimeUtil();
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
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
        userId = databaseHelper.getUser(chatTitle).getUserId();
        List<Message> messages = databaseHelper.getMessges(AppConfig.USER_ID, databaseHelper.getUser(chatTitle).getUserId());
        messageAdapter = new MessageAdapter(getApplicationContext(), R.layout.chat_item, (ArrayList<Message>) messages);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        if (messages.size() > 0) lv_message.setAdapter(messageAdapter);
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            try {
                Message messageObj = new Message();
                messageObj.setMessage(message);
                messageObj.setUserId(userId);
                messageObj.setSender_id(AppConfig.USER_ID);
                messageObj.setExpiresTime(timeUtil.formatDateTime(timeUtil.getCurrentTime()));
                messageAdapter.add(messageObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            messageAdapter.notifyDataSetChanged();
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


    private static MessageSenderContent createMegContent(String regId, String title) {
        String message = txt_chat.getText().toString();
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
                databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                mgsSender = new MessageSender();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        MessageSenderContent mgsContent = createMegContent(registId, AppConfig.USER_NAME);
                        mgsSender.sendPost(mgsContent);
                        return null;
                    }
                }.execute();
                databaseHelper.addMessage(message, timeUtil.getCurrentTime(), userId, AppConfig.USER_ID);
                txt_chat.setText("");
                try {
                    Message messageObj = new Message();
                    messageObj.setMessage(message);
                    messageObj.setUserId(AppConfig.USER_ID);
                    messageObj.setSender_id(userId);
                    messageObj.setExpiresTime(timeUtil.formatDateTime(timeUtil.getCurrentTime()));
                    messageAdapter.add(messageObj);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                messageAdapter.notifyDataSetChanged();
                break;
        }
    }

}
