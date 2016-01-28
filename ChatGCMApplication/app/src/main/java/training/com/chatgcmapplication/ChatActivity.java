package training.com.chatgcmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChatActivity extends AppCompatActivity {
    private Button btn_send;
    private EditText txt_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_send = (Button) findViewById(R.id.btn_send);
        txt_chat = (EditText) findViewById(R.id.txt_chat);

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

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
            Log.i("Service", "From: " + intent.getStringExtra("from") + " ,Message: " + intent.getStringExtra("message"));

        }
    };


}
