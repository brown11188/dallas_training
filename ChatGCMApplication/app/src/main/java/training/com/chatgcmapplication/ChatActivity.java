package training.com.chatgcmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import training.com.adapter.MessageAdapter;
import training.com.common.AppConfig;
import training.com.common.RetrofitCallBackUtil;
import training.com.common.RetrofitGenerator;
import training.com.common.TimeUtil;
import training.com.dao.RESTDatabaseDAO;
import training.com.dao.RetrofitResponseCallBack;
import training.com.model.Message;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static EditText txt_chat;
    private String registId;
    private MessageSender mgsSender;
    private int userId;
    private TimeUtil timeUtil;
    private MessageAdapter messageAdapter;
    private int offsetNumber = 5;
    @Bind(R.id.btn_send)
    Button btn_send;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listMessage)
    ListView lv_message;
    private RetrofitGenerator retrofitGenerator;
    private RetrofitCallBackUtil retrofitCallBackUtil;
    private RESTDatabaseDAO service;
    private static final int OFFSET_NUMBER_DEFAULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        txt_chat = (EditText) findViewById(R.id.txt_chat);
        swipeRefreshLayout.setOnRefreshListener(this);
        lv_message.setOnItemClickListener(this);
        timeUtil = new TimeUtil();
        retrofitGenerator = new RetrofitGenerator();
        retrofitCallBackUtil = new RetrofitCallBackUtil();
        Retrofit client = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(retrofitGenerator.gsonDateDeserializerGenerator()))
                .build();
        service = client.create(RESTDatabaseDAO.class);
        btn_send.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        String chatTitle = bundle.getString("titleName");
        if (getIntent().getBundleExtra("INFO") != null) {
            chatTitle = getIntent().getBundleExtra("INFO").getString("name");
            this.setTitle(chatTitle);
        } else {
            this.setTitle(chatTitle);
        }
        registId = bundle.getString("regId");
        userId = bundle.getInt("userId");
        final Call<ArrayList<Message>> call = service.getLastTenMessage(AppConfig.USER_ID, userId, OFFSET_NUMBER_DEFAULT);
        Thread thread =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Message> messages = call.execute().body();
                    messageAdapter = new MessageAdapter(getApplicationContext(), R.layout.chat_item, messages);
                    LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onNotice, new IntentFilter("Msg"));
                    if (messages.size() > 0) lv_message.setAdapter(messageAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

//        retrofitCallBackUtil.getLastTenMessageCallBack(AppConfig.USER_ID, userId, OFFSET_NUMBER_DEFAULT, service, new RetrofitResponseCallBack() {
//            @Override
//            public void onSuccess(ArrayList<Message> messages) {
//                messageAdapter = new MessageAdapter(getApplicationContext(), R.layout.chat_item, messages);
//                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onNotice, new IntentFilter("Msg"));
//                if (messages.size() > 0) lv_message.setAdapter(messageAdapter);
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
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

    @OnClick({R.id.btn_send, R.id.cryImg, R.id.smileImg, R.id.sadImg, R.id.angryImg})
    @Override
    public void onClick(View v) {
        final String message;
        switch (v.getId()) {
            case R.id.btn_send:

                message = txt_chat.getText().toString();
                if (!message.equals("")) {
                    mgsSender = new MessageSender();
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            MessageSenderContent mgsContent = createMegContent(registId, AppConfig.USER_NAME);
                            Retrofit client = retrofitGenerator.createRetrofit();
                            RESTDatabaseDAO service = client.create(RESTDatabaseDAO.class);
                            if (mgsSender.sendPost(mgsContent)) {
                                retrofitCallBackUtil.addMessageToServer(message, userId, service);
                            }
                            return null;
                        }
                    }.execute();
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
                }
                break;
            case R.id.cryImg:
                showEmoji(":((");
                break;
            case R.id.smileImg:
                showEmoji(":D");
                break;
            case R.id.angryImg:
                showEmoji(":@");
                break;
            case R.id.sadImg:
                showEmoji(":(");
                break;
        }
    }


    @Override
    public void onRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                retrofitCallBackUtil.getLastTenMessageCallBack(AppConfig.USER_ID, userId, offsetNumber, service, new RetrofitResponseCallBack() {
                    @Override
                    public void onSuccess(ArrayList<Message> messages) {
                        messageAdapter.insertToTheFirst(messages);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                offsetNumber += 5;
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

    private void showEmoji(String emoji) {
        txt_chat.setText(txt_chat.getText().toString() + emoji);
        txt_chat.setSelection(txt_chat.getText().length());
    }


}
