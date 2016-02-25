package training.com.chatgcmapplication;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static EditText txt_chat;
    private String registId;
    private String chatTitle;
    private MessageSender mgsSender;
    private int userId;
    private DatabaseHelper databaseHelper;
    private TimeUtil timeUtil;
    private MessageAdapter messageAdapter;
    private int offsetNumber = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        Button btn_send = (Button) findViewById(R.id.btn_send);
        txt_chat = (EditText) findViewById(R.id.txt_chat);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        lv_message = (ListView) findViewById(R.id.listMessage);
        lv_message.setOnItemClickListener(this);
        timeUtil = new TimeUtil();
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        btn_send.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        chatTitle = bundle.getString("titleName");
        if (getIntent().getBundleExtra("INFO") != null) {
            chatTitle = getIntent().getBundleExtra("INFO").getString("name");
            this.setTitle(chatTitle);
        } else {
            this.setTitle(chatTitle);
        }
        registId = bundle.getString("regId");
        userId = databaseHelper.getUser(chatTitle).getUserId();
        List<Message> messages = databaseHelper.getLastTenMessages(AppConfig.USER_ID, databaseHelper.getUser(chatTitle).getUserId(), 0);
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


    @Override
    public void onRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                List<Message> messages = databaseHelper.getLastTenMessages(AppConfig.USER_ID, databaseHelper.getUser(chatTitle).getUserId(), offsetNumber);
                messageAdapter.insertToTheFirst(messages);
                offsetNumber += 5;
                Log.i("Offset number", offsetNumber + "");

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                messageAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tvInfo = (TextView) view.findViewById(R.id.txtInfo);
        tvInfo.setVisibility(View.VISIBLE);
    }
}
